package teamHTBP.vidaReforged.server.items;

import net.minecraft.world.item.*;
import teamHTBP.vidaReforged.core.api.VidaElement;

public class ElementPickaxe extends PickaxeItem {
    VidaElement element = VidaElement.EMPTY;

    public ElementPickaxe(VidaElement element) {
        super(Tiers.DIAMOND, 1, -2.8F, new Item.Properties());
        this.element = element;
    }

    @Override
    public boolean useOnRelease(ItemStack itemStack) {
        return super.useOnRelease(itemStack);
    }
}
