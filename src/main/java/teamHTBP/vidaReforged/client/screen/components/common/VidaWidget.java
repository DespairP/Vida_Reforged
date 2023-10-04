package teamHTBP.vidaReforged.client.screen.components.common;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class VidaWidget extends AbstractWidget {
    protected final int initialX;
    protected final int initialY;
    protected int offsetX;
    protected int offsetY;
    private Screen parent;

    public VidaWidget(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        this.initialX = x;
        this.initialY = y;
    }


    @Override
    public int getX() {
        return initialX + offsetX;
    }

    @Override
    public int getY() {
        return initialY + offsetY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffset(int offsetX, int offsetY){
        setOffsetX(offsetX);
        setOffsetY(offsetY);
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }

    public Screen getParent(){
        return parent;
    }
}
