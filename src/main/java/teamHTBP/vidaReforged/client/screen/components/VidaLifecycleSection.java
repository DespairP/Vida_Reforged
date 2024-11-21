package teamHTBP.vidaReforged.client.screen.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.ILifeCycleOwner;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycle;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycleRegistry;

public abstract class VidaLifecycleSection extends VidaWidget implements ILifeCycleOwner {

    protected LifeCycleRegistry registry;

    @Deprecated
    public VidaLifecycleSection(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        this.registry = new LifeCycleRegistry(this);
    }

    public VidaLifecycleSection(int x, int y, int width, int height, Component component, ResourceLocation componentId) {
        super(x, y, width, height, component, componentId);
        this.registry = new LifeCycleRegistry(this);
    }

    @Override
    public final void render(GuiGraphics graphics, int x, int y, float partialTicks) {
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_RESUME);
        super.render(graphics, x, y, partialTicks);
    }

    public final void init(){
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_CREATE);
        this.onInit();
    }

    public void onInit(){};

    public final void start(){
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_START);
        this.onStart();
    }

    public void onStart(){};

    public final void pause(){
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_PAUSE);
        this.onPause();
    }

    public void onPause(){};

    public final void resume(){
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_RESUME);
        this.onResume();
    }

    public void onResume(){};

    public void hide(){
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_HIDE);
        this.onHide();
    }

    public void onHide(){};

    @Override
    public LifeCycle getLifeCycle() {
        return registry;
    }

}
