package teamHTBP.vidaReforged.core.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

/**
 * 词条系统
 * */
public interface IVidaMagicWordCapability extends INBTSerializable<CompoundTag> {
    public List<String> getAccessibleMagicWord();

    public boolean unlockMagicWord(String magicWordId);
}
