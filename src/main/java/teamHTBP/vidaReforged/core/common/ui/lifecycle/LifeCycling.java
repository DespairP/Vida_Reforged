package teamHTBP.vidaReforged.core.common.ui.lifecycle;


public class LifeCycling {
    static IGenericLifeCycleObserver getCallback(Object object) {
        if (object instanceof IFullLifeCycleObserver) {
            return new FullLifeCycleObserverAdapter((IFullLifeCycleObserver) object);
        }
        if (object instanceof IGenericLifeCycleObserver) {
            return (IGenericLifeCycleObserver) object;
        }

        throw new UnsupportedOperationException("reflective annotation is not supported");
    }
}
