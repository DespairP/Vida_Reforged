package teamHTBP.vidaReforged.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import teamHTBP.vidaReforged.server.menu.MagicJigsawMenu;

import java.util.List;

/**拼图，不再维护*/
@Deprecated
public class MagicJigsawScreen extends AbstractContainerScreen<MagicJigsawMenu> {
    /**显示客户端玩家的拼图*/
    private NonNullList<?> playerJigsawList = NonNullList.create();

    public MagicJigsawScreen(MagicJigsawMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float p_97788_, int p_97789_, int p_97790_) {
        renderBackground(graphics);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int pMouseX, int pMouseY) {

    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);


    }



    @Override
    protected void init() {
        super.init();
    }

    private List<?> getPlayerJigsaw(){
        return null;
    }

}
