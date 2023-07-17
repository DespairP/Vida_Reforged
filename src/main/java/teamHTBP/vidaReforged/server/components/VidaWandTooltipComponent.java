package teamHTBP.vidaReforged.server.components;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

public class VidaWandTooltipComponent implements TooltipComponent {
    private final List<String> magics;

    public VidaWandTooltipComponent(List<String> magics) {
        this.magics = magics;
    }

    public List<String> getMagics() {
        return magics;
    }
}
