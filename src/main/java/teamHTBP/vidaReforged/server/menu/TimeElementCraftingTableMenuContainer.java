package teamHTBP.vidaReforged.server.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;

import java.util.Optional;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class TimeElementCraftingTableMenuContainer extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final Inventory playerInventory;
    public static final String MENU_NAME = "time_element_crafting_table";
    private BlockPos blockPos;

    public TimeElementCraftingTableMenuContainer(int pContainerId, ContainerLevelAccess access, Inventory inventory,BlockPos pos) {
        super(VidaMenuContainerTypeLoader.TIME_ELEMENT_MENU.get(), pContainerId);
        this.access = access;
        this.playerInventory = inventory;
        this.blockPos = pos;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, VidaBlockLoader.TIME_ELEMENT_CRAFTING_TABLE.get());
    }

    public BlockPos getBlockPos(){
        return blockPos;
    }
}
