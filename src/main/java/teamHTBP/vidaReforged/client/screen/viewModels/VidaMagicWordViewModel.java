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
        Map<VidaElement, String> map = this.selectedMagicWord.getValue();
        if(map.containsKey(element)){
            map.replace(element,magicWordId);
            selectedMagicWord.setValue(map);
            return;
        }
        map.put(element,magicWordId);
        selectedMagicWord.setValue(map);
    }
}
