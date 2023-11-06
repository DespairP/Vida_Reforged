package teamHTBP.vidaReforged.core.common.ui.component;

/**
 * 表示UI容器可以提供ViewModelStore的接口
 * {@link ViewModelStore}
 * @see teamHTBP.vidaReforged.client.events.ViewModelPluginHandler
 * */
public interface IViewModelStoreProvider {
    /**容器*/
    public IViewModelStoreProvider getHolder();

    /**获取ViewModelStore*/
    public ViewModelStore getStore();
}
