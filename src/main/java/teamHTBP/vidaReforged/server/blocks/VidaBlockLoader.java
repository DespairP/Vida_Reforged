package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.block.DecoBlockFactory;
import teamHTBP.vidaReforged.core.utils.reg.CustomModelBlock;
import teamHTBP.vidaReforged.core.utils.reg.RegisterItemBlock;
import teamHTBP.vidaReforged.server.blocks.crops.ParticleCropBlock;

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
    public final static RegistryObject<Block> SILENT_FOREST_BRICK_3 = registerDecoBlock("silent_forest_brick_3", STONE, NORMAL);
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
    public final static RegistryObject<Block> DEEP_STONE_BRICK_1 = registerDecoBlock("deep_stone_brick_1", STONE, YAXIS);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_2 = registerDecoBlock("deep_stone_brick_2", STONE, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_3 = registerDecoBlock("deep_stone_brick_3", STONE, YAXIS);
    @RegisterItemBlock
    public final static RegistryObject<Block> DEEP_STONE_BRICK_4 = registerDecoBlock("deep_stone_brick_4", STONE, YAXIS);


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

    /**生命原木*/
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_LOG = registerDecoBlock("vida_log", WOOD, LOG);
    /**生命原木变种*/
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_STRIPPED_LOG = registerDecoBlock("vida_stripped_log", WOOD, LOG);
    /**生命树叶*/
    @RegisterItemBlock
    public static RegistryObject<Block> VIDA_LEAVES = registerDecoBlock("vida_leaves", LEAVES, NORMAL, true);


    /**花*/
    @RegisterItemBlock
    public final static RegistryObject<Block> SUN_FLOWER = registerDecoBlock("sun_flower", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> LUNARIA_FLOWER = registerDecoBlock("lunaria_flower", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> GREEN_DEW_FLOWER = registerDecoBlock("green_dew_flower", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> VERMILLION_BOWL = registerDecoBlock("vermillion_bowl", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> LIGHT_GRASS = registerDecoBlock("light_grass", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> BLUE_GRASS = registerDecoBlock("blue_grass", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> SMALL_EMERALD_MUSHROOM = registerDecoBlock("small_emerald_mushroom", GRASS, FLOWER);
    @RegisterItemBlock
    public final static RegistryObject<Block> EMERALD_MUSHROOM = registerDecoBlock("emerald_mushroom", GRASS, DOUBLE_PLANT);
    @RegisterItemBlock
    public final static RegistryObject<Block> PINK_JELLY_FLOWER = registerDecoBlock("pink_jelly_flower", GRASS, DOUBLE_PLANT);

    /**作物*/
    @RegisterItemBlock
    public static RegistryObject<Block> CRISM_CREST = BLOCKS.register("crimson_crest", () -> new ParticleCropBlock(VidaElement.FIRE, null));
    //public static RegistryObject<Block> HEART_OF_WAL = BLOCKS.register("heart_of_wal", () -> new ParticleCropBlock(VidaElement.WOOD, null));
    //public static RegistryObject<Block> NITRITE_THORNS = BLOCKS.register("nitrite_thorns", () -> new ParticleCropBlock(VidaElement.GOLD, null));
    //public static RegistryObject<Block> PLAM_STEM = BLOCKS.register("plam_stem", () -> new ParticleCropBlock( VidaElement.AQUA, null));
    //public static RegistryObject<Block> SULLEN_HYDRANGE = BLOCKS.register("sullen_hydrangea", () -> new ParticleCropBlock(VidaElement.AQUA, null));
    //public static RegistryObject<Block> SWEET_CYAN_REED  = BLOCKS.register("sweet_cyan_reed", () -> new ParticleCropBlock(VidaElement.WOOD, null));

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
    public final static RegistryObject<Block> TIME_ELEMENT_CRAFTING_TABLE = BLOCKS.register("time_element_crafting_table", TimeElementCraftingTable::new);

    @RegisterItemBlock
    public final static RegistryObject<Block> PURIFICATION_CAULDRON = BLOCKS.register("purification_cauldron", PurificationCauldron::new);

    @RegisterItemBlock
    public final static RegistryObject<Block> TEACON_GUIDEBOOK = BLOCKS.register("teacon_guidebook", TeaconGuideBookBlock::new);

    @RegisterItemBlock
    public final static RegistryObject<Block> JIGSAW_EQUIP = BLOCKS.register("jigsaw_equip", VidaMagicJigsawEquipingBlock::new);

    @RegisterItemBlock
    public final static RegistryObject<Block> MAGIC_WORD_CRAFTING = BLOCKS.register("magic_word_crafting_table", MagicWordCraftingTable::new);

    /**
     * @param name 注册名称
     * @param block 方块provider
     * @return
     */
    public static RegistryObject<Block> registerBlock(String name,Block block){
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
