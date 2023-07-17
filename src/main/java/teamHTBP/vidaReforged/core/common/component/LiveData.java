package teamHTBP.vidaReforged.core.common.component;

import net.minecraftforge.client.model.renderable.IRenderable;

import java.util.LinkedList;

public class LiveData<T> {
    T data;
    LinkedList<IDataObserver<T>> observersList;

    public LiveData(T data) {
        this.data = data;
        this.observersList = new LinkedList<>();
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

    public T getValue(){
        return this.data;
    }

    public void clearObservers(){
        this.observersList.clear();
    }
}
