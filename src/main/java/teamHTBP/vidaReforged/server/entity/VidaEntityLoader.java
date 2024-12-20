package teamHTBP.vidaReforged.server.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.server.entity.projectile.MagicParticleProjectile;
import teamHTBP.vidaReforged.server.entity.projectile.PartyParrotProjecttile;

/**
 * @author TT432
 */
public class VidaEntityLoader {
    /**
     * 注册器
     */
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VidaReforged.MOD_ID);

    @Deprecated
    public static final RegistryObject<EntityType<MagicParticleProjectile>> MAGIC_PARTICLE_PROJECTILE = ENTITIES
            .register("magic_particle_projectile", () -> EntityType.Builder
                    .of(MagicParticleProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("magic_particle_projectile"));

    public static final RegistryObject<EntityType<PartyParrotProjecttile>> PARTY_PARROT = ENTITIES
            .register("party_parrot_projectile", () -> EntityType.Builder
                    .of(PartyParrotProjecttile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("party_parrot_projectile"));

    //
    public static final RegistryObject<EntityType<SparkEntity>> SPARK = ENTITIES
            .register("spark", () -> EntityType.Builder
                    .of(SparkEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("spark_entity")
            );

    //
    public static final RegistryObject<EntityType<MultiblockSparkEntity>> MULTIBLOCK_TRAIL = ENTITIES
            .register("multiblock_trail", () -> EntityType.Builder
                    .of(MultiblockSparkEntity::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .clientTrackingRange(4)
                    .updateInterval(2)
                    .build("trail_entity")
            );

    public static final RegistryObject<EntityType<StarGlintEntity>> STAR_GLINT = ENTITIES
            .register("star_glint", () -> EntityType.Builder
                    .of(StarGlintEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(2)
                    .build("trail_entity")
            );

    public static final RegistryObject<EntityType<FloatingItemEntity>> FLOATING_ITEM_ENTITY = ENTITIES
            .register("floating_item", () -> EntityType.Builder
                    .of((EntityType<FloatingItemEntity> entityType, Level level) -> new FloatingItemEntity(entityType, level), MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(6)
                    .updateInterval(20)
                    .build("floating_item")
            );

    public static final RegistryObject<EntityType<FakeHarmonizeTableItemEntity>> FAKE_HARMONIZE_TABLE_ITEM_ENTITY = ENTITIES
            .register("fake_harmonize_table_item", () -> EntityType.Builder
                    .of((EntityType<FakeHarmonizeTableItemEntity>entityType,Level level) -> new FakeHarmonizeTableItemEntity(entityType, level), MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(6)
                    .updateInterval(10)
                    .build("fake_harmonize_table_item")
            );
}
