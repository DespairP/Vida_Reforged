package teamHTBP.vidaReforged.core.common.ui.component;

import net.minecraft.client.gui.screens.Screen;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**容器仓库*/
public class ViewModelStore {
    private final static Map<String, ViewModel> MANAGER = new HashMap<>();

    /**注册容器*/
    public final void put(String key,ViewModel viewModel){
        ViewModel oldModel = MANAGER.put(key, viewModel);
        if(oldModel != null){
            oldModel.onCleared();
        }
    }

    /**获取容器*/
    public ViewModel get(String key){
        return MANAGER.get(key);
    }

    /**获取所有容器的key*/
    public Set<String> keys(){
        return MANAGER.keySet();
    }

    /**清空所有容器*/
    public final void clear(){
        for(ViewModel model : MANAGER.values()){
            model.onCleared();
        }
        MANAGER.clear();
    }
}
