package teamHTBP.vidaReforged.client.screen.screens.common;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelStore;
import teamHTBP.vidaReforged.core.common.ui.component.IViewModelStoreProvider;

public class VidaScreen extends Screen implements IViewModelStoreProvider {
    private ViewModelStore store;

    public VidaScreen(Component component) {
        super(component);
    }

    @Override
    protected void init() {
        super.init();
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
}
