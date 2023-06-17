package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaBlockEntityLoader {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);

    public static final RegistryObject<BlockEntityType<GlowingLightBlockEntity>> GLOWING_LIGHT = BLOCK_ENTITIES.register(
            "glowing_light",
            () -> BlockEntityType.Builder.of(GlowingLightBlockEntity::new, VidaBlockLoader.GLOWING_LIGHT_BLOCK.get()).build(null)
    );
}
