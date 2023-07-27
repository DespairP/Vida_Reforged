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

public class PrismMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final Inventory playerInventory;
    public static final String MENU_NAME = "prism";
    protected PrismMenu(int pContainerId, ContainerLevelAccess access, Inventory inventory, BlockPos pos) {
        super(VidaMenuContainerTypeLoader.PRISM.get(),pContainerId);
        this.access = access;
        this.playerInventory = inventory;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int number) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, VidaBlockLoader.MAGIC_WORD_CRAFTING.get());
    }
}
