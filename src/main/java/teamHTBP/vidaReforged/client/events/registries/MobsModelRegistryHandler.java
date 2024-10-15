package teamHTBP.vidaReforged.client.events.registries;

import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.world.entity.monster.Spider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.model.mobs.ElementalMobsRenderer;
import teamHTBP.vidaReforged.client.model.mobs.LightSheepRenderer;
import teamHTBP.vidaReforged.client.model.mobs.OrangeSpottedSparrowsRenderer;
import teamHTBP.vidaReforged.server.mobs.VidaElementSpider;
import teamHTBP.vidaReforged.server.mobs.VidaMobsLoader;
import teamHTBP.vidaReforged.client.model.mobs.AncientBelieverRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MobsModelRegistryHandler {
    @SubscribeEvent
    public static void onClientSetUpEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(VidaMobsLoader.ANCIENT_BELIEVER.get(), AncientBelieverRenderer::new);
        event.registerEntityRenderer(VidaMobsLoader.LIGHT_SHEEP.get(), LightSheepRenderer::new);
        event.registerEntityRenderer(VidaMobsLoader.ORANGE_SPOTTED_SPARROW.get(), OrangeSpottedSparrowsRenderer::new);
        event.registerEntityRenderer(VidaMobsLoader.ELEMENTAL_SPIDER.get(), (context) -> new ElementalMobsRenderer<VidaElementSpider, Spider, SpiderModel<Spider>>(context, new ElementalMobsRenderer.ElementSpiderRenderer(context)));
    }
}
