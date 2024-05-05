package teamHTBP.vidaReforged.core.common.ui.lifecycle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.utils.collections.FastSafeIterableMap;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycle.Event.*;
import static teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycle.State.*;

/**
 * LifeCycle管理
 * */
public class LifeCycleRegistry extends LifeCycle{
    private State state;
    private final WeakReference<ILifeCycleOwner> lifeCycleOwner;
    private FastSafeIterableMap<ILifeCycleObserver, ObserverWithState> observerMap = new FastSafeIterableMap<>();
    /**正在加入的观察者数量*/
    private int addingObserverCounter = 0;
    /***/
    private boolean isHandlingEvent;
    private boolean isNewEventOccurred;
    private ArrayList<State> parentStates = new ArrayList<>();
    private static final Logger LOGGER = LogManager.getLogger();

    public LifeCycleRegistry(ILifeCycleOwner lifeCycleOwner) {
        this.lifeCycleOwner = new WeakReference<>(lifeCycleOwner);
        this.state = INITIALIZED;
    }


    /**添加lifecycle观察者*/
    @Override
    public void addObserver(ILifeCycleObserver observer) {
        State initailState = state == REMOVED ? REMOVED : INITIALIZED;
        // 包装为ObserverWithState并放入map中
        ObserverWithState statefulObserver = new ObserverWithState(observer, initailState);
        ObserverWithState previous = observerMap.putIfAbsent(observer, statefulObserver);
        if(previous != null){
            return;
        }

        // 查询实例是否被GC回收，如果被回收则不加入
        ILifeCycleOwner owner = lifeCycleOwner.get();
        if(owner == null){
            return;
        }

        // 判断是否重入
        boolean isReentrance = addingObserverCounter != 0 || isHandlingEvent;
        // 获取目标state
        State targetState = calculateTargetState(observer);
        // 标记状态正在处理加入的观察者数量
        addingObserverCounter++;

        // 如果观察者的state离目标的state小（比如：INITIALIZED到STARTED），循环到那个state为止
        while((statefulObserver.state.compareTo(targetState) < 0 && observerMap.contains(observer))) {
            // 暂存原来状态
            pushParentState(statefulObserver.state) ;
            final Event event = upEvent(statefulObserver.state) ;
            // 分发状态
            statefulObserver.dispatchEvent(owner, event) ;
            // 删除原来状态
            popParentState();
            targetState = calculateTargetState(observer) ;
        }
        if(!isReentrance) {
            sync() ;
        }

        // 清空状态
        addingObserverCounter--;
    }

    /**计算目标的State*/
    private State calculateTargetState(ILifeCycleObserver observer) {
        Map.Entry<ILifeCycleObserver, ObserverWithState> previous = observerMap.ceil(observer);
        State siblingState = previous != null ? previous.getValue().state : null;
        State parentState = !parentStates.isEmpty() ? parentStates.get(parentStates.size() - 1) : null;
        return min(min(state, siblingState), parentState);
    }

    static State min(State state1, State state2) {
        return state2 != null && state2.compareTo(state1) < 0 ? state2 : state1;
    }

    @Override
    public LifeCycle.State getCurrentState() {
        return state;
    }
    @Override
    public void removeObserver(ILifeCycleObserver observer) {
        observerMap.remove(observer);
    }

    /**获取Event后状态*/
    static State getStateAfter(Event event) {
        switch (event) {
            case ON_CREATE, ON_HIDE -> {
                return CREATED;
            }
            case ON_START, ON_PAUSE -> {
                return STARTED;
            }
            case ON_RESUME -> {
                return RESUMED;
            }
            case ON_REMOVE -> {
                return REMOVED;
            }
        }
        throw new IllegalArgumentException("Unexpected event value " + event);
    }


    private static Event downEvent(State state) {
        switch (state) {
            case INITIALIZED -> throw new IllegalArgumentException();
            case CREATED -> {
                return ON_REMOVE;
            }
            case STARTED -> {
                return ON_HIDE;
            }
            case RESUMED -> {
                return ON_PAUSE;
            }
            case REMOVED -> throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException("Unexpected state value " + state);
    }


    private static Event upEvent(State state) {
        switch (state) {
            case INITIALIZED, REMOVED -> {
                return ON_CREATE;
            }
            case CREATED -> {
                return ON_START;
            }
            case STARTED -> {
                return ON_RESUME;
            }
            case RESUMED -> throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException("Unexpected state value " + state);
    }

    /**标记当前状态为需要的状态*/
    public void markState(State state) {
        moveToState(state);
    }

    public void handleLifecycleEvent(LifeCycle.Event event) {
        State next = getStateAfter(event);
        moveToState(next);
    }

    /***/
    private void moveToState(State next) {
        if (state == next) {
            return;
        }
        state = next;
        if (isHandlingEvent || addingObserverCounter != 0) {
            isNewEventOccurred = true;
            return;
        }
        isHandlingEvent = true;
        sync();
        isHandlingEvent = false;
    }

    private void forwardPass(ILifeCycleOwner lifecycleOwner) {
        Iterator<Map.Entry<ILifeCycleObserver, ObserverWithState>> ascendingIterator = observerMap.iteratorWithAdditions();
        while (ascendingIterator.hasNext() && !isNewEventOccurred) {
            Map.Entry<ILifeCycleObserver, ObserverWithState> entry = ascendingIterator.next();
            ObserverWithState observer = entry.getValue();
            while ((observer.state.compareTo(state) < 0 && !isNewEventOccurred && observerMap.contains(entry.getKey()))) {
                pushParentState(observer.state);
                observer.dispatchEvent(lifecycleOwner, upEvent(observer.state));
                popParentState();
            }
        }
    }
    private void backwardPass(ILifeCycleOwner lifecycleOwner) {
        Iterator<Map.Entry<ILifeCycleObserver, ObserverWithState>> descendingIterator = observerMap.descendingIterator();
        while (descendingIterator.hasNext() && !isNewEventOccurred) {
            Map.Entry<ILifeCycleObserver, ObserverWithState> entry = descendingIterator.next();
            ObserverWithState observer = entry.getValue();
            while ((observer.state.compareTo(state) > 0 && !isNewEventOccurred && observerMap.contains(entry.getKey()))) {
                Event event = downEvent(observer.state);
                pushParentState(getStateAfter(event));
                observer.dispatchEvent(lifecycleOwner, event);
                popParentState();
            }
        }
    }

    private void popParentState() {
        parentStates.remove(parentStates.size() - 1);
    }
    private void pushParentState(State state) {
        parentStates.add(state);
    }

    private void sync() {
        // 判定是否被GC回收
        ILifeCycleOwner lifecycleOwner = this.lifeCycleOwner.get();
        if (lifecycleOwner == null) {
            LOGGER.warn("LifecycleOwner is garbage collected, you shouldn't try dispatch new events from it.");
            return;
        }

        //
        while (!isSynced()) {
            isNewEventOccurred = false;
            // 判断当前的状态是否比队列头的状态小，如果队列太大了要继续处理
            if (state.compareTo(observerMap.eldest().getValue().state) < 0) {
                backwardPass(lifecycleOwner);
            }
            // 判断当前的状态是否比队列尾的状态大，如果队列太小了要继续处理
            Map.Entry<ILifeCycleObserver, ObserverWithState> newest = observerMap.newest();
            if (!isNewEventOccurred && newest != null && state.compareTo(newest.getValue().state) > 0) {
                forwardPass(lifecycleOwner);
            }
        }
        isNewEventOccurred = false;
    }

    private boolean isSynced() {
        if (observerMap.size() == 0) {
            return true;
        }
        State eldestObserverState = observerMap.eldest().getValue().state;
        State newestObserverState = observerMap.newest().getValue().state;
        return eldestObserverState == newestObserverState && state == newestObserverState;
    }

    static class ObserverWithState {
        State state;
        IGenericLifeCycleObserver mLifecycleObserver;

        ObserverWithState(ILifeCycleObserver observer, State initialState) {
            mLifecycleObserver = LifeCycling.getCallback(observer);
            state = initialState;
        }
        void dispatchEvent(ILifeCycleOwner owner, Event event) {
            State newState = getStateAfter(event);
            state = min(state, newState);
            mLifecycleObserver.onStateChanged(owner, event);
            state = newState;
        }
    }
}
