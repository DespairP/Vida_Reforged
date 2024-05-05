package teamHTBP.vidaReforged.client.model.entity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.model.entity.projectile.PartyParrotProjectileRenderer;
import teamHTBP.vidaReforged.client.model.entity.projectile.MagicParticleProjectileRenderer;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;

/**
 * @author TT432
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD)
public class VidaEntityRendererLoader {
    @SubscribeEvent
    public static void onEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(VidaEntityLoader.MAGIC_PARTICLE_PROJECTILE.get(), MagicParticleProjectileRenderer::new);
        event.registerEntityRenderer(VidaEntityLoader.PARTY_PARROT.get(), PartyParrotProjectileRenderer::new);
        event.registerEntityRenderer(VidaEntityLoader.SPARK.get(), SparkEntityRenderer::new);
        event.registerEntityRenderer(VidaEntityLoader.TRAIL.get(), LazerEntityRenderer::new);
    }
}
