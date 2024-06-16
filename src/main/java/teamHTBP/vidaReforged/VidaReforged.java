package teamHTBP.vidaReforged;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.server.advancement.VidaAdvancementTriggers;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.blocks.VidaFluidsLoader;
import teamHTBP.vidaReforged.server.entity.VidaEntityDataSerializer;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;
import teamHTBP.vidaReforged.server.events.VidaItemGroupLoader;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.levelgen.VidaLevelGenerationLoader;
import teamHTBP.vidaReforged.server.menu.VidaMenuContainerTypeLoader;
import teamHTBP.vidaReforged.server.mobs.VidaMobsLoader;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;
import teamHTBP.vidaReforged.server.recipe.VidaRecipeLoader;
import teamHTBP.vidaReforged.server.recipe.VidaRecipeSerializerLoader;

/**
 * Vida Reforged
 * @author DespairP
 * @Date 2022/12/24
 *
 * */
@Mod("vida_reforged")
public class VidaReforged {
    public static final String MOD_ID = "vida_reforged";

    //将所有事件/注册器接入管线
    public VidaReforged() {
        // 设置配置
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, VidaConfig.CONFIG_SPEC);
        //
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        VidaBlockLoader.BLOCKS.register(bus);
        VidaFluidsLoader.FLUID_TYPE.register(bus);
        VidaFluidsLoader.FLUIDS.register(bus);
        VidaBlockEntityLoader.BLOCK_ENTITIES.register(bus);
        VidaItemLoader.ITEMS.register(bus);
        VidaEntityDataSerializer.DATA_SERIALIZER.register(bus);
        VidaEntityLoader.ENTITIES.register(bus);
        VidaMenuContainerTypeLoader.MENU_CONTAINER_TYPE.register(bus);
        VidaMobsLoader.ENTITY_TYPES.register(bus);
        VidaItemGroupLoader.CREATIVE_TAB.register(bus);
        VidaRecipeLoader.TYPES.register(bus);
        VidaRecipeSerializerLoader.SERIALIZER.register(bus);
        VidaParticleTypeLoader.PARTICLE_TYPE_REGISTER.register(bus);
        VidaLevelGenerationLoader.TRUNK_PLACER_TYPES.register(bus);
        VidaLevelGenerationLoader.DECORATOR_PLACER_TYPES.register(bus);
        //VidaCommandManager.ARGUMENT_TYPE.register(bus);
        VidaLevelGenerationLoader.STRUCTURE_TYPES.register(bus);
        VidaAdvancementTriggers.init();
        VidaPacketManager.register();
    }
}
