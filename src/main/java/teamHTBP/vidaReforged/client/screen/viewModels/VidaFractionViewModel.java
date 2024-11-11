package teamHTBP.vidaReforged.client.screen.viewModels;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import teamHTBP.vidaReforged.core.common.ui.component.LiveData;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModel;

public class VidaFractionViewModel extends ViewModel {
    /**交互的生物*/
    public LiveData<LivingEntity> entity;
    /**阵营数值*/
    public LiveData<?> fractionData;
    /***/
    public LiveData<Boolean> isInventoryOpen = new LiveData<>(false);
}
