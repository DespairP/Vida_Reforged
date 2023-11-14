package teamHTBP.vidaReforged.client.screen.viewModels;

import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.core.common.ui.component.LiveData;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModel;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandEquipmentSlot;

import java.util.HashMap;
import java.util.Map;

public class VidaWandCraftingViewModel extends ViewModel {
    public LiveData<Map<Position, VidaWandEquipmentSlot>> slots = new LiveData<>();
    public LiveData<Boolean> needUpdate = new LiveData<>(false);

    public void setSlots(Map<Position, VidaWandEquipmentSlot> slots){
        this.slots.setValue(slots);
    }

    public VidaWandEquipmentSlot getSlot(Position position){
        return this.slots.getValue().get(position);
    }

    public void setUpdate(){
        needUpdate.setValue(true);
        needUpdate.setValueWithoutNotify(false);
    }
}
