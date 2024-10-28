package teamHTBP.vidaReforged.server.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.server.menu.slots.FobiddenSlot;

import java.util.ArrayList;
import java.util.List;

public abstract class VidaAbstractMenu extends AbstractContainerMenu {

    protected final ContainerLevelAccess access;
    protected Inventory playerInventory;
    /**每一格宽*/
    private final static int COL_SIZE = 18;
    /**物品栏列数*/
    private final static int INVENTORY_COL_AMOUNT = 9;
    /**物品栏里面的行数*/
    private final static int INVENTORY_ROW_AMOUNT = 3;
    private final List<Slot> playerInventorySlots;

    protected VidaAbstractMenu(@Nullable MenuType<?> menuType, int containerId, ContainerLevelAccess access, Inventory playerInventory, int xBackpackOffset, int yBackpackOffset) {
        super(menuType, containerId);
        this.access = access;
        this.playerInventory = playerInventory;
        this.playerInventorySlots = new ArrayList<>();

        // 背包栏位
        for(int row = 0; row < INVENTORY_ROW_AMOUNT; ++row) {
            for(int col = 0; col < INVENTORY_COL_AMOUNT; ++col) {
                int slotNumber = col + row * 9 + 9;
                Slot slot = new Slot(playerInventory, slotNumber, 8 + col * COL_SIZE + xBackpackOffset, row * COL_SIZE + yBackpackOffset);
                this.addSlot(slot);
                this.playerInventorySlots.add(slot);
            }
        }

        // 快捷栏位
        for(int col = 0; col < INVENTORY_COL_AMOUNT; ++col) {
            Slot slot = new Slot(playerInventory, col, 8 + col * 18 + yBackpackOffset, 161 - 103 + yBackpackOffset);
            this.addSlot(slot);
            this.playerInventorySlots.add(slot);
        }
    }

    public final List<Slot> playerInventorySlots() {
        return playerInventorySlots != null ? playerInventorySlots : new ArrayList<>();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }
}
