package teamHTBP.vidaReforged.server.mobs;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;

import static teamHTBP.vidaReforged.core.common.VidaConstant.*;
public class VidaMobsLoader {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VidaReforged.MOD_ID);
    public static final RegistryObject<EntityType<AncientBeliever>> ANCIENT_BELIEVER =
            ENTITY_TYPES.register(ANCIENT_BELIEVER_NAME, () -> EntityType.Builder.of(AncientBeliever::new, MobCategory.MONSTER)
                    .sized(1, 2)
                    .build(ANCIENT_BELIEVER_NAME));

    public static final RegistryObject<EntityType<GlowLight>> GLOW_LIGHT =
            ENTITY_TYPES.register("glow_light", () -> EntityType.Builder.of(GlowLight::new, MobCategory.AMBIENT)
                    .sized(0.5F, 0.5F)
                    .build("glow_light"));

    public static final RegistryObject<EntityType<LightSheep>> LIGHT_SHEEP =
            ENTITY_TYPES.register(LIGHT_SHEEP_NAME, () -> EntityType.Builder.of(LightSheep::new, MobCategory.CREATURE)
                    .sized(1.0F, 2.0F)
                    .build(LIGHT_SHEEP_NAME));

    public static final RegistryObject<EntityType<OrangeSpottedSparrow>> ORANGE_SPOTTED_SPARROW =
            ENTITY_TYPES.register(ORANGE_SPOTTED_SPARROW_NAME, () -> EntityType.Builder.of(OrangeSpottedSparrow::new, MobCategory.CREATURE)
                    .sized(1.0F, 0.5F)
                    .build(ORANGE_SPOTTED_SPARROW_NAME));

    public static final RegistryObject<EntityType<VidaElementSpider>> ELEMENTAL_SPIDER =
            ENTITY_TYPES.register("element_spider", () -> EntityType.Builder.of(VidaElementSpider::new, MobCategory.MONSTER)
                    .sized(1.4F, 0.9F)
                    .clientTrackingRange(8)
                    .build("element_spider"));

    static <T extends Entity> RegistryObject<EntityType<T>> registerMisc(String name, EntityType.EntityFactory<T> factory) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder
                .of(factory, MobCategory.MISC)
                .sized(.3F, .3F)
                .setUpdateInterval(1)
                .build(name));
    }
}
