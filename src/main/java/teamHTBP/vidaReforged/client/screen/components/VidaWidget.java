package teamHTBP.vidaReforged.client.screen.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.hud.IVidaNodes;
import teamHTBP.vidaReforged.core.common.ui.component.IViewModelStoreProvider;

public abstract class VidaWidget extends AbstractWidget implements IVidaNodes {
    protected int initialX;
    protected int initialY;
    protected int offsetX;
    protected int offsetY;
    private final ResourceLocation id;
    public Minecraft mc;

    @Deprecated
    public VidaWidget(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        this.initialX = x;
        this.initialY = y;
        this.id = new ResourceLocation(VidaReforged.MOD_ID, "widget");
        this.mc = Minecraft.getInstance();
    }

    public VidaWidget(int x, int y, int width, int height, Component component, ResourceLocation id) {
        super(x, y, width, height, component);
        this.initialX = x;
        this.initialY = y;
        this.id = id;
        this.mc = Minecraft.getInstance();
    }

    @Override
    public void setY(int y) {
        this.initialY = y;
    }

    @Override
    public void setX(int x) {
        this.initialX = x;
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
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {}

    public ResourceLocation getId() {
        return id;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.HINT, id.toLanguageKey());
    }

    public Screen getParent(){
        return Minecraft.getInstance().screen;
    }


    public void onAdded(){}

    @Override
    public <T extends IViewModelStoreProvider> T requireParent() {
        Screen parent = getParent();
        if(parent instanceof IViewModelStoreProvider){
            return (T) parent;
        }
        return null;
    }
}
