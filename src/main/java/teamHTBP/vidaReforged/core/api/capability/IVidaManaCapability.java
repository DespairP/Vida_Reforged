package teamHTBP.vidaReforged.core.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;
import teamHTBP.vidaReforged.core.api.VidaElement;

import java.util.Map;

/**
 *
 * */
public interface IVidaManaCapability extends INBTSerializable<CompoundTag> {
    /**最大能储存的能量值*/
    public double maxMana();

    /**设置最大能量*/
    public double setMaxMana(double maxMana);

    /**获取每种能量的能量值*/
    public Map<VidaElement,Double> getCurrentMana();

    /**消耗能量*/
    public double consumeMana(VidaElement element,double energy);

    /**是否能够消耗能量*/
    public boolean canConsumeMana(VidaElement element,double energy);

    /**获取施法效率*/
    public double getConsumeEfficiency(VidaElement element);

    /**某种元素是否为空*/
    public boolean isElementEmpty(VidaElement element);

    /**能量是否都为空*/
    public boolean isEmpty();

    /**测试是否能消耗能量*/
    public boolean testConsume(VidaElement element,double energy);

    /***/
    public double addMana(VidaElement element,double energy);

    /***/
    public double setMana(VidaElement element,double energy);

    /**获取元素能量总数*/
    public double getAllMana();
}
