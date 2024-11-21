package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;

public class VidaGuidebookWhite extends VidaWidget implements IVidaGuidebookComponent {

    public VidaGuidebookWhite(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
    }

    @Override
    public void init() {

    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {}
}
