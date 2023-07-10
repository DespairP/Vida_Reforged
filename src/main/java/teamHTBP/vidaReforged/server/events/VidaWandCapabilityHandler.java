package teamHTBP.vidaReforged.server.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.common.VidaConstant;
import teamHTBP.vidaReforged.server.capabilities.provider.VidaManaCapabilityProvider;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

/**当玩家*/
@Mod.EventBusSubscriber()
public class VidaWandCapabilityHandler {
    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<ItemStack> event) {
        if(!event.getObject().is(VidaItemLoader.VIDA_WAND.get())){
            return;
        }
        event.addCapability(new ResourceLocation(VidaReforged.MOD_ID, VidaConstant.CAP_VIDA_MANA), new VidaManaCapabilityProvider());
    }
}
