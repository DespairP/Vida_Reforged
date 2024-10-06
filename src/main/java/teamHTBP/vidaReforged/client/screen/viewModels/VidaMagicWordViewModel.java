package teamHTBP.vidaReforged.client.screen.viewModels;

import net.minecraft.core.BlockPos;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.ui.component.LiveData;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModel;
import teamHTBP.vidaReforged.server.packets.MagicWordSelectPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VidaMagicWordViewModel extends ViewModel {
    public LiveData<VidaElement> selectedFilterElement = new LiveData<>(VidaElement.GOLD);

    public LiveData<Map<VidaElement, String>> selectedMagicWord = new LiveData<>(new LinkedHashMap<>());

    public LiveData<List<String>> playerMagicWords = new LiveData<>(new ArrayList<>());

    public LiveData<BlockPos> blockPos = new LiveData<BlockPos>(BlockPos.ZERO);

    public LiveData<Boolean> isCrafting = new LiveData<>(false);

    public void setSelectWord(VidaElement element,String magicWordId){
        String replacedMagicWordId = magicWordId;
        Map<VidaElement, String> map = this.selectedMagicWord.getValue();
        // toggle
        if(map.containsKey(element)){
            // 如果有且是同一个，取消选择
            String magicId = map.get(element);
            if(magicId != null && magicId.equals(magicWordId)){
                replacedMagicWordId = "";
            }
            map.replace(element,replacedMagicWordId);
            selectedMagicWord.setValue(map);
            VidaPacketManager.sendToServer(new MagicWordSelectPacket(blockPos.getValue(), selectedMagicWord.getValue()));
            return;
        }
        // 如果没有就添加
        map.put(element, replacedMagicWordId);
        VidaPacketManager.sendToServer(new MagicWordSelectPacket(blockPos.getValue(), selectedMagicWord.getValue()));
        selectedMagicWord.setValue(map);
    }


    @Override
    public void onCleared() {
        this.playerMagicWords.clearObservers();
        this.selectedMagicWord.clearObservers();
        this.isCrafting.clearObservers();
        this.blockPos.clearObservers();
        this.isCrafting.clearObservers();
    }
}
