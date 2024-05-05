package teamHTBP.vidaReforged.core.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicAttribute;

import java.util.List;
import java.util.Map;

/**
 * 魔法物品系统
 * */
public interface IVidaMagicContainerCapability extends INBTSerializable<CompoundTag> {
    VidaMagicAttribute getAttribute();
    boolean setContainer(VidaMagicAttribute container);

    /**获取冷却时间*/
    public int getCurrentCoolDownTime();

    /**是否在冷却时间内*/
    boolean isInCoolDown(long currentMillSecond);

    public Result setSingleMagic(int index, ResourceLocation availableMagic);

    public ResourceLocation getCurrentMagic();

    public int getCurrentMagicIndex();

    public void setCurrentMagicIndex(int currentMagicIndex);

    public void setMagics(Map<Integer, ResourceLocation> magics);

    public List<ResourceLocation> getAvailableMagics();
}
