package teamHTBP.vidaReforged.server.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.api.capability.Result;
import teamHTBP.vidaReforged.core.api.capability.VidaCapabilityResult;
import teamHTBP.vidaReforged.helper.VidaElementHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static teamHTBP.vidaReforged.core.api.capability.Result.*;

/**带有*/
public class VidaManaCapability implements IVidaManaCapability, INBTSerializable<CompoundTag> {
    /**法器最大能承受的容量*/
    private double maxMana = 0.0;
    /**元素：能量值*/
    private Map<VidaElement,Double> mana = new ConcurrentHashMap<>();
    /**元素使用效能*/
    @Deprecated
    private double efficiency = 0.0;
    /**元素数量是否被限制*/
    private boolean isLimited = false;
    private int limitAmount = 1;
    /**限制可以加入的元素*/
    private List<VidaElement> strictElements;
    private static final Logger LOGGER = LogManager.getLogger();

    /** Codec*/
    public Codec<VidaManaCapability> codec = RecordCodecBuilder.create(ins -> ins.group(
            Codec.DOUBLE.fieldOf("maxMana").orElse(1.0).forGetter(VidaManaCapability::getMaxMana),
            Codec.BOOL.fieldOf("isLimited").orElse(false).forGetter(VidaManaCapability::isEnableLimitedElement),
            Codec.unboundedMap(VidaElement.CODEC, Codec.DOUBLE).orElseGet(() -> {HashMap<VidaElement, Double> map =new HashMap<>(); map.putAll(Arrays.stream(VidaElement.values()).collect(Collectors.toMap(key -> key, key -> 0.0))); return map;}).fieldOf("mana").forGetter(VidaManaCapability::getAllElementsMana),
            Codec.INT.fieldOf("limitAmount").orElse(1).forGetter(VidaManaCapability::getLimitElementAmount),
            VidaElement.CODEC.listOf().fieldOf("strictElements").orElse(VidaElementHelper.getNormalElements()).forGetter(VidaManaCapability::getStrictElements)
    ).apply(ins, VidaManaCapability::new));


    public VidaManaCapability(double maxMana, boolean isLimited, Map<VidaElement, Double> mana, int limitAmount, List<VidaElement> strictElements) {
        this();
        this.maxMana = maxMana;
        this.mana.putAll(mana);
        this.isLimited = isLimited;
        this.limitAmount = limitAmount;
        this.strictElements = strictElements;
    }

    public VidaManaCapability(){
        this.mana.putAll(Arrays.stream(VidaElement.values()).collect(Collectors.toMap(key -> key, key -> 0.0)));
    }

    public VidaManaCapability(double maxMana, boolean isLimited, List<VidaElement> strictedElements) {
        this.maxMana = maxMana;
        this.isLimited = isLimited;
        this.strictElements = strictedElements;
    }

    @Override
    public double getMaxMana() {
        return this.maxMana;
    }

    @Override
    public double resetMaxMana(double maxMana) {
        this.mana = recalculateManaAndSet(maxMana);
        return this.maxMana = maxMana;
    }

    /**当新的容量被设置，重新计算每个元素的比例*/
    protected Map<VidaElement, Double> recalculateManaAndSet(double newMaxMana){
        // 计算现在每个元素的比例
        HashMap<VidaElement, Double> percentage = new HashMap<>();
        for(VidaElement element : this.mana.keySet()){
            if(maxMana == 0){
                percentage.put(element, 0.0);
                continue;
            }

            percentage.put(element, mana.get(element) / maxMana);
        }

        // 根据比例重新设置值
        for(VidaElement element : percentage.keySet()){
            double oldManaAmount = mana.get(element);
            this.mana.put(element, Math.floor(percentage.getOrDefault(element, 0.0) * oldManaAmount));
        }

        return mana;
    }

    @Override
    public Map<VidaElement, Double> getAllElementsMana() {
        return this.mana;
    }

    @Override
    public double getManaByElement(VidaElement element){
        return mana.getOrDefault(element, 0.0);
    }

    @Override
    public Result consumeMana(VidaElement element, double energy) {
        final double currentElementManaAmount = getManaByElement(element);
        // 如果消耗值小于0，或者不足够消费时，返回FAILED
        if(energy < 0 || testConsume(element, energy)){
            return FAILED;
        }
        // 如果足够，消耗返回SUCCESS
        mana.replace(element, currentElementManaAmount - energy);
        return SUCCESS;
    }

    @Override
    public boolean testConsume(VidaElement element, double energy) {
        return testConsumeAndGetRemain(element, energy) >= 0;
    }

    @Override
    public double testConsumeAndGetRemain(VidaElement element, double energy) {
        return mana.getOrDefault(element,0.0) - energy ;
    }

    @Override
    public double canConsume(VidaElement element, double energy) {
        return testConsumeAndGetRemain(element, energy);
    }

    @Deprecated
    @Override
    public double getConsumeEfficiency(VidaElement element) {
        return efficiency;
    }

    @Override
    public boolean isElementEmpty(VidaElement element) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public VidaCapabilityResult<Double> addMana(VidaElement element, double energy) {
        final double currentElementManaAmount = getManaByElement(element);
        return setMana(element, currentElementManaAmount + energy);
    }

    /**
     * 设置某个元素的能量
     * @return 加入了对应元素的能量以后，结余的能量还剩多少（如果全部被加入了，结果为0）
     * */
    @Override
    public VidaCapabilityResult<Double> setMana(VidaElement element, double energy) {
        final int usedElementCount = getElementsCountCurrentInUse();
        final boolean isNewElement = Math.floor(mana.getOrDefault(element, 0.0)) <= 0.0;
        final double remainSumManaAmount = this.maxMana - getSumElementMana();
        final double currentElementManaAmount = getManaByElement(element);

        // 如果不在可以接受的元素中，则不在存储
        if(!getStrictElements().contains(element)){
            return new VidaCapabilityResult<>(FAILED, 0.0);
        }

        // 如果已经超过了可以存储的元素个数，则不再存储
        if(isEnableLimitedElement() && (isNewElement && usedElementCount + 1 > getLimitElementAmount())) {
            return new VidaCapabilityResult<>(FAILED, 0.0);
        }

        if(remainSumManaAmount <= 0.0){
            return new VidaCapabilityResult<>(PASS, energy);
        }

        // 取较小的一方，但是必须大于0
        double setEnergy = Math.max( Math.min(remainSumManaAmount + currentElementManaAmount, energy), 0.0);
        mana.put(element, setEnergy);

        return new VidaCapabilityResult<>(SUCCESS, energy - setEnergy);
    }


    protected int getElementsCountCurrentInUse(){
        return (int) this.mana.values().stream().filter(value -> Math.floor(value) > 0.0).count();
    }

    /**获取元素占用容器的大小*/
    @Override
    public double getSumElementMana(){
        double remainingMana = 0;
        for(VidaElement element : mana.keySet()){
            remainingMana += getManaByElement(element);
        }
        return remainingMana;
    }

    @Override
    public boolean isEnableLimitedElement() {
        return isLimited;
    }

    @Override
    public int getLimitElementAmount() {
        return limitAmount;
    }

    @Override
    public void setStrictElements(List<VidaElement> elements) {
        this.strictElements = new ArrayList<>(elements);
    }

    public List<VidaElement> getStrictElements() {
        return strictElements;
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
            VidaManaCapability cap = codec.parse(NbtOps.INSTANCE, nbt).get().orThrow();
            this.mana = cap.mana;
            this.maxMana = cap.maxMana;
            this.isLimited = cap.isLimited;
            this.limitAmount = cap.limitAmount;
            this.efficiency = cap.efficiency;
            this.strictElements = cap.strictElements;
        } catch (Exception ex){
            LOGGER.error(ex);
        }
    }



}
