package teamHTBP.vidaReforged.server.levelgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

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
        return null;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, int height, BlockPos startPos, TreeConfiguration config) {
        setDirtAt(reader, replacer, random, startPos.below(), config);

        for(int i = 0; i < heightRandA; ++i) {
            this.placeLog(reader, replacer, random, startPos.above(i), config);
        }

        placeGlowBlock(reader, replacer, random, startPos.above(heightRandA));

        for(int i = 0; i < heightRandB; ++i) {
            this.placeLog(reader, replacer, random, startPos.above(heightRandA + 1).above(i), config);
        }

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(startPos.above(heightRandA + heightRandB + 1), 0, false));
    }

    protected boolean placeGlowBlock(LevelSimulatedReader p_226176_, BiConsumer<BlockPos, BlockState> placer, RandomSource randomSource, BlockPos pos) {
        if (this.validTreePos(p_226176_, pos)) {
            placer.accept(pos, glowBlock.getState(randomSource, pos));
            return true;
        } else {
            return false;
        }
    }
}
