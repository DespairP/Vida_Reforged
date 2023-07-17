package teamHTBP.vidaReforged.server.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;

public class MagicWordCraftingTableMenu extends AbstractContainerMenu {
    public static final String MENU_NAME = "magic_word_crafting_table";
    private final ContainerLevelAccess access;
    private final Inventory playerInventory;
    private final BlockPos blockPos;

    public MagicWordCraftingTableMenu(int menuId, ContainerLevelAccess access, Inventory inventory, BlockPos pos) {
        super(VidaMenuContainerTypeLoader.MAGIC_WORD_CRAFTING.get(), menuId);
        this.access = access;
        this.playerInventory = inventory;
        this.blockPos = pos;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int number) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, VidaBlockLoader.MAGIC_WORD_CRAFTING.get());
    }
}
