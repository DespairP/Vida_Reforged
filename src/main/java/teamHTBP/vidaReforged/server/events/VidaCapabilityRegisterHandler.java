package teamHTBP.vidaReforged.server.events;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.server.providers.MagicTemplateManager;

/**
 * 管理注册Capability的Event
 * */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VidaCapabilityRegisterHandler {
    public static Capability<IVidaManaCapability> VIDA_MANA = CapabilityManager.get(new CapabilityToken<>(){});;

    /**注册Capability*/
    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IVidaManaCapability.class);
    }
}
