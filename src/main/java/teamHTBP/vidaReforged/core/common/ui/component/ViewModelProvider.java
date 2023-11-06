package teamHTBP.vidaReforged.core.common.ui.component;

import java.lang.reflect.InvocationTargetException;

public class ViewModelProvider {
    private final ViewModelStore store;
    private final ViewModelFactory factory;

    private final static ViewModelFactory<ViewModel> DEFAULT_FACTORY = (modelClazz) -> {
        try {
            return (ViewModel) modelClazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    };

    public ViewModelProvider(IViewModelStoreProvider holder) {
        this(holder.getStore(), DEFAULT_FACTORY);
    }

    public ViewModelProvider(IViewModelStoreProvider holder, ViewModelFactory factory){
        this(holder.getStore(), factory);
    }

    public ViewModelProvider(ViewModelStore store, ViewModelFactory factory){
        this.store = store;
        this.factory = factory;
    }

    public <T extends ViewModel> T get(Class<T> modelClazz){
        String clazzName = modelClazz.getCanonicalName();
        if(clazzName == null || "".equals(clazzName)){
            throw new IllegalArgumentException("view-model cannot be generated in anonymous class");
        }
        return get(clazzName, modelClazz);
    }

    public <T extends ViewModel> T get(String key, Class<T> modelClazz){
        ViewModelStore store = this.store;
        ViewModel viewModel = store.get(key);
        if(modelClazz.isInstance(viewModel)){
            return (T) viewModel;
        }

        //TODO: Factory
        viewModel = factory.create(modelClazz);

        store.put(key, viewModel);
        return (T) viewModel;
    }

    public interface ViewModelFactory<T extends ViewModel>{
        public ViewModel create(Class<T> modelClazz);
    }
}
