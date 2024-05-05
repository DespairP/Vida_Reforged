package teamHTBP.vidaReforged.core.common.ui.lifecycle;

public class FullLifeCycleObserverAdapter implements IGenericLifeCycleObserver{

    private final IFullLifeCycleObserver mObserver;
    FullLifeCycleObserverAdapter(IFullLifeCycleObserver observer) {
        mObserver = observer;
    }

    @Override
    public void onStateChanged(ILifeCycleOwner source, LifeCycle.Event event) {
        switch (event) {
            case ON_CREATE -> mObserver.onCreate(source);
            case ON_START -> mObserver.onStart(source);
            case ON_RESUME -> mObserver.onResume(source);
            case ON_PAUSE -> mObserver.onPause(source);
            case ON_HIDE -> mObserver.onHide(source);
            case ON_REMOVE -> mObserver.onDestroy(source);
        }
    }
}
