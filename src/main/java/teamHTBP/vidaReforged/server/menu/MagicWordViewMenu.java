package teamHTBP.vidaReforged.server.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static teamHTBP.vidaReforged.server.menu.VidaMenuContainerTypeLoader.MAGIC_WORD_VIEWING;

public class MagicWordViewMenu extends AbstractContainerMenu {
    public static final String MENU_NAME = "magic_word_view";
    /***/
    private List<String> playerMagicWords = new ArrayList<>();

    public MagicWordViewMenu(int containerId, List<String> playerMagicWords) {
        super(MAGIC_WORD_VIEWING.get(), containerId);
        this.playerMagicWords = playerMagicWords;
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }

    public List<String> getPlayerMagicWords() {
        return playerMagicWords;
    }
}
