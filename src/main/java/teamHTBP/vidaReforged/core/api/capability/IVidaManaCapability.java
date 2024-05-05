package teamHTBP.vidaReforged.core.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import teamHTBP.vidaReforged.core.api.VidaElement;

import java.util.Map;

/**
 * 魔力系统
 * */
public interface IVidaManaCapability extends INBTSerializable<CompoundTag> {
    /**最大能储存的能量值*/
    public double getMaxMana();

    /**设置最大能量*/
    public double resetMaxMana(double maxMana);

    /**获取每种能量的能量值*/
    public Map<VidaElement,Double> getAllElementsMana();

    public double getManaByElement(VidaElement element);

    /**消耗能量*/
    public Result consumeMana(VidaElement element, double energy);

    /**是否能够消耗能量*/
    public boolean testConsume(VidaElement element,double energy);

    /**获取施法效率*/
    public double getConsumeEfficiency(VidaElement element);

    /**检查某种元素是否为空*/
    public boolean isElementEmpty(VidaElement element);

    /**检查能量是否都为空*/
    public boolean isEmpty();

    public double canConsume(VidaElement element,double energy);

    /**测试是否能消耗能量*/
    double testConsumeAndGetRemain(VidaElement element, double energy);

    /**添加某种元素的能量*/
    public VidaCapabilityResult addMana(VidaElement element, double energy);

    /**指定某种元素的能量数值*/
    public VidaCapabilityResult<Double> setMana(VidaElement element, double energy);

    /**获取元素能量总数*/
    public double getSumElementMana();

    /**检查是否法器只能容纳有限个元素*/
    public boolean isEnableLimitedElement();

    /**如果有限制，则获取限制元素的数量*/
    public int getLimitElementAmount();
}
