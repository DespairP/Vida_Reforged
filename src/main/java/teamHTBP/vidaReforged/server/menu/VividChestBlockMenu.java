package teamHTBP.vidaReforged.server.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import static teamHTBP.vidaReforged.server.menu.VidaMenuContainerTypeLoader.VIVID_CHEST_MENU;

public class VividChestBlockMenu extends AbstractContainerMenu {
    private final int containerRows;
    private final Container container;
    /**MENU*/
    public final static String MENU_NAME = "vivid_chest";
    /**物品栏列数*/
    private final static int INVENTORY_COL_AMOUNT = 9;
    /**物品栏里面的行数*/
    private final static int INVENTORY_ROW_AMOUNT = 3;
    private final static int COL_SIZE = 18;
    public VividChestBlockMenu(int containerId, Inventory playerInventory, Container container) {
        super(VIVID_CHEST_MENU.get(), containerId);
        this.containerRows = 3;
        this.container = container;
        this.container.startOpen(playerInventory.player);

        // 箱子栏位
        for(int j = 0; j < this.containerRows; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(container, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        // 背包栏位
        for(int row = 0; row < INVENTORY_ROW_AMOUNT; ++row) {
            for(int col = 0; col < INVENTORY_COL_AMOUNT; ++col) {
                int slotNumber = col + row * 9 + 9;
                this.addSlot(new Slot(playerInventory, slotNumber, 8 + col * COL_SIZE, row * COL_SIZE + 86));
            }
        }

        // 快捷栏位
        for(int col = 0; col < INVENTORY_COL_AMOUNT; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 161 - 103 + 86));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotNumber) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotNumber);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotNumber < this.containerRows * 9) {
                if (!this.moveItemStackTo(itemstack1, this.containerRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.containerRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
