package teamHTBP.vidaReforged.client.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.server.mobs.VidaMobsLoader;
import teamHTBP.vidaReforged.client.model.mobs.AncientBelieverRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MobsRegModelHandler {
    @SubscribeEvent
    public static void onClientSetUpEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(VidaMobsLoader.ANCIENT_BELIEVER.get(), AncientBelieverRenderer::new);
    }
}
