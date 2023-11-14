package teamHTBP.vidaReforged.server.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.server.blockEntities.VidaWandCraftingTableBlockEntity;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandSlot;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandEquipmentSlot;

import java.util.*;

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
    private Map<Position,VidaWandEquipmentSlot> equipmentSlots = new HashMap<>();

    public VidaWandCraftingTableMenu(int windowId, Inventory inventory, BlockPos pos) {
        super(VidaMenuContainerTypeLoader.VIDA_WAND_CRAFTING_TABLE.get(), windowId);
        this.access = ContainerLevelAccess.create(inventory.player.level(), pos);
        playerInventory = inventory;
        blockPos = pos;

        //添加玩家背包槽位
        int xOffset = 0;
        int yOffset = 0;

        for(int row = 0; row < INVENTORY_ROW_AMOUNT; ++row) {
            for(int col = 0; col < INVENTORY_COL_AMOUNT; ++col) {
                int slotNumber = col + row * 9 + 9;
                this.addSlot(
                        new Slot(inventory, slotNumber,8 + col * COL_SIZE + xOffset,row * COL_SIZE + yOffset)
                );
            }
        }

        for(int col = 0; col < INVENTORY_COL_AMOUNT; ++col) {
            int slotNumber = col;
            this.addSlot(new Slot(inventory, slotNumber, 8 + col * 18 + xOffset, 161 - 103 + yOffset));
        }

        //添加饰品槽
        this.access.execute((level, pPos) -> {
            VidaWandCraftingTableBlockEntity entity = (VidaWandCraftingTableBlockEntity) level.getBlockEntity(pPos);
            Position[] position = Position.values();
            //获取法杖下标
            int index = Position.values().length;
            //0~3是饰品槽位,4是法杖槽
            for(int i = 0; i < index; i++){
                VidaWandEquipmentSlot slot = new VidaWandEquipmentSlot(entity.getEquipmentSlots(), i, 0, 0, position[i]);
                this.equipmentSlots.put(position[i], slot);
                this.addSlot(slot);
            }
            //添加法杖槽
            this.addSlot(new VidaWandSlot(entity.getEquipmentSlots(), index, -68, 0, itemStack -> itemStack.is(VidaItemLoader.VIDA_WAND.get())));
        });
    }

    @Override
    public ItemStack quickMoveStack(Player player, int number) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, VidaBlockLoader.VIDA_WAND_CRATING_TABLE.get());
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Map<Position, VidaWandEquipmentSlot> getEquipmentSlots() {
        return equipmentSlots;
    }
}
