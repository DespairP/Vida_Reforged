package teamHTBP.vidaReforged.server.components;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import teamHTBP.vidaReforged.core.api.VidaElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VidaWandTooltipComponent implements TooltipComponent {
    private List<String> magics;

    private Map<VidaElement,Double> mana;

    private double maxMana = 1;

    public VidaWandTooltipComponent() {
        this.magics = new ArrayList<>();
        this.mana = new HashMap<>();
    }

    public List<String> getMagics() {
        return magics;
    }

    public void setMagics(List<String> magics) {
        this.magics = magics;
    }

    public void setMana(Map<VidaElement, Double> mana) {
        this.mana = mana;
    }

    public Map<VidaElement, Double> getMana() {
        return mana;
    }

    public void setMaxMana(double maxMana) {
        this.maxMana = maxMana == 0 ? 1 : maxMana;
    }

    public double getMaxMana() {
        return maxMana;
    }
}
