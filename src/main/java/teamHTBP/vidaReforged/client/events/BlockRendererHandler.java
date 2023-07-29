package teamHTBP.vidaReforged.client.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.model.blockEntities.*;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BlockRendererHandler {
    @SubscribeEvent
    public static void onClientEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.PURIFICATION_CAULDRON.get(), PurificationCauldronBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.COLLECTOR.get(), CollectorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.TEACON_GUIDEBOOK.get(), TeaconGuideBookBlockRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.FLOATING_CRYSTAL.get(), FloatingCrystalBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.GEM_SHELF.get(), CrystalDecorationBlockEntityRenderer::new);

    }
}
