package teamHTBP.vidaReforged.client.screen.screens.guidebook;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.ILifeCycleOwner;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycle;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycleRegistry;

/**类似于ActivityFragment*/
public abstract class VidaGuidebookPage extends VidaWidget implements ILifeCycleOwner {
    LifeCycleRegistry registry;

    public VidaGuidebookPage(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        this.registry = new LifeCycleRegistry(this);
    }

    public void init(){
        this.onInit();
        registry.handleLifecycleEvent(LifeCycle.Event.ON_CREATE);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            registry.handleLifecycleEvent(LifeCycle.Event.ON_RESUME);
            this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
            this.renderWidget(graphics, mouseX, mouseY, partialTicks);
        }
    }

    /**移除*/
    public void onRemove(){
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_REMOVE);
    }

    public void onHide(){
        this.visible = false;
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_HIDE);
    }

    public abstract void onInit();

    @Override
    public LifeCycle getLifeCycle() {
        return registry;
    }
}
