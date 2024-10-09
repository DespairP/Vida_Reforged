package teamHTBP.vidaReforged.client.events.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.model.entity.FloatingItemEntityRenderer;
import teamHTBP.vidaReforged.client.model.entity.GlowLightEntityRenderer;
import teamHTBP.vidaReforged.client.model.entity.LazerEntityRenderer;
import teamHTBP.vidaReforged.client.model.entity.SparkEntityRenderer;
import teamHTBP.vidaReforged.client.model.entity.projectile.PartyParrotProjectileRenderer;
import teamHTBP.vidaReforged.client.model.entity.projectile.MagicParticleProjectileRenderer;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;
import teamHTBP.vidaReforged.server.mobs.VidaMobsLoader;

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
        event.registerEntityRenderer(VidaMobsLoader.GLOW_LIGHT.get(), (dispatch) -> new GlowLightEntityRenderer(dispatch, new TextureSection(new ResourceLocation(VidaReforged.MOD_ID, "textures/particle/tinkle.png"), 0, 0, 256, 256, 256, 256)));;
        event.registerEntityRenderer(VidaEntityLoader.FLOATING_ITEM_ENTITY.get(), FloatingItemEntityRenderer::new);
        event.registerEntityRenderer(VidaEntityLoader.FAKE_HARMONIZE_TABLE_ITEM_ENTITY.get(), FloatingItemEntityRenderer::new);
    }
}
