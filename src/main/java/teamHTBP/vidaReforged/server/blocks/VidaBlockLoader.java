package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.events.ModelRenderTypeAutoRegisterHandler;
import teamHTBP.vidaReforged.core.common.block.DecoBlockFactory;
import teamHTBP.vidaReforged.core.utils.reg.CustomModelBlock;
import teamHTBP.vidaReforged.core.utils.reg.RegisterItemBlock;

import static teamHTBP.vidaReforged.core.common.block.DecoBlockFactory.DecoBlockType.*;
import static teamHTBP.vidaReforged.core.common.block.DecoBlockFactory.DecoPropertyType.*;
import static teamHTBP.vidaReforged.client.events.ModelRenderTypeAutoRegisterHandler.CustomModelRenderType.*;
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

    /**生命木板**/
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_PLANK_0 = registerDecoBlock("vida_plank_0", WOOD, NORMAL);
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_PLANK_SLAB = registerDecoBlock("vida_plank_slab", WOOD, SLAB);
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_PLANK_DOOR = registerDecoBlock("vida_plank_door", WOOD, DOOR);
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_PLANK_FENCE = registerDecoBlock("vida_plank_fence", WOOD, FENCE);
    @CustomModelBlock(CUTOUT)
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_STAIRS = registerDecoStairBlock("vida_stair", VIDA_PLANK_0, WOOD);

    /**生命原木*/
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_LOG = registerDecoBlock("vida_log", WOOD, LOG);
    /**生命原木变种*/
    @RegisterItemBlock
    public final static RegistryObject<Block> VIDA_STRIPPED_LOG = registerDecoBlock("vida_stripped_log", WOOD, LOG);
    /**生命树叶*/
    @CustomModelBlock(CUTOUT)
    @RegisterItemBlock
    public static RegistryObject<Block> VIDA_LEAVES = registerDecoBlock("vida_leaves", GRASS, NORMAL, true);


    /**花*/


    @CustomModelBlock(CUTOUT_MIPPED)
    @RegisterItemBlock
    public static RegistryObject<Block> collector = registerDecoBlock("collector", STANDARD, NORMAL, true);



    public static RegistryObject<Block> registerBlock(String name,Block block){
        return BLOCKS.register(name, () -> block);
    }

    public static RegistryObject<Block> registerDecoBlock(String name, DecoBlockFactory.DecoPropertyType propertyType, DecoBlockFactory.DecoBlockType buildType){
        return BLOCKS.register(name, new DecoBlockFactory.Builder().setProperties(propertyType).build(buildType));
    }

    public static RegistryObject<Block> registerDecoBlock(String name, DecoBlockFactory.DecoPropertyType propertyType, DecoBlockFactory.DecoBlockType buildType,boolean noSolid){
        return BLOCKS.register(name, new DecoBlockFactory.Builder().setProperties(propertyType.getProperties().noOcclusion()).build(buildType));
    }

    public static RegistryObject<Block> registerDecoStairBlock(String name,RegistryObject<Block> baseBlock, DecoBlockFactory.DecoPropertyType propertyType){
        return BLOCKS.register(name, new DecoBlockFactory.Builder().setProperties(propertyType.getProperties().noOcclusion()).build(baseBlock));
    }
}
