package teamHTBP.vidaReforged.client.screen.viewModels;

import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.ui.component.LiveData;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModel;

import java.util.*;

public class VidaViewMagicWordViewModel extends ViewModel {
    public LiveData<String> selectedMagicWord = new LiveData<>("");

    public LiveData<List<String>> playerMagicWords = new LiveData<>(new ArrayList<>());

    public void setSelectWord(String magicWordId){
        String replacedMagicWordId = magicWordId;
        // 如果没有就添加
        selectedMagicWord.setValue(replacedMagicWordId);
    }


}
