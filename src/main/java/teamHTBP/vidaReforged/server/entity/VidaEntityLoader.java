package teamHTBP.vidaReforged.server.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.server.entity.projectile.MagicParticleProjectile;

/**
 * @author TT432
 */
public class VidaEntityLoader {
    /**
     * 注册器
     */
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VidaReforged.MOD_ID);

    public static final RegistryObject<EntityType<MagicParticleProjectile>> MAGIC_PARTICLE_PROJECTILE = ENTITIES
            .register("magic_particle_projectile", () -> EntityType.Builder
                    .of(MagicParticleProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("magic_particle_projectile"));
}
