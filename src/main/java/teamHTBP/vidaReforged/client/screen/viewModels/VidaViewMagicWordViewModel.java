package teamHTBP.vidaReforged.client.screen.viewModels;

import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.component.LiveData;
import teamHTBP.vidaReforged.server.packets.MagicWordPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;

import java.util.*;

public class VidaViewMagicWordViewModel {
    public LiveData<String> selectedMagicWord = new LiveData<>("");

    public LiveData<List<String>> playerMagicWords = new LiveData<>(new ArrayList<>());

    public void setSelectWord(VidaElement element,String magicWordId){
        String replacedMagicWordId = magicWordId;
        // 如果没有就添加
        selectedMagicWord.setValue(replacedMagicWordId);
    }
}
