package teamHTBP.vidaReforged.core.common.block;


import com.google.common.util.concurrent.ClosingFuture;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 方块工厂类
 * 有关Block.Properties,参见：{@link BlockBehaviour.Properties}<br/>
 *
 */
public class DecoBlockFactory {

    public static class Builder{
        private BlockBehaviour.Properties properties;

        public Builder setProperties(DecoPropertyType type){
            this.properties = type.getProperties();
            return this;
        }

        public Builder setProperties(BlockBehaviour.Properties type){
            this.properties = type;
            return this;
        }

        public Supplier<Block> build(DecoBlockType type){
            return () -> type.blockFunction.apply(properties);
        }

        /**特别为阶梯特制的build*/
        public Supplier<Block> build(RegistryObject<Block> baseBlock){
            BiFunction<RegistryObject<Block>,BlockBehaviour.Properties,Block> blockFunction = DecoBlockFactory::stairs;
            return () -> blockFunction.apply(baseBlock,properties);
        }

    }



    public enum DecoPropertyType{
        STANDARD(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).strength(3.0F)),
        WOOD(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F, 3.0F).sound(SoundType.WOOD)),
        STONE(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)),
        GRASS(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)),
        IRON(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL));

        private BlockBehaviour.Properties properties;


        DecoPropertyType(BlockBehaviour.Properties property){
            this.properties = property;
        }

        public BlockBehaviour.Properties getProperties(){
            return this.properties;
        }
    }


    public enum DecoBlockType{
        YAXIS(DecoBlockFactory::yAxis),
        NORMAL(DecoBlockFactory::normal),
        FENCE(DecoBlockFactory::fence),
        DOOR(DecoBlockFactory::door),
        SLAB(DecoBlockFactory::slab),
        PLANT(DecoBlockFactory::plant),
        FLOWER(DecoBlockFactory::flower),
        DOUBLE_PLANT(DecoBlockFactory::doublePlant),
        LOG(DecoBlockFactory::log),
        TRAP_DOOR(TrapDoorBlock::new);
        private Function<BlockBehaviour.Properties,Block> blockFunction;

        DecoBlockType(Function<BlockBehaviour.Properties,Block> blockFunction) {
            this.blockFunction = blockFunction;
        }
    }

    public static Block yAxis(BlockBehaviour.Properties properties){
        return new DecoWithYBlock(properties);
    }

    public static Block normal(BlockBehaviour.Properties properties){
        return new DecoBlock(properties);
    }

    public static Block fence(BlockBehaviour.Properties properties){
        return new DecoFenceBlock(properties);
    }

    public static Block door(BlockBehaviour.Properties properties){
        return new DecoDoorBlock(properties);
    }

    public static Block slab(BlockBehaviour.Properties properties){
        return new DecoSlabBlock(properties);
    }

    public static Block stairs(RegistryObject<Block> baseBlock, BlockBehaviour.Properties properties){
        return new StairBlock(() -> baseBlock.get().defaultBlockState(), properties);
    }

    public static Block plant(BlockBehaviour.Properties properties){
        return new DecoBlock(properties);
    }

    private static Block flower(BlockBehaviour.Properties properties) {
        return new DecoFlowerBlock(properties);
    }

    public static Block doublePlant(BlockBehaviour.Properties properties){
        return new DoublePlantBlock(properties);
    }

    public static Block log(BlockBehaviour.Properties properties){
        return new RotatedPillarBlock(properties);
    }
}
