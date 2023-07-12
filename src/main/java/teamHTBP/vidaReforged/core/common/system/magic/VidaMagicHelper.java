package teamHTBP.vidaReforged.core.common.system.magic;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class VidaMagicHelper {

    public static VidaElement getCurrentMagicElement(ItemStack wandStack){
        return VidaElement.EMPTY;
    }

    public static List<VidaElement> getCurrentMagicElements(ItemStack wandStack){
        return ImmutableList.of(VidaElement.EMPTY);
    }

    public static VidaMagicContainer getCurrentMagic(ItemStack wandStack){
        if(wandStack == null || wandStack.isEmpty()){
            return null;
        }
        LazyOptional<IVidaMagicContainerCapability> containerCap = wandStack.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
        AtomicReference<VidaMagicContainer> container = new AtomicReference<>(null);
        containerCap.ifPresent((capability) -> container.set(capability.getContainer()));
        return container.get();
    }
}
