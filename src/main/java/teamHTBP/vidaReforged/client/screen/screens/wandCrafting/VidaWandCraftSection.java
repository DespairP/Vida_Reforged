package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.ILifeCycleOwner;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycle;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycleRegistry;

public abstract class VidaWandCraftSection extends VidaWidget implements ILifeCycleOwner {

    LifeCycleRegistry registry;

    public VidaWandCraftSection(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        this.registry = new LifeCycleRegistry(this);
    }

    public void init(){
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_CREATE);
    }

    public void pause(){
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_PAUSE);
    }

    public void resume(){
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_RESUME);
        setVisible(true);
    }

    public void hide(){
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_HIDE);
        setVisible(false);
    }

    @Override
    public LifeCycle getLifeCycle() {
        return registry;
    }
}
