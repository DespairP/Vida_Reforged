package teamHTBP.vidaReforged.core.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IVidaPlayerRPGSkillCapability extends INBTSerializable<CompoundTag> {

    public int getEruditionKnowledge();

    public int addEruditionKnowledge(int knowledge);

    public void setEruditionKnowledge(int knowledge);
}
