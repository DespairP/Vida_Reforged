package teamHTBP.vidaReforged.server.capabilities;

import com.google.gson.annotations.Expose;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.capability.Result;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicAttribute;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicAttributeType;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class VidaMagicToolCapability implements IVidaMagicContainerCapability, INBTSerializable<CompoundTag> {
    /**法器的属性*/
    private VidaMagicAttribute attribute;
    /**存储的魔法*/
    private NonNullList<ResourceLocation> magicIds;
    /***/
    private int maxMagicSize = 1;
    /**饰品*/
    private CompoundTag equipments;
    /**已经启动了多少次*/
    private long count;
    /**现在需要的冷却时间（tps）*/
    private int currentCoolDownTime = 0;
    /**上一次施法时间*/
    private long lastInvokeTime = 0;
    /**现在选择的魔法*/
    private ResourceLocation currentMagic;
    /**现在选择的魔法的下标*/
    private int currentMagicIndex = 0;
    /**现在选择的元素*/
    private VidaElement currentElement = VidaElement.EMPTY;
    /**熟练度经验值*/
    private long exp;
    /**最大熟练等级*/
    private int maxLevel;
    @Expose(deserialize = false, serialize = false)
    public final static Logger LOGGER = LogManager.getLogger();

    @Expose(deserialize = false, serialize = false)
    public static Codec<VidaMagicToolCapability> codec = RecordCodecBuilder.create(ins -> ins.group(
            VidaMagicAttribute.CODEC.fieldOf("attribute")
                    .orElseGet((Consumer<String>)(error -> LOGGER.error("VidaMagicToolCapability cannot parse the attribute, will use default attribute: {}", error)), () -> VidaMagicAttribute.empty(VidaMagicAttributeType.TOOL))
                    .forGetter(VidaMagicToolCapability::getAttribute),
            ResourceLocation.CODEC.listOf().fieldOf("magicIds").orElseGet(() -> NonNullList.withSize(1, VidaMagic.MAGIC_UNKNOWN)).forGetter(VidaMagicToolCapability::getAvailableMagics),
            Codec.INT.fieldOf("maxMagicSize").orElse(1).forGetter(VidaMagicToolCapability::getMaxMagicSize),
            CompoundTag.CODEC.fieldOf("equipments").orElse(new CompoundTag()).forGetter(VidaMagicToolCapability::getEquipments),
            Codec.LONG.fieldOf("count").orElse(0L).forGetter(VidaMagicToolCapability::getCountInfo),
            Codec.INT.fieldOf("currentCoolDownTime").orElse(0).forGetter(VidaMagicToolCapability::getCurrentCoolDownTime),
            Codec.LONG.fieldOf("lastInvokeTime").orElse(System.currentTimeMillis()).forGetter(VidaMagicToolCapability::getLastInvokeTime),
            ResourceLocation.CODEC.fieldOf("currentMagic").orElse(VidaMagic.MAGIC_UNKNOWN).forGetter(VidaMagicToolCapability::getCurrentMagicId),
            Codec.INT.fieldOf("currentMagicIndex").orElse(0).forGetter(VidaMagicToolCapability::getCurrentMagicIndex),
            VidaElement.CODEC.fieldOf("currentElement").orElse(VidaElement.EMPTY).forGetter(VidaMagicToolCapability::getCurrentElementOverride),
            Codec.LONG.fieldOf("exp").orElse(0L).forGetter(VidaMagicToolCapability::getExp),
            Codec.INT.fieldOf("maxLevel").orElse(0).forGetter(VidaMagicToolCapability::getMaxLevel)
    ).apply(ins, VidaMagicToolCapability::new));


    public VidaMagicToolCapability(){

    }

    public VidaMagicToolCapability(VidaMagicAttribute attribute, List<ResourceLocation> magicIds, int maxMagicSize, CompoundTag equipments, long count, int currentCoolDownTime, long lastInvokeTime, ResourceLocation currentMagic, int currentMagicIndex, VidaElement element, long exp, int maxLevel) {
        this.attribute = attribute;
        this.magicIds = NonNullList.withSize(maxMagicSize, VidaMagic.MAGIC_UNKNOWN);
        for(int index = 0; index < magicIds.size(); index++ ){
            if(index + 1 > magicIds.size()){
                continue;
            }
            this.magicIds.set(index, magicIds.get(index));
        }
        this.maxMagicSize = maxMagicSize;
        this.equipments = equipments;
        this.count = count;
        this.currentCoolDownTime = currentCoolDownTime;
        this.lastInvokeTime = lastInvokeTime;
        this.currentMagic = currentMagic;
        this.currentMagicIndex = currentMagicIndex;
        this.currentElement = element;
        this.exp = exp;
        this.maxLevel = maxLevel;
    }


    /**设置全量属性*/
    public void set(VidaMagicToolCapability oldCap) {
        this.attribute = oldCap.attribute;
        this.magicIds = NonNullList.withSize(maxMagicSize, VidaMagic.MAGIC_UNKNOWN);
        for(int index = 0; index < magicIds.size(); index++ ){
            if(index + 1 > magicIds.size()){
                continue;
            }
            this.magicIds.set(index, oldCap.magicIds.get(index));
        }
        this.equipments = oldCap.equipments;
        this.count = oldCap.count;
        this.currentCoolDownTime = oldCap.currentCoolDownTime;
        this.lastInvokeTime = oldCap.lastInvokeTime;
        this.currentMagic = oldCap.currentMagic;
        this.currentMagicIndex = oldCap.currentMagicIndex;
        this.currentElement = oldCap.currentElement;
        this.exp = oldCap.exp;
        this.maxLevel = oldCap.maxLevel;
    }

    /**获取法器属性*/
    @Override
    public VidaMagicAttribute getAttribute() {
        if(attribute == null){
            return VidaMagicAttribute.empty(VidaMagicAttributeType.TOOL);
        }
        return attribute;
    }

    /**获取现在可以切换的魔法*/
    public List<ResourceLocation> getAvailableMagics() {
        return magicIds == null ? NonNullList.withSize(1, VidaMagic.MAGIC_UNKNOWN) : magicIds;
    }

    /**获取当前魔法*/
    public ResourceLocation getCurrentMagicId() {
        return getCurrentMagicIndex() == -1 ? VidaMagic.MAGIC_UNKNOWN : getAvailableMagics().get(getCurrentMagicIndex());
    }

    /**切换focus的魔法下标，用于Tab键切换*/
    public void setCurrentMagicIndex(int currentMagicIndex) {
        this.currentMagicIndex = currentMagicIndex;
    }

    /**获取现在focus魔法的下标*/
    @Override
    public int getCurrentMagicIndex() {
        return currentMagicIndex;
    }

    /**获取当前元素*/
    public VidaElement getCurrentElementOverride() {
        return currentElement;
    }

    /**设置当前元素*/
    public void setCurrentElementOverride(VidaElement currentElement) {
        this.currentElement = currentElement;
    }

    /**设置切换的魔法*/
    public Result setSingleMagic(int index, ResourceLocation availableMagic) {
        if(index + 1 > maxMagicSize){
            return Result.FAILED;
        }
        this.magicIds.set(index, availableMagic);
        return Result.SUCCESS;
    }

    /**map覆盖切换魔法*/
    public void setMagics(Map<Integer, ResourceLocation> magics){
        this.magicIds.clear();
        magics.forEach((index, magicId) -> this.magicIds.set((int)index, magicId));
    }

    /**list覆盖切换魔法*/
    public void setMagics(List<ResourceLocation> magicIds) {
        this.magicIds.clear();
        this.magicIds.addAll(magicIds);
    }

    /**设置当前选中魔法*/
    public void setCurrentMagic(ResourceLocation currentMagic) {
        this.currentMagic = currentMagic;
    }


    /**获取至今被触发了多少次*/
    public long getCountInfo() {
        return count;
    }

    /**获取现在的冷却需求时间*/
    public int getCurrentCoolDownTime() {
        return currentCoolDownTime;
    }

    @Override
    public boolean isInCoolDown(long currentMillSecond) {
        return getRemainCoolDown(currentMillSecond) > 0;
    }

    /**设置新的冷却需求时间*/
    public void setNewCoolDownTime(long currentTime, int currentCoolDownTime) {
        this.currentCoolDownTime = currentCoolDownTime;
        this.lastInvokeTime = currentTime;
    }


    /**获取剩下的冷却时间*/
    public long getRemainCoolDown(long currentMillSecond){
        // TODO:时区
        return currentMillSecond - (this.lastInvokeTime + this.currentCoolDownTime) > 0 ? System.currentTimeMillis() - this.lastInvokeTime : 0;
    }

    public long getLastInvokeTime() {
        return lastInvokeTime;
    }

    public void setLastInvokeTime(long lastInvokeTime) {
        this.lastInvokeTime = lastInvokeTime;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    /**获取配饰*/
    public CompoundTag getEquipments() {
        return equipments;
    }

    public int getMaxMagicSize() {
        return maxMagicSize;
    }

    public void setMaxMagicSize(int maxMagicSize) {
        this.maxMagicSize = maxMagicSize;
    }

    @Override
    public boolean setAttribute(VidaMagicAttribute attribute) {
        this.attribute = attribute;
        return true;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        try {
            tag = (CompoundTag) codec.encode(this, NbtOps.INSTANCE, tag).result().get();
        } catch (Exception ex){
            LOGGER.error(ex);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        try {
            VidaMagicToolCapability cap = codec.parse(NbtOps.INSTANCE, nbt).get().orThrow();
            this.set(cap);
        } catch (Exception ex){
            LOGGER.error(ex);
        }
    }

}
