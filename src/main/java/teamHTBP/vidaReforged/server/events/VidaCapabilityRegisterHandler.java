package teamHTBP.vidaReforged.server.events;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.core.api.capability.*;

/**
 * 管理注册Capability的Event
 * */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VidaCapabilityRegisterHandler {
    public static Capability<IVidaManaCapability> VIDA_MANA = CapabilityManager.get(new CapabilityToken<>(){});
    public static Capability<IVidaMagicContainerCapability> VIDA_MAGIC_CONTAINER = CapabilityManager.get(new CapabilityToken<>(){});
    public static Capability<IVidaMagicWordCapability> VIDA_MAGIC_WORD = CapabilityManager.get(new CapabilityToken<>(){});
    public static Capability<IVidaMultiBlockCapability> VIDA_MULTI_BLOCK = CapabilityManager.get(new CapabilityToken<>(){});
    public static Capability<IVidaPlayerMagicCapability> VIDA_PLAYER_MAGIC = CapabilityManager.get(new CapabilityToken<>(){});
    public static Capability<IVidaChunkCrystalCapability> VIDA_CHUNK_CRYSTAL = CapabilityManager.get(new CapabilityToken<>(){});
    public static Capability<IVidaPlayerRPGSkillCapability> VIDA_RPG_SKILL = CapabilityManager.get(new CapabilityToken<>(){});


    /**注册Capability*/
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IVidaManaCapability.class);
        event.register(IVidaMagicContainerCapability.class);
        event.register(IVidaMagicWordCapability.class);
        event.register(IVidaMultiBlockCapability.class);
        event.register(IVidaPlayerMagicCapability.class);
        event.register(IVidaChunkCrystalCapability.class);
        event.register(IVidaPlayerRPGSkillCapability.class);
    }
}
