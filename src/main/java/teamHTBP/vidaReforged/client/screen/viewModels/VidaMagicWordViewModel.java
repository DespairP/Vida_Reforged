package teamHTBP.vidaReforged.client.screen.viewModels;

import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.component.LiveData;
import teamHTBP.vidaReforged.core.common.component.ViewModel;

import java.util.LinkedHashMap;
import java.util.Map;

public class VidaMagicWordViewModel extends ViewModel {
    public LiveData<VidaElement> selectedFilterElement = new LiveData<>(VidaElement.GOLD);

    public LiveData<Map<VidaElement, String>> selectedMagicWord = new LiveData<>(new LinkedHashMap<>());

    public void setSelectWord(VidaElement element,String magicWordId){
        String replacedMagicWordId = magicWordId;
        Map<VidaElement, String> map = this.selectedMagicWord.getValue();
        // toggle
        if(map.containsKey(element)){
            // 如果有且是同一个，取消选择
            String magicId = map.get(element);
            if(magicId != null && magicId.equals(magicWordId)){
                replacedMagicWordId = null;
            }
            map.replace(element,replacedMagicWordId);
            selectedMagicWord.setValue(map);
            return;
        }
        // 如果没有就添加
        map.put(element, replacedMagicWordId);
        selectedMagicWord.setValue(map);
    }
}
