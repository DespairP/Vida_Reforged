package teamHTBP.vidaReforged.core.common.ui.component;

import teamHTBP.vidaReforged.client.events.gui.ScreenViewModelPluginHandler;

/**
 * 表示UI容器可以提供ViewModelStore的接口
 * {@link ViewModelStore}
 * @see ScreenViewModelPluginHandler
 * */
public interface IViewModelStoreProvider {
    /**容器*/
    public IViewModelStoreProvider getHolder();

    /**获取ViewModelStore*/
    public ViewModelStore getStore();
}
