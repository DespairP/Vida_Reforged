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

    @Override
    public LifeCycle getLifeCycle() {
        return registry;
    }
}
