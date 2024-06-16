package teamHTBP.vidaReforged.server.levelgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.material.Fluids;
import teamHTBP.vidaReforged.server.levelgen.VidaLevelGenerationLoader;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class VidaGlowTrunkPlacer extends TrunkPlacer {
    public static final Codec<VidaGlowTrunkPlacer> CODEC = RecordCodecBuilder.create((p_259008_) -> {
        return trunkPlacerParts(p_259008_).and(p_259008_.group(SimpleStateProvider.CODEC.fieldOf("glow_provider").forGetter((p_226242_) -> {
            return p_226242_.glowBlock;
        }), Codec.BOOL.fieldOf("isGenerateGlowBlock").orElse(true).forGetter((p_226240_) -> {
            return p_226240_.isGenerateGlowBlock;
        }))).apply(p_259008_, VidaGlowTrunkPlacer::new);
    });
    public final boolean isGenerateGlowBlock;
    public final SimpleStateProvider glowBlock;
    
    public VidaGlowTrunkPlacer(int baseHeight, int heightA, int heightB, SimpleStateProvider provider, boolean isGenerateGlowBlock) {
        super(baseHeight, heightA, heightB);
        this.glowBlock = provider;
        this.isGenerateGlowBlock = isGenerateGlowBlock;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return VidaLevelGenerationLoader.VIDA_TREE_GLOW_TRUNK_TYPE.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, int height, BlockPos startPos, TreeConfiguration config) {
        setDirtAt(reader, replacer, random, startPos.below(), config);
        int offset = 0;
        for(int i = 0; i < heightRandA; ++i) {
            this.placeLog(reader, replacer, random, startPos.above(i), config);
        }

        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        // 随机摆放树叶
        tryPlaceLeaf(reader, replacer, random, config, startPos.above(heightRandA - 1).offset(direction.getStepX(), 0, direction.getStepZ()));
        tryPlaceLeaf(reader, replacer, random, config, startPos.above(heightRandA + 1).offset(direction.getStepX(), 0, direction.getStepZ()));

        if(isGenerateGlowBlock){
            placeGlowBlock(reader, replacer, random, startPos.above(heightRandA));
            offset = 1;
        }

        for(int i = 0; i < heightRandB; ++i) {
            this.placeLog(reader, replacer, random, startPos.above(heightRandA + offset).above(i), config);
        }

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(startPos.above(heightRandA + heightRandB + offset), 0, false));
    }

    protected boolean placeGlowBlock(LevelSimulatedReader p_226176_, BiConsumer<BlockPos, BlockState> placer, RandomSource randomSource, BlockPos pos) {
        if (this.validTreePos(p_226176_, pos)) {
            placer.accept(pos, glowBlock.getState(randomSource, pos));
            return true;
        } else {
            return false;
        }
    }

    private static boolean tryPlaceLeaf(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, TreeConfiguration config, BlockPos pos) {
        if (!TreeFeature.validTreePos(level, pos) || !random.nextBoolean()) {
            return false;
        } else {
            BlockState blockstate = config.foliageProvider.getState(random, pos);
            if (blockstate.hasProperty(BlockStateProperties.WATERLOGGED)) {
                blockstate = blockstate.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(level.isFluidAtPosition(pos, (prop) -> {
                    return prop.isSourceOfType(Fluids.WATER);
                })));
            }

            replacer.accept(pos, blockstate);
            return true;
        }
    }
}
