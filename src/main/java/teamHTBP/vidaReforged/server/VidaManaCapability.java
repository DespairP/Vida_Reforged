package teamHTBP.vidaReforged.server;

import net.minecraftforge.common.util.MutableHashedLinkedMap;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;

import java.util.Map;

public class VidaManaCapability implements IVidaManaCapability {
    private final double maxMana;
    private Map<VidaElement,Double> elementMana;
    private double efficiency;

    public VidaManaCapability(double maxMana) {
        this.maxMana = maxMana;
    }

    @Override
    public double maxMana() {
        return this.maxMana;
    }

    @Override
    public Map<VidaElement, Double> getCurrentMana() {
        return null;
    }

    @Override
    public double consumeMana(VidaElement element, double energy) {
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
    public boolean testRemove(VidaElement element, double energy) {
        return false;
    }

    @Override
    public double add(VidaElement element, double energy) {
        return 0;
    }
}
