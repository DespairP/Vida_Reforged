package teamHTBP.vidaReforged.core.common.ui.component;

import java.util.LinkedList;
import java.util.List;

public class LiveData<T> {
    T data;
    LinkedList<IDataObserver<T>> observersList;

    public LiveData(T data) {
        this.data = data;
        this.observersList = new LinkedList<>();
    }

    public LiveData(){
        this(null);
    }

    public void observe(IDataObserver<T> observer){
        if(!this.observersList.contains(observer)){
            this.observersList.add(observer);
        }
    }

    public void removeObserver(IDataObserver<T> observer){
        this.observersList.remove(observer);
    }

    public void setValue(T newValue){
        this.data = newValue;
        this.observersList.forEach(observer -> observer.observe(data));
    }

    public void setValueWithoutNotify(T newValue){
        this.data = newValue;
    }

    public T getValue(){
        return this.data;
    }

    public void clearObservers(){
        this.observersList.clear();
    }
}
