package teamHTBP.vidaReforged.core.common.ui.component;

import teamHTBP.vidaReforged.core.common.ui.lifecycle.IGenericLifeCycleObserver;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.ILifeCycleOwner;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycle;
import teamHTBP.vidaReforged.core.utils.collections.SafeIterableMap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import static teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycle.State.REMOVED;
import static teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycle.State.STARTED;

public class LiveData<T> {
    private int activeCount = 0;
    T data;
    SafeIterableMap<IDataObserver<T>, ObserverWrapper> observersList;
    private int version = -1;
    private boolean isDispatchingValue;
    private boolean isDispatchInvalidated;

    public LiveData(T data) {
        this.data = data;
        this.observersList = new SafeIterableMap<>();
    }

    public LiveData(){
        this(null);
    }


    public void observe(ILifeCycleOwner owner, IDataObserver<T> observer){
        if (owner.getLifeCycle().getCurrentState() == REMOVED) {
            return;
        }
        LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer);
        ObserverWrapper existing = observersList.putIfAbsent(observer, wrapper);
        if (existing != null && !existing.isAttachedTo(owner)) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (existing != null) {
            return;
        }
        owner.getLifeCycle().addObserver(wrapper);
    }

    public void observeForever(IDataObserver<T> observer){
        AlwaysActiveObserver wrapper = new AlwaysActiveObserver(observer);
        ObserverWrapper existing = observersList.putIfAbsent(observer, wrapper);
        if (existing != null && existing instanceof LiveData.LifecycleBoundObserver) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (existing != null) {
            return;
        }
        wrapper.activeStateChanged(true);
    }

    private void considerNotify(ObserverWrapper observer) {
        if (!observer.active) {
            return;
        }
        if (!observer.shouldBeActive()) {
            observer.activeStateChanged(false);
            return;
        }
        if (observer.mLastVersion >= version) {
            return;
        }
        observer.mLastVersion = version;
        observer.observer.observe((T) data);
    }

    public void removeObserver(IDataObserver<T> observer){
        this.observersList.remove(observer);
    }

    public void setValue(T newValue){
        version++;
        this.data = newValue;
        dispatchingValue(null);
    }

    private void dispatchingValue(ObserverWrapper initiator) {
        if (isDispatchingValue) {
            isDispatchInvalidated = true;
            return;
        }
        isDispatchingValue = true;
        do {
            isDispatchInvalidated = false;
            if (initiator != null) {
                considerNotify(initiator);
                initiator = null;
            } else {
                for (Iterator<Map.Entry<IDataObserver<T>, ObserverWrapper>> iterator = observersList.iteratorWithAdditions(); iterator.hasNext(); ) {
                    considerNotify(iterator.next().getValue());
                    if (isDispatchInvalidated) {
                        break;
                    }
                }
            }
        } while (isDispatchInvalidated);
        isDispatchingValue = false;
    }

    public void setValueWithoutNotify(T newValue){
        this.data = newValue;
    }

    public T getValue(){
        return this.data;
    }

    public void clearObservers(final ILifeCycleOwner owner){
        for (Map.Entry<IDataObserver<T>, ObserverWrapper> entry : observersList) {
            if (entry.getValue().isAttachedTo(owner)) {
                removeObserver(entry.getKey());
            }
        }
    }

    public void clearObservers(){
        for (Map.Entry<IDataObserver<T>, ObserverWrapper> entry : observersList) {
            removeObserver(entry.getKey());
        }
    }

    protected void onActive() {
    }

    protected void onInactive() {
    }

    class LifecycleBoundObserver extends ObserverWrapper implements IGenericLifeCycleObserver {
        final ILifeCycleOwner owner;

        LifecycleBoundObserver(ILifeCycleOwner owner, IDataObserver<T> observer) {
            super(observer);
            this.owner = owner;
        }

        @Override
        boolean shouldBeActive() {
            return owner.getLifeCycle().getCurrentState().isAtLeast(STARTED);
        }

        @Override
        public void onStateChanged(ILifeCycleOwner source, LifeCycle.Event event) {
            if (owner.getLifeCycle().getCurrentState() == REMOVED) {
                removeObserver(observer);
                return;
            }
            activeStateChanged(shouldBeActive());
        }
        @Override
        boolean isAttachedTo(ILifeCycleOwner owner) {
            return this.owner == owner;
        }

        @Override
        void detachObserver() {
            owner.getLifeCycle().removeObserver(this);
        }
    }
    private abstract class ObserverWrapper {
        final IDataObserver<T> observer;
        boolean active;
        int mLastVersion = -1;

        ObserverWrapper(IDataObserver< T > observer) {
            this.observer = observer;
        }

        abstract boolean shouldBeActive();

        boolean isAttachedTo(ILifeCycleOwner owner) {
            return false;
        }

        void detachObserver() {}

        void activeStateChanged(boolean newActive) {
            if (newActive == active) {
                return;
            }
            // immediately set active state, so we'd never dispatch anything to inactive
            // owner
            active = newActive;
            boolean wasInactive = LiveData.this.activeCount == 0;
            LiveData.this.activeCount += active ? 1 : -1;
            if (wasInactive && active) {
                onActive();
            }
            if (LiveData.this.activeCount == 0 && !active) {
                onInactive();
            }
            if (active) {
                dispatchingValue(this);
            }
        }
    }
    private class AlwaysActiveObserver extends ObserverWrapper {
        AlwaysActiveObserver(IDataObserver<T> observer) {
            super(observer);
        }
        @Override
        boolean shouldBeActive() {
            return true;
        }
    }

}
