package teamHTBP.vidaReforged.core.common.ui.lifecycle;

public interface IFullLifeCycleObserver extends ILifeCycleObserver{
    void onCreate(ILifeCycleOwner owner);
    void onStart(ILifeCycleOwner owner);
    void onResume(ILifeCycleOwner owner);
    void onPause(ILifeCycleOwner owner);
    void onHide(ILifeCycleOwner owner);
    void onDestroy(ILifeCycleOwner owner);
}
