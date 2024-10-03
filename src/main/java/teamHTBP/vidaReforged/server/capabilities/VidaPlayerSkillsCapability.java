package teamHTBP.vidaReforged.server.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import teamHTBP.vidaReforged.core.api.capability.IVidaCodecCapabilitySerializer;
import teamHTBP.vidaReforged.core.api.capability.IVidaPlayerRPGSkillCapability;

/** 玩家RPG数值系统 */
public class VidaPlayerSkillsCapability implements IVidaPlayerRPGSkillCapability, IVidaCodecCapabilitySerializer<VidaPlayerSkillsCapability> {
    public static final Codec<VidaPlayerSkillsCapability> CODEC = RecordCodecBuilder.create(inst -> inst.group(Codec.INT.fieldOf("eruditionKnowledge").forGetter(VidaPlayerSkillsCapability::getEruditionKnowledge)).apply(inst, VidaPlayerSkillsCapability::new));
    /**学识点*/
    private int eruditionKnowledge = 0;


    public VidaPlayerSkillsCapability(int eruditionKnowledge) {
        this.eruditionKnowledge = eruditionKnowledge;
    }

    /**增加学识点*/
    public int addEruditionKnowledge(int knowledge){
        eruditionKnowledge += knowledge;
        return eruditionKnowledge;
    }

    /**获取学识点*/
    public int getEruditionKnowledge(){
        return eruditionKnowledge;
    }

    /**设置*/
    public void setEruditionKnowledge(int knowledge){
        eruditionKnowledge = knowledge;
    }

    @Override
    public Codec<VidaPlayerSkillsCapability> getCodec() {
        return CODEC;
    }

    @Override
    public void copyFrom(VidaPlayerSkillsCapability record) {
        this.eruditionKnowledge = record.eruditionKnowledge;
    }
}
