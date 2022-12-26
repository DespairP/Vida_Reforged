package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.common.block.DecoBlockFactory;
import teamHTBP.vidaReforged.core.utils.reg.RegisterItemBlock;

import static teamHTBP.vidaReforged.core.common.block.DecoBlockFactory.DecoBlockType.NORMAL;
import static teamHTBP.vidaReforged.core.common.block.DecoBlockFactory.DecoBlockType.SLAB;
import static teamHTBP.vidaReforged.core.common.block.DecoBlockFactory.DecoPropertyType.*;
/**
 * 方块注册管理类
 *
 * */
public class VidaBlockLoader {
    /**注册器*/
    public final static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VidaReforged.MOD_ID);
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


    public static Block registerBlock(String name,Block block){
        RegistryObject<Block> regBlock = BLOCKS.register(name, () -> block);
        return regBlock.get();
    }

    public static RegistryObject<Block> registerDecoBlock(String name, DecoBlockFactory.DecoPropertyType propertyType, DecoBlockFactory.DecoBlockType buildType){
        RegistryObject<Block> regBlock = BLOCKS.register(name, new DecoBlockFactory.Builder().setProperties(propertyType).build(buildType));
        return regBlock;
    }
}
