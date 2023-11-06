package teamHTBP.vidaReforged.client.screen.viewModels;

import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.core.common.ui.component.LiveData;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModel;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandEquipmentSlot;

import java.util.HashMap;
import java.util.Map;

public class VidaWandCraftingViewModel extends ViewModel {
    LiveData<Map<Position, VidaWandEquipmentSlot>> slots = new LiveData<>();


}
