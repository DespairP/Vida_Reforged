package teamHTBP.vidaReforged.core.common.ui.lifecycle;

public interface IGenericLifeCycleObserver extends ILifeCycleObserver{
    void onStateChanged(ILifeCycleOwner source, LifeCycle.Event event);
}
