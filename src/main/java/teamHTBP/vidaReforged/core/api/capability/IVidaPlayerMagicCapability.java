package teamHTBP.vidaReforged.core.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Set;

public interface IVidaPlayerMagicCapability extends INBTSerializable<CompoundTag> {
    /**获取现在持有的魔法*/
    public Set<ResourceLocation> getAvailableMagic();

    /**添加魔法*/
    public Result addMagic(ResourceLocation magicId);
}
