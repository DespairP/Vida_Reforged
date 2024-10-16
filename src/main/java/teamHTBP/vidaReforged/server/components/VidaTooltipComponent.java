package teamHTBP.vidaReforged.server.components;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class VidaTooltipComponent implements TooltipComponent {
    private final ItemStack item;

    public VidaTooltipComponent(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem(){
        return item;
    }
}
