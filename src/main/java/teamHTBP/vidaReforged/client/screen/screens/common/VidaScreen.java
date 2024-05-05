package teamHTBP.vidaReforged.client.screen.screens.common;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelStore;
import teamHTBP.vidaReforged.core.common.ui.component.IViewModelStoreProvider;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.ILifeCycleOwner;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycle;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycleRegistry;

import java.util.List;

public class VidaScreen extends Screen implements ILifeCycleOwner,IViewModelStoreProvider {
    private ViewModelStore store;
    private final LifeCycleRegistry registry;

    public VidaScreen(Component component) {
        super(component);
        this.registry = new LifeCycleRegistry(this);
    }

    @Override
    public void added() {
        super.added();
        registry.handleLifecycleEvent(LifeCycle.Event.ON_CREATE);
    }

    @Override
    protected void init() {
        super.init();
        registry.handleLifecycleEvent(LifeCycle.Event.ON_START);
        registry.handleLifecycleEvent(LifeCycle.Event.ON_RESUME);
    }


    @Override
    protected void rebuildWidgets() {
        registry.handleLifecycleEvent(LifeCycle.Event.ON_PAUSE);
        super.rebuildWidgets();
    }

    @Override
    public IViewModelStoreProvider getHolder() {
        return this;
    }

    @Override
    public ViewModelStore getStore() {
        if(store == null){
            this.store = new ViewModelStore();
        }
        return this.store;
    }

    public boolean keyPressed(int key, int scanmode, int modifier) {
        return super.keyPressed(key, scanmode, modifier);
    }

    @Override
    public LifeCycle getLifeCycle() {
        return registry;
    }

    @Override
    public void onClose() {
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_HIDE);
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_REMOVE);
        super.onClose();
    }
}
