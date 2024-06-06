package teamHTBP.vidaReforged.core.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicAttribute;

import java.util.List;
import java.util.Map;

/**
 * 魔法物品系统
 * */
public interface IVidaMagicContainerCapability extends INBTSerializable<CompoundTag> {
    /**获取基本参数*/
    VidaMagicAttribute getAttribute();

    /**设置基础参数*/
    boolean setAttribute(VidaMagicAttribute container);

    /**获取冷却时间*/
    public int getCurrentCoolDownTime();

    /**是否在冷却时间内*/
    public boolean isInCoolDown(long currentMillSecond);

    /**设置对应下标的法术*/
    public Result setSingleMagic(int index, ResourceLocation availableMagic);

    /**获取当前法术Id*/
    default ResourceLocation getCurrentMagic(){
        return getCurrentMagicIndex() < 0 ? VidaMagic.MAGIC_UNKNOWN : getAvailableMagics().get(getCurrentMagicIndex());
    }

    /**获取当前法杖元素*/
    public VidaElement getCurrentElement();

    /**设置法杖元素*/
    public void setCurrentElement(VidaElement currentElement);

    /**获取当前法术下标*/
    public int getCurrentMagicIndex();

    /**切换当前法术，用于Tab键切换*/
    public void setCurrentMagicIndex(int currentMagicIndex);

    /**覆盖当前魔法，用于设置法术*/
    public void setMagics(Map<Integer, ResourceLocation> magics);

    /**获取现在法杖设置的所有法术*/
    public List<ResourceLocation> getAvailableMagics();
}
