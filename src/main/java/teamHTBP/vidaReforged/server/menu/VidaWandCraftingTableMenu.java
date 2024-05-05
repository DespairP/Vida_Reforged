package teamHTBP.vidaReforged.server.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.capability.Result;
import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.menu.slots.FobiddenSlot;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandEquipmentSlot;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandSlot;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class VidaWandCraftingTableMenu extends AbstractContainerMenu {

    public static final String MENU_NAME = "vida_wand_crafting_table";
    private ContainerLevelAccess access;
    private Inventory playerInventory;
    private BlockPos blockPos;
    /**每一格宽*/
    private final static int COL_SIZE = 18;
    /**物品栏列数*/
    private final static int INVENTORY_COL_AMOUNT = 9;
    /**物品栏里面的行数*/
    private final static int INVENTORY_ROW_AMOUNT = 3;
    /***/
    private Map<Position,VidaWandEquipmentSlot> equipmentSlotMap = new HashMap<>();
    /***/
    private SimpleContainer equipmentSlots = new SimpleContainer(5);
    /***/
    private Map<Integer, ResourceLocation> magics;
    private ItemStack wandStack;
    private VidaWandSlot wandSlot;
    private int wandIndex;


    public VidaWandCraftingTableMenu(int windowId, Inventory inventory, BlockPos pos, int wandIndex) {
        super(VidaMenuContainerTypeLoader.VIDA_WAND_CRAFTING_TABLE.get(), windowId);
        this.access = ContainerLevelAccess.create(inventory.player.level(), pos);
        this.playerInventory = inventory;
        this.blockPos = pos;
        this.wandIndex = wandIndex;

        // 添加玩家背包槽位
        int xOffset = 0;
        int yOffset = 0;

        // 背包栏位
        for(int row = 0; row < INVENTORY_ROW_AMOUNT; ++row) {
            for(int col = 0; col < INVENTORY_COL_AMOUNT; ++col) {
                int slotNumber = col + row * 9 + 9;
                if(slotNumber != wandIndex) {
                    this.addSlot(
                            new Slot(inventory, slotNumber, 8 + col * COL_SIZE + xOffset, row * COL_SIZE + yOffset)
                    );
                } else {
                    // 如果是法杖槽位,槽位不可拖动
                    this.wandStack = inventory.getItem(slotNumber);
                    this.addSlot(new FobiddenSlot(inventory, slotNumber, 8 + col * COL_SIZE + xOffset, row * COL_SIZE + yOffset, false));
                }
            }
        }

        // 快捷栏位
        for(int col = 0; col < INVENTORY_COL_AMOUNT; ++col) {
            int slotNumber = col;
            if(slotNumber != wandIndex) {
                this.addSlot(new Slot(inventory, slotNumber, 8 + col * 18 + xOffset, 161 - 103 + yOffset));
            } else {
                // 如果是法杖槽位,槽位不可拖动
                this.wandStack = inventory.getItem(slotNumber);
                this.addSlot(new FobiddenSlot(inventory, slotNumber, 8 + col * 18 + xOffset, 161 - 103 + yOffset, false));
            }
        }

        // 添加饰品槽
        this.access.execute((level, pPos) -> {
            Position[] position = Position.values();
            // 获取法杖下标
            int index = Position.values().length;
            // 0~3是饰品槽位,4是法杖槽
            for(int i = 0; i < index; i++){
                VidaWandEquipmentSlot slot = new VidaWandEquipmentSlot(this.equipmentSlots, i, 0, 0, position[i]);
                this.equipmentSlotMap.put(position[i], slot);
                this.addSlot(slot);
            }
            this.wandSlot = new VidaWandSlot(this.equipmentSlots, index, -68, 0, itemStack -> itemStack.is(VidaItemLoader.VIDA_WAND.get()));
            //添加法杖槽
            this.addSlot(wandSlot);
        });

        // 暂时存储法杖槽位
        this.wandSlot.set(wandStack);

        // 读取法杖的魔法
        this.magics = readWandMagics();
    }

    /**读取法杖现在的魔法*/
    public Map<Integer, ResourceLocation> readWandMagics(){
        Map<Integer, ResourceLocation> magics = new LinkedHashMap<>();
        wandStack.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER).ifPresent(
                cap -> {
                    List<ResourceLocation> availableMagics = cap.getAvailableMagics();
                    for(int i = 0; i < availableMagics.size(); i++){
                        magics.put(i, availableMagics.get(i));
                    }
                }
        );
        return magics;
    }

    /**SHIFT快捷键暂时无法使用*/
    @Override
    public ItemStack quickMoveStack(Player player, int number) {
        return ItemStack.EMPTY;
    }

    /**判断是否还可以显示menu*/
    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, VidaBlockLoader.VIDA_WAND_CRATING_TABLE.get()) && playerInventory.getItem(wandIndex).is(VidaItemLoader.VIDA_WAND.get());
    }

    /**获取方块位置*/
    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Map<Position, VidaWandEquipmentSlot> getEquipmentSlots() {
        return equipmentSlotMap;
    }

    /**在menu关闭时*/
    @Override
    public void removed(Player player) {
        ItemStack wandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        wandSlot.onTake(player, wandItem);
        LazyOptional<IVidaMagicContainerCapability> toolCapability = wandItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
        toolCapability.ifPresent(cap -> cap.setMagics(magics));
        super.removed(player);
    }

    /**获取现在的魔法*/
    public Map<Integer, ResourceLocation> getMagics() {
        return magics;
    }

    /**
     * 通过Packet设置魔法
     * @see teamHTBP.vidaReforged.server.packets.MagicSelectionPacket
     * */
    public void setMagics(Map<Integer, ResourceLocation> magics) {
        this.magics = magics;
    }
}
