package teamHTBP.vidaReforged.server.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import teamHTBP.vidaReforged.server.levelgen.VidaLevelGenerationLoader;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FallenLogTrunkPlacer extends TrunkPlacer {
    public static final Codec<FallenLogTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return trunkPlacerParts(instance).apply(instance, FallenLogTrunkPlacer::new);
    });


    @Override
    protected TrunkPlacerType<?> type() {
        return VidaLevelGenerationLoader.FALLEN_TRUNK_TYPE.get();
    }

    public FallenLogTrunkPlacer(int baseHeight, int heightA, int heightB) {
        super(baseHeight, heightA, heightB);
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, int height, BlockPos startPos, TreeConfiguration config) {
        Direction randomDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockPos.MutableBlockPos blockPos$Multiple = startPos.mutable();
        for(int i = 0; i < heightRandA; i ++){
            placeLog(reader, replacer, random, startPos.offset(randomDirection.getNormal().multiply(i)), config, Function.identity(), randomDirection);
        }
        return List.of();
    }

    protected boolean placeLog(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, BlockPos placePos, TreeConfiguration config, Function<BlockState, BlockState> stater, Direction directiond) {
        if (this.validTreePos(reader, placePos)) {
            replacer.accept(placePos, stater.apply(config.trunkProvider.getState(random, placePos).setValue(RotatedPillarBlock.AXIS, directiond.getAxis())));
            return true;
        } else {
            return false;
        }
    }
}
