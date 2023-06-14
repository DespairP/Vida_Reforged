package teamHTBP.vidaReforged.core.common.block;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.core.common.block.templates.*;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 方块工厂类
 * 有关Block.Properties,参见：{@link BlockBehaviour.Properties}<br/>
 */
public class DecoBlockFactory {

    public static class Builder {
        /**
         * 方块所设定的properties
         */
        private BlockBehaviour.Properties properties;
        /**
         * BlockSetType
         */
        private BlockSetType blockSetType;
        /**
         * WoodType
         */
        private WoodType woodType;
        /***/
        private DecoBlockType blockStructureType;
        /**/
        private RegistryObject<Block> baseBlock;

        /**
         * Constructor
         */
        public Builder(BlockBehaviour.Properties properties) {
            this.properties = properties;
        }

        public Builder(DecoPropertyType type) {
            this.properties = type.getProperties();
        }

        public Builder setBlockType(BlockSetType type) {
            this.blockSetType = type;
            return this;
        }

        public Builder setBlockType(WoodType type) {
            this.woodType = type;
            return this;
        }

        public Builder noOcclusion() {
            if (properties != null) {
                this.properties = this.properties.noOcclusion();
            }
            return this;
        }

        public Builder setStructureType(DecoBlockType type) {
            this.blockStructureType = type;
            return this;
        }

        public Builder setBaseBlock(RegistryObject<Block> baseBlock) {
            this.baseBlock = baseBlock;
            return this;
        }

        public Supplier<Block> build() {
            switch (blockStructureType) {
                case DOOR, TRAP_DOOR, BUTTON, PRESSURE_PLATE, FENCE_GATE, STAIRS -> {
                    DecoBlockProperties decoProperties = new DecoBlockProperties(woodType, blockSetType, baseBlock);
                    return () -> blockStructureType.blockBiFunction.apply(properties, decoProperties);
                }

            }
            return () -> blockStructureType.blockFunction.apply(properties);
        }

    }

    /**
     * Block的Property通用提供类
     */
    public enum DecoPropertyType {
        STANDARD(),
        WOOD(),
        STONE(),
        GRASS(),
        IRON(),
        LEAVES();

        private BlockBehaviour.Properties properties;


        public BlockBehaviour.Properties getProperties() {
            switch (this){
                case IRON -> {
                    return BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL);
                }
                case WOOD -> {
                    return BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F, 3.0F).sound(SoundType.WOOD);
                }
                case GRASS -> {
                    return BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS);
                }
                case STONE -> {
                    return BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
                }
                case STANDARD -> {
                    return BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).strength(3.0F);
                }
                case LEAVES -> {
                    return BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion();
                }
            }
            return null;
        }
    }


    public enum DecoBlockType {
        YAXIS(DecoBlockFactory::yAxis),
        NORMAL(DecoBlockFactory::normal),
        FENCE(DecoBlockFactory::fence),
        DOOR(DecoDoorBlock::new),
        SLAB(DecoBlockFactory::slab),
        PLANT(DecoBlockFactory::plant),
        FLOWER(DecoBlockFactory::flower),
        DOUBLE_PLANT(DecoBlockFactory::doublePlant),
        LOG(DecoBlockFactory::log),
        TRAP_DOOR(DecoTrapDoorBlock::new),
        FENCE_GATE(DecoFenceGateBlock::new),
        CARPET(CarpetBlock::new),
        BUTTON(DecoButtonBlock::new),
        PRESSURE_PLATE(DecoPressurePlateBlock::new),
        WALL(DecoWallBlock::new),
        STAIRS(DecoStairBlock::new);

        /**一般方块提供者*/
        private Function<BlockBehaviour.Properties, Block> blockFunction;
        /**复杂方块提供者*/
        private BiFunction<BlockBehaviour.Properties,DecoBlockProperties, Block> blockBiFunction;

        DecoBlockType(Function<BlockBehaviour.Properties, Block> blockFunction) {
            this.blockFunction = blockFunction;
        }

        DecoBlockType(BiFunction<BlockBehaviour.Properties,DecoBlockProperties, Block> blockBiFunction) {
            this.blockBiFunction = blockBiFunction;
        }
    }

    public static Block yAxis(BlockBehaviour.Properties properties) {
        return new DecoWithYBlock(properties);
    }

    public static Block normal(BlockBehaviour.Properties properties) {
        return new DecoBlock(properties);
    }

    public static Block fence(BlockBehaviour.Properties properties) {
        return new DecoFenceBlock(properties);
    }

    public static Block slab(BlockBehaviour.Properties properties) {
        return new DecoSlabBlock(properties);
    }


    public static Block plant(BlockBehaviour.Properties properties) {
        return new DecoBlock(properties);
    }

    private static Block flower(BlockBehaviour.Properties properties) {
        return new DecoFlowerBlock(properties);
    }

    public static Block doublePlant(BlockBehaviour.Properties properties) {
        return new DoublePlantBlock(properties);
    }

    public static Block log(BlockBehaviour.Properties properties) {
        return new RotatedPillarBlock(properties);
    }
}
