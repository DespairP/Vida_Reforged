package teamHTBP.vidaReforged.client.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.model.blockEntities.CollectorBlockEntityRenderer;
import teamHTBP.vidaReforged.client.model.blockEntities.PurificationCauldronBlockEntityRenderer;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BlockRendererHandler {
    @SubscribeEvent
    public static void onClientEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.PURIFICATION_CAULDRON.get(), PurificationCauldronBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.COLLECTOR.get(), CollectorBlockEntityRenderer::new);
    }
}
