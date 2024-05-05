package teamHTBP.vidaReforged.client.screen.viewModels;

import teamHTBP.vidaReforged.core.common.system.guidebook.VidaScreenEvent;
import teamHTBP.vidaReforged.core.common.ui.component.IDataObserver;
import teamHTBP.vidaReforged.core.common.ui.component.LiveData;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModel;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.ILifeCycleOwner;


public class VidaScreenEventChannelViewModel extends ViewModel {
    /**请不要在组件中监听此变量*/
    private LiveData<VidaScreenEvent> channel = new LiveData<>();


    /**发送事件*/
    public void pushMessage(VidaScreenEvent event){
        channel.setValue(event);
    }

    /**注册监听*/
    public void listenMessage(ILifeCycleOwner owner, IDataObserver<VidaScreenEvent> handler){
        channel.observe(owner, handler);
    }
}
