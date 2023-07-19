package teamHTBP.vidaReforged.server.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.client.screen.components.MagicWordListWidget;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;

public class MagicWordCraftingTableMenu extends AbstractContainerMenu {
    public static final String MENU_NAME = "magic_word_crafting_table";
    private final ContainerLevelAccess access;
    private final Inventory playerInventory;
    private final BlockPos blockPos;
    private final static int COL_SIZE = 18;

    private final static int INVENTORY_COL_AMOUNT = 9;

    private final static int INVENTORY_ROW_AMOUNT = 3;

    public MagicWordCraftingTableMenu(int menuId, ContainerLevelAccess access, Inventory inventory, BlockPos pos) {
        super(VidaMenuContainerTypeLoader.MAGIC_WORD_CRAFTING.get(), menuId);
        this.access = access;
        this.playerInventory = inventory;
        this.blockPos = pos;

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
    }

    @Override
    public ItemStack quickMoveStack(Player player, int number) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, VidaBlockLoader.MAGIC_WORD_CRAFTING.get());
    }

    public BlockPos getBlockPos(){
        return blockPos;
    }


}
