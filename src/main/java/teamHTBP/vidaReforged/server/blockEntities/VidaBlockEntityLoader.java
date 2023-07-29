package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.server.blocks.PurificationCauldron;
import teamHTBP.vidaReforged.server.blocks.TeaconGuideBookBlock;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaBlockEntityLoader {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);

    public static final RegistryObject<BlockEntityType<GlowingLightBlockEntity>> GLOWING_LIGHT = BLOCK_ENTITIES.register(
            "glowing_light",
            () -> BlockEntityType.Builder.of(GlowingLightBlockEntity::new, VidaBlockLoader.GLOWING_LIGHT_BLOCK.get()).build(null)
    );

    public static final RegistryObject<BlockEntityType<BasePurificationCauldronBlockEntity>> PURIFICATION_CAULDRON = BLOCK_ENTITIES.register(
            "purification_cauldron",
            () -> BlockEntityType.Builder.of(BasePurificationCauldronBlockEntity::new, VidaBlockLoader.PURIFICATION_CAULDRON.get()).build(null)
    );

    public static final RegistryObject<BlockEntityType<CollectorBlockEntity>> COLLECTOR = BLOCK_ENTITIES.register(
            "collector",
            () -> BlockEntityType.Builder.of(CollectorBlockEntity::new, VidaBlockLoader.COLLECTOR.get()).build(null)
    );

    public static final RegistryObject<BlockEntityType<TeaconGuideBookBlockEntity>> TEACON_GUIDEBOOK = BLOCK_ENTITIES.register(
            "teacon_guidebook",
            () -> BlockEntityType.Builder.of(
                    TeaconGuideBookBlockEntity::new,
                    VidaBlockLoader.TEACON_GUIDEBOOK.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_01.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_02.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_03.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_04.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_05.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_06.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_07.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_08.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_09.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_10.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_11.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_12.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_13.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_14.get(),
                    VidaBlockLoader.TEACON_GUIDEBOOK_15.get()
            ).build(null)
    );

    public static final RegistryObject<BlockEntityType<VidaMagicJigsawEquipingBlockEntity>> JIGSAW_EQUIP = BLOCK_ENTITIES.register(
            "jigsaw_equip",
            () -> BlockEntityType.Builder.of(VidaMagicJigsawEquipingBlockEntity::new, VidaBlockLoader.JIGSAW_EQUIP.get()).build(null)
    );

    public static final RegistryObject<BlockEntityType<MagicWordCraftingTableBlockEntity>> MAGIC_WORD_CRAFTING = BLOCK_ENTITIES.register(
            "magic_word_crafting_table",
            () -> BlockEntityType.Builder.of(MagicWordCraftingTableBlockEntity::new, VidaBlockLoader.MAGIC_WORD_CRAFTING.get()).build(null)
    );

    public static final RegistryObject<BlockEntityType<FloatingCrystalBlockEntity>> FLOATING_CRYSTAL = BLOCK_ENTITIES.register(
            "floating_crystal",
            () -> BlockEntityType.Builder.of(FloatingCrystalBlockEntity::new,
                    VidaBlockLoader.GOLD_FLOATING_ELEMENT_CRYSTAL.get(),
                    VidaBlockLoader.WOOD_FLOATING_ELEMENT_CRYSTAL.get(),
                    VidaBlockLoader.AQUA_FLOATING_ELEMENT_CRYSTAL.get(),
                    VidaBlockLoader.FIRE_FLOATING_ELEMENT_CRYSTAL.get(),
                    VidaBlockLoader.EARTH_FLOATING_ELEMENT_CRYSTAL.get()
            ).build(null)
    );

    public static final RegistryObject<BlockEntityType<PrismBlockEntity>> PRISM = BLOCK_ENTITIES.register(
            "prism", () -> BlockEntityType.Builder.of(PrismBlockEntity::new, VidaBlockLoader.PRISM.get()).build(null)
    );

    public static final RegistryObject<BlockEntityType<CrystalDecorationBlockEntity>> GEM_SHELF = BLOCK_ENTITIES.register(
            "gem_shelf", () -> BlockEntityType.Builder.of(CrystalDecorationBlockEntity::new, VidaBlockLoader.CRYSTAL_DECORATION_BLOCK.get()).build(null)
    );
}
