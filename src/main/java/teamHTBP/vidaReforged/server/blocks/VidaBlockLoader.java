package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.block.DecoBlockFactory;
import teamHTBP.vidaReforged.core.common.block.templates.DecoFlowerBlock;
import teamHTBP.vidaReforged.core.utils.reg.RegisterItemBlock;
import teamHTBP.vidaReforged.server.blocks.crops.MutationCrop;
import teamHTBP.vidaReforged.server.blocks.crops.ParticleCropBlock;
import teamHTBP.vidaReforged.server.blocks.liquid.VividLiquidBlock;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import static teamHTBP.vidaReforged.core.common.block.DecoBlockFactory.DecoBlockType.*;
import static teamHTBP.vidaReforged.core.common.block.DecoBlockFactory.DecoPropertyType.*;
/**
 * 方块注册管理类
 *
 * */
public class VidaBlockLoader {
    /**注册器*/
    public final static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VidaReforged.MOD_ID);

    /**寂静系列*/
    @RegisterItemBlock
    public final static RegistryObject<Block> SILENT_FOREST_BRICK_0 = registerDecoBlock("silent_forest_brick_0", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SILENT_FOREST_BRICK_1 = registerDecoBlock("silent_forest_brick_1", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SILENT_FOREST_BRICK_2 = registerDecoBlock("silent_forest_brick_2", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SILENT_FOREST_BRICK_3 = registerDecoBlock("silent_forest_brick_3", STONE, LOG);
    @RegisterItemBlock
    public final static RegistryObject<Block> SILENT_FOREST_BRICK_4 = registerDecoBlock("silent_forest_brick_4", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SILENT_FOREST_BRICK_5 = registerDecoBlock("silent_forest_brick_5", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SILENT_FOREST_BRICK_6 = registerDecoBlock("silent_forest_brick_6", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SILENT_FOREST_BRICK_7 = registerDecoBlock("silent_forest_brick_7", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SILENT_FOREST_BRICK_SLAB = registerDecoBlock("silent_forest_brick_slab", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> SILENT_FOREST_BRICK_SLAB_1 = registerDecoBlock("silent_forest_brick_slab_1", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> SILENT_FOREST_STAIR = registerDecoBlock("silent_forest_stair", STONE, STAIRS, SILENT_FOREST_BRICK_0);
    @RegisterItemBlock
    public final static RegistryObject<Block> SILENT_FOREST_WALL = registerDecoBlock("silent_forest_wall", STONE, WALL);

    /**深潜系列**/
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE = registerDecoBlock("deep_stone", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_0 = registerDecoBlock("deep_stone_brick_0", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_1 = registerDecoBlock("deep_stone_brick_1", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_2 = registerDecoBlock("deep_stone_brick_2", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_3 = registerDecoBlock("deep_stone_brick_3", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_4 = registerDecoBlock("deep_stone_brick_4", STONE, LOG);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_SLAB = registerDecoBlock("deep_stone_brick_slab", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_SLAB_1 = registerDecoBlock("deep_stone_brick_slab_1", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_STAIR = registerDecoBlock("deep_stone_brick_stair", STONE, STAIRS,DEEP_STONE_BRICK_0);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_STAIR_1 = registerDecoBlock("deep_stone_brick_stair_1", STONE, STAIRS,DEEP_STONE_BRICK_2);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_WALL = registerDecoBlock("deep_stone_brick_wall", STONE, WALL);

    /**沉渊系列**/
    @RegisterItemBlock
    public final static RegistryObject<Block> SINKING_STONE = registerDecoBlock("sinking_stone", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SINKING_STONE_BRICK_0 = registerDecoBlock("sinking_stone_brick_0", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SINKING_STONE_BRICK_1 = registerDecoBlock("sinking_stone_brick_1", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SINKING_STONE_BRICK_2 = registerDecoBlock("sinking_stone_brick_2", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SINKING_STONE_BRICK_3 = registerDecoBlock("sinking_stone_brick_3", STONE, LOG);
    @RegisterItemBlock
    public final static RegistryObject<Block> SINKING_STONE_BRICK_4 = registerDecoBlock("sinking_stone_brick_4", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SINKING_STONE_BRICK_SLAB = registerDecoBlock("sinking_stone_brick_slab", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> SINKING_STONE_BRICK_SLAB_1 = registerDecoBlock("sinking_stone_brick_slab_1", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> SINKING_STONE_BRICK_STAIR = registerDecoBlock("sinking_stone_brick_stair", STONE, STAIRS,SINKING_STONE_BRICK_0);
    @RegisterItemBlock
    public final static RegistryObject<Block> SINKING_STONE_BRICK_STAIR_1 = registerDecoBlock("sinking_stone_brick_stair_1", STONE, STAIRS,SINKING_STONE_BRICK_1);
    @RegisterItemBlock
    public final static RegistryObject<Block> SINKING_STONE_BRICK_WALL = registerDecoBlock("sinking_stone_brick_wall", STONE,WALL);

    /**生命木板*/
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_PLANK_0 = registerDecoBlock("vida_plank_0", WOOD, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_PLANK_SLAB = registerDecoBlock("vida_plank_slab", WOOD, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_PLANK_DOOR = registerDecoBlock("vida_plank_door", WOOD, DOOR);
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_PLANK_FENCE = registerDecoBlock("vida_plank_fence", WOOD, FENCE);
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_STAIRS = registerDecoBlock("vida_stair", WOOD, STAIRS, VIDA_PLANK_0);
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_PLANK_FENCE_GATE = registerDecoBlock("vida_plank_fence_gate", WOOD, FENCE_GATE);
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_PLANK_TRAPDOOR = registerDecoBlock("vida_plank_trapdoor", WOOD, TRAP_DOOR);
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_PLANK_PRESSURE_PLATE = registerDecoBlock("vida_plank_pressure_plate", WOOD, PRESSURE_PLATE);
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_PLANK_BUTTON = registerDecoBlock("vida_plank_button", WOOD, BUTTON);

    /**寒颤魔石**/
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_STONE = registerDecoBlock("tremble_magic_stone", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_BRICK_0 = registerDecoBlock("tremble_magic_brick_0", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_BRICK_1 = registerDecoBlock("tremble_magic_brick_1", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_BRICK_2 = registerDecoBlock("tremble_magic_brick_2", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_BRICK_3 = registerDecoBlock("tremble_magic_brick_3", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_BRICK_4 = registerDecoBlock("tremble_magic_brick_4", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_BRICK_5 = registerDecoBlock("tremble_magic_brick_5", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_BRICK_SLAB = registerDecoBlock("tremble_magic_brick_slab", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_BRICK_SLAB_1 = registerDecoBlock("tremble_magic_brick_slab_1", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_BRICK_STAIR = registerDecoBlock("tremble_magic_brick_stair", STONE, STAIRS,TREMBLE_MAGIC_BRICK_0);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_BRICK_STAIR_1 = registerDecoBlock("tremble_magic_brick_stair_1", STONE, STAIRS,TREMBLE_MAGIC_BRICK_4);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_BRICK_WALL = registerDecoBlock("tremble_magic_brick_wall", STONE, WALL);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_STONE_CANDLESTICK = registerDecoBlock("tremble_magic_stone_candlestick", STONE, NORMAL, true);
    @RegisterItemBlock
    public final static RegistryObject<Block> TREMBLE_MAGIC_STONE_BENCH = registerDecoBlock("tremble_magic_stone_bench", STONE, NORMAL, true);

    /**锃闪石系列**/
    @RegisterItemBlock
    public final static RegistryObject<Block> GLISTENING_STONE = registerDecoBlock("glistening_stone", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> GLISTENING_STONE_BRICK_0 = registerDecoBlock("glistening_stone_brick_0", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> GLISTENING_STONE_BRICK_1 = registerDecoBlock("glistening_stone_brick_1", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> GLISTENING_STONE_BRICK_2 = registerDecoBlock("glistening_stone_brick_2", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> GLISTENING_STONE_BRICK_3 = registerDecoBlock("glistening_stone_brick_3", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> GLISTENING_STONE_BRICK_4 = registerDecoBlock("glistening_stone_brick_4", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> GLISTENING_STONE_SLAB = registerDecoBlock("glistening_stone_slab", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> GLISTENING_STONE_SLAB_1 = registerDecoBlock("glistening_stone_slab_1", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> GLISTENING_STONE_STAIR = registerDecoBlock("glistening_stone_stair", STONE, STAIRS,GLISTENING_STONE);
    @RegisterItemBlock
    public final static RegistryObject<Block> GLISTENING_STONE_STAIR_1 = registerDecoBlock("glistening_stone_stair_1", STONE, STAIRS,GLISTENING_STONE);
    @RegisterItemBlock
    public final static RegistryObject<Block> GLISTENING_STONE_WALL = registerDecoBlock("glistening_stone_wall", STONE, WALL);
    @RegisterItemBlock
    public final static RegistryObject<Block> GLISTENING_STONE_WALL_1 = registerDecoBlock("glistening_stone_wall_1", STONE, WALL);

    /**烟熔石系列**/
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE = registerDecoBlock("smoke_stone", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_BRICK_0 = registerDecoBlock("smoke_stone_brick_0", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_BRICK_1 = registerDecoBlock("smoke_stone_brick_1", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_BRICK_2 = registerDecoBlock("smoke_stone_brick_2", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_BRICK_3 = registerDecoBlock("smoke_stone_brick_3", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_BRICK_4 = registerDecoBlock("smoke_stone_brick_4", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_SLAB = registerDecoBlock("smoke_stone_slab", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_SLAB_1 = registerDecoBlock("smoke_stone_slab_1", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_SLAB_2 = registerDecoBlock("smoke_stone_slab_2", STONE, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_STAIR = registerDecoBlock("smoke_stone_stair", STONE, STAIRS,SMOKE_STONE);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_STAIR_1 = registerDecoBlock("smoke_stone_stair_1", STONE, STAIRS,SMOKE_STONE_BRICK_0);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_STAIR_2 = registerDecoBlock("smoke_stone_stair_2", STONE, STAIRS,SMOKE_STONE_BRICK_2);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_WALL = registerDecoBlock("smoke_stone_wall", STONE, WALL);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMOKE_STONE_WALL_1 = registerDecoBlock("smoke_stone_wall_1", STONE, WALL);

    /**生命原木*/
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_LOG = registerDecoBlock("vida_log", WOOD, LOG);

    @RegisterItemBlock
    public final static RegistryObject<Block> WHITE_VIDA_LOG = registerDecoBlock("white_vida_log", WOOD, LOG);

    @RegisterItemBlock
    public final static RegistryObject<Block> ROTTEN_VIDA_LOG = registerDecoBlock("rotten_vida_log", WOOD, LOG);
    /**生命原木变种*/
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_STRIPPED_LOG = registerDecoBlock("vida_stripped_log", WOOD, LOG);
    /**生命菌光体*/
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_SHROOMLIGHT = registerDecoBlock("vida_shroomlight", WOOD, LOG);
    /**生命树叶*/
    @RegisterItemBlock
    public static RegistryObject<Block> VIDA_LEAVES = registerDecoBlock("vida_leaves", LEAVES, NORMAL, true);

    /**蘑菇**/
    @RegisterItemBlock
    public final static RegistryObject<Block> EMERALD_MUSHROOM_STEM = registerDecoBlock("emerald_mushroom_stem", WOOD, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> EMERALD_MUSHROOM_BLOCK = registerDecoBlock("emerald_mushroom_block", WOOD, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> MYSTIC_MUSHROOM_STEM = registerDecoBlock("mystic_mushroom_stem", WOOD, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> MYSTIC_MUSHROOM_BLOCK = registerDecoBlock("mystic_mushroom_block", WOOD, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> BLUE_JELLY_MUSHROOM_STEM = registerDecoBlock("blue_jelly_mushroom_stem", WOOD, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> BLUE_JELLY_MUSHROOM_BLOCK = registerDecoBlock("blue_jelly_mushroom_block", WOOD, NORMAL);

    /*花*/
    /**太阳花*/
    @RegisterItemBlock
    public final static RegistryObject<Block> SUN_FLOWER = registerDecoBlock("sun_flower", GRASS, FLOWER);
    /**月光花*/
    @RegisterItemBlock
    public final static RegistryObject<Block> LUNARIA_FLOWER = BLOCKS.register("lunaria_flower", () -> new DecoFlowerBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).lightLevel((state) -> 2)));
    @RegisterItemBlock
    public final static RegistryObject<Block> GREEN_DEW_FLOWER = registerDecoBlock("green_dew_flower", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> VERMILLION_BOWL = registerDecoBlock("vermillion_bowl", GRASS, FLOWER);
    /**灯草*/
    @RegisterItemBlock
    public final static RegistryObject<Block> LIGHT_GRASS = BLOCKS.register("light_grass", () -> new DecoFlowerBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).lightLevel((state) -> 7)));
    @RegisterItemBlock
    public final static RegistryObject<Block> BLUE_GRASS = registerDecoBlock("blue_grass", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMALL_EMERALD_MUSHROOM = registerDecoBlock("small_emerald_mushroom", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> EMERALD_MUSHROOM = registerDecoBlock("emerald_mushroom", GRASS, DOUBLE_PLANT);
    @RegisterItemBlock
    public final static RegistryObject<Block> PINK_JELLY_FLOWER = registerDecoBlock("pink_jelly_flower", GRASS, DOUBLE_PLANT);
    @RegisterItemBlock
    public final static RegistryObject<Block> MYSTIC_MUSHROOM = registerDecoBlock("mystic_mushroom", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> BLUE_JELLY_MUSHROOM = registerDecoBlock("blue_jelly_mushroom", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> FRAGRANT_FLOWER = registerDecoBlock("fragrant_flower", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> SOULFUL_FLOWER = registerDecoBlock("soulful_flower", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> AGATE_CORAL = registerDecoBlock("agate_coral", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> SEASIDE_ANEMONE = registerDecoBlock("seaside_anemone", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> SUNNY_FLOWER = registerDecoBlock("sunny_flower", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> OOZE_BLOOM_FLOWER = registerDecoBlock("ooze_bloom_flower", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> UMBRELLA_FLOWER = registerDecoBlock("umbrella_flower", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> DAZZLING_FLOWER = registerDecoBlock("dazzling_flower", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> SNOW_FLOWER = registerDecoBlock("snow_flower", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> MOONLIGHT_PURPLE_FLOWER = registerDecoBlock("moonlight_purple_flower", GRASS, FLOWER);

    /**作物*/
    @RegisterItemBlock
    public static RegistryObject<Block> CRIMSON_CREST = BLOCKS.register("crimson_crest", () ->
            new MutationCrop(
                    VidaElement.FIRE,
                    5,
                    ()-> Blocks.WHEAT,
                    ()-> Items.REDSTONE)

    );
    @RegisterItemBlock
    public static RegistryObject<Block> HEART_OF_WAL = BLOCKS.register("heart_of_wal", () -> new ParticleCropBlock(VidaElement.WOOD, ()-> VidaItemLoader.HEART_OF_WAL_SEED_ITEM.get(), () -> VidaBlockLoader.HEART_OF_WAL_JUICE_PLACED.get()));
    @RegisterItemBlock
    public static RegistryObject<Block> NITRITE_THORNS = BLOCKS.register("nitrite_thorns", () -> new ParticleCropBlock(VidaElement.GOLD, ()-> VidaItemLoader.NITRITE_THORNS_SEED_ITEM.get(), () -> VidaBlockLoader.NITRITE_TEA_PLACED.get()));
    @RegisterItemBlock
    public static RegistryObject<Block> PLAM_STEM = BLOCKS.register("plam_stem", () -> new ParticleCropBlock( VidaElement.AQUA, ()-> VidaItemLoader.PLAM_STEM_SEED_ITEM.get(), () -> VidaBlockLoader.PLAM_STEM_TEA_PLACED.get()));
    @RegisterItemBlock
    public static RegistryObject<Block> SULLEN_HYDRANGEA = BLOCKS.register("sullen_hydrangea", () -> new ParticleCropBlock(VidaElement.AQUA, ()-> VidaItemLoader.SULLEN_HYDRANGEA_SEED_ITEM.get(), () -> VidaBlockLoader.SULLEN_HYDRANGEA_SOUP_PLACED.get()));
    @RegisterItemBlock
    public static RegistryObject<Block> SWEET_CYAN_REED  = BLOCKS.register("sweet_cyan_reed", () -> new ParticleCropBlock(VidaElement.WOOD, ()-> VidaItemLoader.SWEET_CYAN_REED_SEED_ITEM.get(), () -> VidaBlockLoader.DRIED_SWEET_CYAN_REED_PLACED.get()));

    /**矿物*/
    @RegisterItemBlock
    public static RegistryObject<Block> GOLD_ELEMENT_ORE = BLOCKS.register("gold_element_ore", () -> new VidaOreBlock(VidaElement.GOLD));
    @RegisterItemBlock
    public static RegistryObject<Block> WOOD_ELEMENT_ORE = BLOCKS.register("wood_element_ore", () -> new VidaOreBlock(VidaElement.WOOD));
    @RegisterItemBlock
    public static RegistryObject<Block> AQUA_ELEMENT_ORE = BLOCKS.register("aqua_element_ore", () -> new VidaOreBlock(VidaElement.AQUA));
    @RegisterItemBlock
    public static RegistryObject<Block> FIRE_ELEMENT_ORE = BLOCKS.register("fire_element_ore", () -> new VidaOreBlock(VidaElement.FIRE));
    @RegisterItemBlock
    public static RegistryObject<Block> EARTH_ELEMENT_ORE = BLOCKS.register("earth_element_ore", () -> new VidaOreBlock(VidaElement.EARTH));
    @RegisterItemBlock
    public static RegistryObject<Block> DEEPSLATE_GOLD_ELEMENT_ORE = BLOCKS.register("deepslate_gold_element_ore", () -> new VidaOreBlock(VidaElement.GOLD));
    @RegisterItemBlock
    public static RegistryObject<Block> DEEPSLATE_WOOD_ELEMENT_ORE = BLOCKS.register("deepslate_wood_element_ore", () -> new VidaOreBlock(VidaElement.WOOD));
    @RegisterItemBlock
    public static RegistryObject<Block> DEEPSLATE_AQUA_ELEMENT_ORE = BLOCKS.register("deepslate_aqua_element_ore", () -> new VidaOreBlock(VidaElement.AQUA));
    @RegisterItemBlock
    public static RegistryObject<Block> DEEPSLATE_FIRE_ELEMENT_ORE = BLOCKS.register("deepslate_fire_element_ore", () -> new VidaOreBlock(VidaElement.FIRE));
    @RegisterItemBlock
    public static RegistryObject<Block> DEEPSLATE_EARTH_ELEMENT_ORE = BLOCKS.register("deepslate_earth_element_ore", () -> new VidaOreBlock(VidaElement.EARTH));



    /*功能性方块*/
    @RegisterItemBlock
    public static RegistryObject<Block> COLLECTOR = BLOCKS.register("collector", CollectorBlock::new);

    @RegisterItemBlock
    public static RegistryObject<Block> GLOWING_LIGHT_BLOCK = BLOCKS.register("glowing_light", GlowingLightBlock::new);
    @RegisterItemBlock
    public static RegistryObject<Block> GLOWING_LIGHT_BLOCK_GOLD = BLOCKS.register("glowing_light_gold", () -> new GlowingLightBlock(VidaElement.GOLD));
    @RegisterItemBlock
    public static RegistryObject<Block> GLOWING_LIGHT_BLOCK_WOOD = BLOCKS.register("glowing_light_wood", () -> new GlowingLightBlock(VidaElement.WOOD));
    @RegisterItemBlock
    public static RegistryObject<Block> GLOWING_LIGHT_BLOCK_AQUA = BLOCKS.register("glowing_light_aqua", () -> new GlowingLightBlock(VidaElement.AQUA));
    @RegisterItemBlock
    public static RegistryObject<Block> GLOWING_LIGHT_BLOCK_FIRE = BLOCKS.register("glowing_light_fire", () -> new GlowingLightBlock(VidaElement.FIRE));
    @RegisterItemBlock
    public static RegistryObject<Block> GLOWING_LIGHT_BLOCK_EARTH = BLOCKS.register("glowing_light_earth", () -> new GlowingLightBlock(VidaElement.EARTH));

    /*食物*/
    @RegisterItemBlock
    public final static RegistryObject<Block> FRIED_CRIMSON_CREST_PLACED = registerDecoBlock("fried_crimson_crest_placed", STONE,YAXIS,true);
    @RegisterItemBlock
    public final static RegistryObject<Block> HEART_OF_WAL_JUICE_PLACED = registerDecoBlock("heart_of_wal_juice_placed", STONE,YAXIS,true);
    @RegisterItemBlock
    public final static RegistryObject<Block> NITRITE_TEA_PLACED = registerDecoBlock("nitrite_tea_placed", STONE,YAXIS,true);
    @RegisterItemBlock
    public final static RegistryObject<Block> PLAM_STEM_TEA_PLACED = registerDecoBlock("plam_stem_tea_placed", STONE,YAXIS,true);
    @RegisterItemBlock
    public final static RegistryObject<Block> PROCESSED_SULLEN_HYDRANGEA_BERRY_PLACED = registerDecoBlock("processed_sullen_hydrangea_berry_placed", STONE,YAXIS,true);
    @RegisterItemBlock
    public final static RegistryObject<Block> SULLEN_HYDRANGEA_SOUP_PLACED = registerDecoBlock("sullen_hydrangea_soup_placed", STONE,YAXIS,true);
    @RegisterItemBlock
    public final static RegistryObject<Block> DRIED_SWEET_CYAN_REED_PLACED = registerDecoBlock("dried_sweet_cyan_reed_placed", STONE,YAXIS,true);

    @RegisterItemBlock
    public final static RegistryObject<Block> TIME_ELEMENT_CRAFTING_TABLE = BLOCKS.register("time_element_crafting_table", TimeElementCraftingTable::new);

    @RegisterItemBlock
    public final static RegistryObject<Block> PURIFICATION_CAULDRON = BLOCKS.register("purification_cauldron", PurificationCauldron::new);

    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK = BLOCKS.register("teacon_guidebook", () -> new TeaconGuideBookBlock("vida_reforged:vida"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_01 = BLOCKS.register("teacon_guidebook_01", () -> new TeaconGuideBookBlock("vida_reforged:guide_silent_forest_brick"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_02 = BLOCKS.register("teacon_guidebook_02", () -> new TeaconGuideBookBlock("vida_reforged:vida_02"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_03 = BLOCKS.register("teacon_guidebook_03", () -> new TeaconGuideBookBlock("vida_reforged:vida_03"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_04 = BLOCKS.register("teacon_guidebook_04", () -> new TeaconGuideBookBlock("vida_reforged:vida_04"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_05 = BLOCKS.register("teacon_guidebook_05", () -> new TeaconGuideBookBlock("vida_reforged:vida_05"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_06 = BLOCKS.register("teacon_guidebook_06", () -> new TeaconGuideBookBlock("vida_reforged:vida_06"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_07 = BLOCKS.register("teacon_guidebook_07", () -> new TeaconGuideBookBlock("vida_reforged:vida_07"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_08 = BLOCKS.register("teacon_guidebook_08", () -> new TeaconGuideBookBlock("vida_reforged:vida_08"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_09 = BLOCKS.register("teacon_guidebook_09", () -> new TeaconGuideBookBlock("vida_reforged:vida_09"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_10 = BLOCKS.register("teacon_guidebook_10", () -> new TeaconGuideBookBlock("vida_reforged:vida_10"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_11 = BLOCKS.register("teacon_guidebook_11", () -> new TeaconGuideBookBlock("vida_reforged:vida_11"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_12 = BLOCKS.register("teacon_guidebook_12", () -> new TeaconGuideBookBlock("vida_reforged:vida_12"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_13 = BLOCKS.register("teacon_guidebook_13", () -> new TeaconGuideBookBlock("vida_reforged:vida_13"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_14 = BLOCKS.register("teacon_guidebook_14", () -> new TeaconGuideBookBlock("vida_reforged:vida_14"));
    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK_15 = BLOCKS.register("teacon_guidebook_15", () -> new TeaconGuideBookBlock("vida_reforged:vida_15"));
    @RegisterItemBlock
    public final static RegistryObject<Block> JIGSAW_EQUIP = BLOCKS.register("jigsaw_equip", VidaMagicJigsawEquipingBlock::new);
    @RegisterItemBlock
    public final static RegistryObject<Block> MAGIC_WORD_CRAFTING = BLOCKS.register("magic_word_crafting_table", MagicWordCraftingTable::new);
    @RegisterItemBlock
    public final static RegistryObject<Block> GOLD_FLOATING_ELEMENT_CRYSTAL = BLOCKS.register("gold_floating_element_crystal", ()-> new FloatingCrystalBlock(VidaElement.GOLD));
    @RegisterItemBlock
    public final static RegistryObject<Block> WOOD_FLOATING_ELEMENT_CRYSTAL = BLOCKS.register("wood_floating_element_crystal", ()-> new FloatingCrystalBlock(VidaElement.WOOD));
    @RegisterItemBlock
    public final static RegistryObject<Block> AQUA_FLOATING_ELEMENT_CRYSTAL = BLOCKS.register("aqua_floating_element_crystal", ()-> new FloatingCrystalBlock(VidaElement.AQUA));
    @RegisterItemBlock
    public final static RegistryObject<Block> FIRE_FLOATING_ELEMENT_CRYSTAL = BLOCKS.register("fire_floating_element_crystal", ()-> new FloatingCrystalBlock(VidaElement.FIRE));
    @RegisterItemBlock
    public final static RegistryObject<Block> EARTH_FLOATING_ELEMENT_CRYSTAL = BLOCKS.register("earth_floating_element_crystal", ()-> new FloatingCrystalBlock(VidaElement.EARTH));
    @RegisterItemBlock
    public final static RegistryObject<Block> CRYSTAL_DECORATION_BLOCK = BLOCKS.register("gem_shelf", CrystalDecorationBlock::new);
    @RegisterItemBlock
    public final static RegistryObject<Block> PRISM = BLOCKS.register("prism", PrismBlock::new);
    @RegisterItemBlock
    public final static RegistryObject<Block> INJECT_TABLE = BLOCKS.register("inject_table", InjectTable::new);
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_WAND_CRATING_TABLE = BLOCKS.register("vida_wand_crating_table", VidaWandCraftingTable::new);

    @RegisterItemBlock
    public final static RegistryObject<Block> MAGIC_MANA_PROVIDER_GOLD = BLOCKS.register("magic_mana_provider_gold", ()->new MagicManaProvider(VidaElement.GOLD));
    @RegisterItemBlock
    public final static RegistryObject<Block> MAGIC_MANA_PROVIDER_WOOD = BLOCKS.register("magic_mana_provider_wood", ()->new MagicManaProvider(VidaElement.WOOD));
    @RegisterItemBlock
    public final static RegistryObject<Block> MAGIC_MANA_PROVIDER_AQUA = BLOCKS.register("magic_mana_provider_aqua", ()->new MagicManaProvider(VidaElement.AQUA));
    @RegisterItemBlock
    public final static RegistryObject<Block> MAGIC_MANA_PROVIDER_FIRE = BLOCKS.register("magic_mana_provider_fire", ()->new MagicManaProvider(VidaElement.FIRE));
    @RegisterItemBlock
    public final static RegistryObject<Block> MAGIC_MANA_PROVIDER_EARTH = BLOCKS.register("magic_mana_provider_earth", ()->new MagicManaProvider(VidaElement.EARTH));
    @RegisterItemBlock
    public final static RegistryObject<Block> CRYSTAL_LANTERN = BLOCKS.register("crystal_lantern", CrystalLantern::new);

    /**液体*/
    public final static RegistryObject<Block> VIVID_LIQUID = BLOCKS.register("vivid_liquid",  VividLiquidBlock::new);


    /**
     * @param name 注册名称
     * @param block 方块provider
     * @return
     */
    public static RegistryObject<Block> registerBlock(String name, Block block){
        return BLOCKS.register(name, () -> block);
    }


    /**
     * 注册装饰性方块
     * @param name 注册名称
     * @param propertyType 通用方块属性
     * @param buildType 方块建造者
     * @return 注册方块
     */
    public static RegistryObject<Block> registerDecoBlock(String name, DecoBlockFactory.DecoPropertyType propertyType, DecoBlockFactory.DecoBlockType buildType){
        return BLOCKS.register(
                name,
                new DecoBlockFactory.Builder(propertyType)
                        .setStructureType(buildType)
                        .build()
        );
    }

    /**
     * 注册装饰性方块
     * @param name 注册名称
     * @param propertyType 通用方块属性
     * @param buildType 方块建造者
     * @param noSolid 是否为整体方块
     * @return 注册方块
     */
    public static RegistryObject<Block> registerDecoBlock(String name, DecoBlockFactory.DecoPropertyType propertyType, DecoBlockFactory.DecoBlockType buildType,boolean noSolid){
        DecoBlockFactory.Builder builder = new DecoBlockFactory.Builder(propertyType).setStructureType(buildType);
        if(noSolid){
            builder.noOcclusion();
        }
        return BLOCKS.register(name,builder.build());
    }

    /**
     * 注册装饰性方块,此方法一般为阶梯方块使用
     * @param name 注册名称
     * @param propertyType 通用方块属性
     * @param buildType 方块建造者
     * @param baseBlock 基准方块
     * @return 注册方块
     */
    public static RegistryObject<Block> registerDecoBlock(String name, DecoBlockFactory.DecoPropertyType propertyType, DecoBlockFactory.DecoBlockType buildType,RegistryObject<Block> baseBlock){
        return BLOCKS.register(name, new DecoBlockFactory.Builder(propertyType).noOcclusion().setStructureType(buildType).setBaseBlock(baseBlock).build());
    }
}
