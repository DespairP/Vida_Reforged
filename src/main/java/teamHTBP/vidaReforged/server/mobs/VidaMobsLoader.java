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
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, VidaReforged.MOD_ID);
    public static final RegistryObject<EntityType<AncientBeliever>> ANCIENT_BELIEVER =
            ENTITY_TYPES.register(ANCIENT_BELIEVER_NAME, () -> EntityType.Builder.of(AncientBeliever::new, MobCategory.MONSTER)
                    .sized(1, 2)
                    .build(ANCIENT_BELIEVER_NAME));


    static <T extends Entity> RegistryObject<EntityType<T>> registerMisc(String name, EntityType.EntityFactory<T> factory) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder
                .of(factory, MobCategory.MISC)
                .sized(.3F, .3F)
                .setUpdateInterval(1)
                .build(name));
    }
}
