package teamHTBP.vidaReforged.core.common.ui.lifecycle;

public interface IDefaultLifeCycleObserver extends IFullLifeCycleObserver{
    @Override
    default void onCreate(ILifeCycleOwner owner){
    }

    @Override
    default void onStart(ILifeCycleOwner owner){
    }

    @Override
    default void onResume(ILifeCycleOwner owner){
    }

    @Override
    default void onPause(ILifeCycleOwner owner) {
    }

    @Override
    default void onHide(ILifeCycleOwner owner){
    }

    @Override
    default void onDestroy(ILifeCycleOwner owner){
    }
}
