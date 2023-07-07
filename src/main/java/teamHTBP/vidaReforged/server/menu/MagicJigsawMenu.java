package teamHTBP.vidaReforged.server.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;

public class MagicJigsawMenu extends AbstractContainerMenu {
    public static String MENU_NAME = "magic_jigsaw_equipment_table";
    private ContainerLevelAccess access;
    private Inventory inventory;
    private BlockPos pos;

    public MagicJigsawMenu(int containerId, ContainerLevelAccess access, Inventory inventory, BlockPos pos) {
        super(VidaMenuContainerTypeLoader.JIGSAW_EQUIP.get(), containerId);
        this.access = access;
        this.inventory = inventory;
        this.pos = pos;
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, VidaBlockLoader.JIGSAW_EQUIP.get());
    }
}
