package teamHTBP.vidaReforged.core.common.ui.lifecycle;

public abstract class LifeCycle {

    public abstract void addObserver(ILifeCycleObserver observer);

    public abstract void removeObserver(ILifeCycleObserver observer);

    public abstract LifeCycle.State getCurrentState();

    public enum Event{
        //
        ON_CREATE,
        //
        ON_START,
        ON_RESUME,
        ON_PAUSE,
        //
        ON_HIDE,
        //
        ON_REMOVE;
    }

    /*
    * 状态
    * INITIALIZED (ON_CREATE) -> CREATED/ADDED  (ON_START) -> STARTED (ON_RESUME) -> RESUMED (ON_PAUSE) -> STARTED (ON_HIDE) -> CREATED (ON_REMOVED) -> REMOVE
    * */
    public enum State{
        // 被移除时
        REMOVED,
        // 初始状态
        INITIALIZED,
        // 实例化完成时
        CREATED,
        // 添加窗口后初始化数据init后时
        STARTED,
        // 重建
        RESUMED;

        public boolean isAtLeast(State state) {
            return compareTo(state) >= 0;
        }
    }
}
