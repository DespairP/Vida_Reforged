package teamHTBP.vidaReforged.core.api.capability;

import teamHTBP.vidaReforged.core.api.VidaElement;

import java.util.Map;

/**
 *
 * */
public interface IVidaManaCapability {
    /**最大能储存的能量值*/
    public double maxMana();

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

    /**模拟加入能量*/
    public boolean testRemove(VidaElement element,double energy);

    /***/
    public double add(VidaElement element,double energy);
}
