package teamHTBP.vidaReforged.client.events.registries;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.model.blockEntities.*;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;

/**
 * 注册每种BlockEntity的BlockEntityRenderer
 * */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BlockEntityRendererRegistryHandler {
    @SubscribeEvent
    public static void onClientEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.PURIFICATION_CAULDRON.get(), PurificationCauldronBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.COLLECTOR.get(), CollectorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.TEACON_GUIDEBOOK.get(), TeaconGuideBookBlockRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.FLOATING_CRYSTAL.get(), FloatingCrystalBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.GEM_SHELF.get(), CrystalDecorationBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.INJECT_TABLE.get(), InjectTableBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.CRYSTAL_LANTERN.get(), CrystalLanternRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.GLOWING_LIGHT.get(), GlowingLightBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.VIDA_WAND_CRAFTING_TABLE.get(), VidaWandCraftingTableBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.SHERD_RESEARCH_TABLE.get(), SherdResearchEntityRenderer::new);
        event.registerBlockEntityRenderer(VidaBlockEntityLoader.ELEMENT_HARMONIZE_TABLE.get(), ElementHarmonizeTableBlockEntityRenderer::new);
    }
}
