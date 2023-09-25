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
    private Map<Position,Slot> equipmentSlots = new HashMap<>();

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
            for(int i = 0; i < Objects.requireNonNull(entity).getEquipmentSlots().getContainerSize(); i++){
                Slot slot = new Slot(entity.getEquipmentSlots(),i, 0, 0);
                this.equipmentSlots.put(position[i], slot);
                this.addSlot(slot);
            }
        });

        //添加法杖槽

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

    public Map<Position, Slot> getEquipmentSlots() {
        return equipmentSlots;
    }
}
