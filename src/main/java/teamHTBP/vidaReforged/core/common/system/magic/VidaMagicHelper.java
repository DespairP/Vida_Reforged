package teamHTBP.vidaReforged.core.common.system.magic;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.core.api.VidaElement;

import java.util.List;

public class VidaMagicHelper {

    public static VidaElement getCurrentMagicElement(ItemStack wandStack){
        return VidaElement.EMPTY;
    }

    public static List<VidaElement> getCurrentMagicElements(ItemStack wandStack){
        return ImmutableList.of(VidaElement.EMPTY);
    }

    public static VidaMagicContainer getCurrentMagic(ItemStack wandStack){
        return new VidaMagicContainer().damage(1).coolDown(120).amount(1).invokeCount(1).costMana(10);
    }
}
