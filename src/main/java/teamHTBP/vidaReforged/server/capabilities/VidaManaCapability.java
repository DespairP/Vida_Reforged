package teamHTBP.vidaReforged.server.capabilities;

import net.minecraft.nbt.CompoundTag;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;

import java.util.LinkedHashMap;
import java.util.Map;

public class VidaManaCapability implements IVidaManaCapability {
    private double maxMana;
    private Map<VidaElement,Double> elementMana = new LinkedHashMap<>();
    private double efficiency;

    public VidaManaCapability(double maxMana) {
        this.maxMana = maxMana;
    }

    @Override
    public double maxMana() {
        return this.maxMana;
    }

    @Override
    public double setMaxMana(double maxMana) {
        return this.maxMana = maxMana;
    }

    @Override
    public Map<VidaElement, Double> getCurrentMana() {
        return this.elementMana;
    }

    public double getMana(VidaElement element){
        return elementMana.getOrDefault(element, 0.0);
    }

    @Override
    public double consumeMana(VidaElement element, double energy) {
        elementMana.replace(element, elementMana.get(element) - energy);
        return 0;
    }

    @Override
    public boolean canConsumeMana(VidaElement element, double energy) {
        return false;
    }

    @Override
    public double getConsumeEfficiency(VidaElement element) {
        return 0;
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
    public boolean testConsume(VidaElement element, double energy) {
        return elementMana.getOrDefault(element,0.0) > energy ;
    }

    @Override
    public double addMana(VidaElement element, double energy) {
        final double currentElementManaAmount = getMana(element);

        // 取较小的一方，但是必须大于0
        double setEnergy = setMana(element, currentElementManaAmount + energy);

        return energy >= 0 ? energy - setEnergy : 0;
    }

    /**
     * 设置某个元素的能量
     * @return 加入了对应元素的能量以后，结余的能量还剩多少（如果全部被加入了，结果为0）
     * */
    @Override
    public double setMana(VidaElement element, double energy) {
        final double emptyManaAmount = this.maxMana - getAllMana();
        final double currentElementManaAmount = getMana(element);

        // 取较小的一方，但是必须大于0
        double setEnergy = Math.max( Math.min(emptyManaAmount + currentElementManaAmount, energy), 0.0);
        elementMana.put(element, setEnergy);

        return energy >= 0 ? energy - setEnergy : 0;
    }

    /**获取元素占用容器的大小*/
    public double getAllMana(){
        double remainingMana = 0;
        for(VidaElement element : elementMana.keySet()){
            remainingMana += getMana(element);
        }
        return remainingMana;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("maxMana", maxMana);
        tag.putDouble("efficiency", efficiency);

        if(this.elementMana == null){
            return tag;
        }
        for(VidaElement element : elementMana.keySet()){
            tag.putDouble(
                    String.format("%s:%s", "mana", element.getElementName()),
                    elementMana.get(element)
            );
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.maxMana = nbt.getDouble("maxMana");
        this.elementMana = new LinkedHashMap<>();
        for(VidaElement element: VidaElement.values()){
            String key = String.format("%s:%s", "mana", element.getElementName());
            if(!nbt.contains(key)){
                continue;
            }
            elementMana.put(element, nbt.getDouble(key));
        }
        this. efficiency = nbt.getDouble("efficiency");
    }
}
