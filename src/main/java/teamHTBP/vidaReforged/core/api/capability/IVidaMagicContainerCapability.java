package teamHTBP.vidaReforged.core.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicContainer;

import java.util.Optional;

/**
 * 魔法物品系统
 * */
public interface IVidaMagicContainerCapability extends INBTSerializable<CompoundTag> {
    VidaMagicContainer getContainer();
    boolean setContainer(VidaMagicContainer container);

    /**获取冷却时间*/
    default long getCoolDown(){
        VidaMagicContainer container = getContainer();
        return container.coolDown();
    }

    /**是否在冷却时间内*/
    default boolean isInCoolDown(long currentMillSecond){
        VidaMagicContainer container = getContainer();
        long millSecondBefore = currentMillSecond - container.lastInvokeMillSec() - getCoolDown();
        return !(millSecondBefore >= 0);
    }
}
