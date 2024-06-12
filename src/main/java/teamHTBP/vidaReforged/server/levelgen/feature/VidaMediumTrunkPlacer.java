package teamHTBP.vidaReforged.server.levelgen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.UpwardsBranchingTrunkPlacer;
import net.minecraft.world.level.material.Fluids;
import org.joml.Vector2i;
import teamHTBP.vidaReforged.server.levelgen.VidaLevelGenerationLoader;

import java.util.*;
import java.util.function.BiConsumer;

public class VidaMediumTrunkPlacer extends TrunkPlacer {
    public static final Codec<VidaMediumTrunkPlacer> CODEC = RecordCodecBuilder.create((p_259008_) -> {
        return trunkPlacerParts(p_259008_).and(p_259008_.group(IntProvider.POSITIVE_CODEC.fieldOf("rootMaxNumber").forGetter((p_226242_) -> {
            return p_226242_.rootMaxNumber;
        }), IntProvider.POSITIVE_CODEC.fieldOf("leavesMaxNumber").forGetter((p_226240_) -> {
            return p_226240_.leavesMaxNumber;
        }))).apply(p_259008_, VidaMediumTrunkPlacer::new);
    });

    //
    private final IntProvider rootMaxNumber;
    private final IntProvider leavesMaxNumber;

    /**中间周围所有可能的偏移*/
    private static final List<Vector2i> OFFSETS = List.of(
            new Vector2i(-1, -1), new Vector2i(-1, 0), new Vector2i(-1, 1),
            new Vector2i(0, -1), new Vector2i(0, 1),
            new Vector2i(1, -1), new Vector2i(1, 0), new Vector2i(1, 1)
    );

    /**平移四角的偏移*/
    private static final List<Vector2i> CORNER_OFFSETS = List.of(
             new Vector2i(-1, 0),
             new Vector2i(0, -1), new Vector2i(0, 1),
             new Vector2i(1, 0)
    );


    public VidaMediumTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, IntProvider rootMaxNumber, IntProvider leavesMaxNumber) {
        super(baseHeight, heightRandA, heightRandB);
        this.rootMaxNumber = rootMaxNumber;
        this.leavesMaxNumber = leavesMaxNumber;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return VidaLevelGenerationLoader.VIDA_TREE_TRUNK_PLACER_MID_TYPE.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, int height, BlockPos startPos, TreeConfiguration config) {
        setDirtAt(reader, replacer, random, startPos.below(), config);
        // 生成根部1
        ArrayList<Vector2i> rootOffset = new ArrayList<>(OFFSETS);
        Collections.shuffle(rootOffset);
        List<Vector2i> subRootOffsets = rootOffset.subList(0, rootMaxNumber.sample(random) % rootOffset.size());
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for(Vector2i subOffset : subRootOffsets){
            placeLogIfFreeWithOffset(reader, replacer, random, blockpos$mutableblockpos, config, startPos.below(), subOffset.x, 0, subOffset.y);
        }
        // 生成根部2
        ArrayList<Vector2i> rootOffset$2 = new ArrayList<>(CORNER_OFFSETS);
        Collections.shuffle(rootOffset$2);
        // 随机拼接1与2的shuffle结果
        List<Vector2i> subRootOffsets$2 = rootOffset$2.subList(0, 1);
        subRootOffsets$2.add(rootOffset.get(2));
        for(Vector2i subOffset : subRootOffsets$2){
            placeLogIfFreeWithOffset(reader, replacer, random, blockpos$mutableblockpos, config, startPos, subOffset.x, 0, subOffset.y);
        }

        List<FoliagePlacer.FoliageAttachment> foliageAttachments = new ArrayList<>();
        // 树干部分生成1
        for(int i = 0; i < heightRandA; i ++){
            placeLog(reader, replacer, random, startPos.above(i), config);
        }
        tryPlaceLeaf(reader, replacer, random, config, startPos.above(heightRandA));

        // 生成叶子装饰
        List<Vector2i> midLeaves = new ArrayList<>();
        midLeaves.addAll(OFFSETS.subList(1, 7));
        for(Vector2i mid : midLeaves){
            tryPlaceLeaf(reader, replacer, random, config, startPos.immutable().offset(mid.x, (int) Math.floor(heightRandA / 2.0f), mid.y));
        }

        // 树干部分2
        Direction randDirect = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        for(int i = heightRandA; i < heightRandA + heightRandB; i++){
            placeLogIfFreeWithOffset(reader, replacer, random, blockpos$mutableblockpos, config, startPos, randDirect.getStepX(), i, randDirect.getStepZ());
        }
        // 生成叶子装饰
        tryPlaceLeaf(reader, replacer, random, config, startPos.immutable().offset(2 * randDirect.getStepX(), heightRandA + 2, 2 * randDirect.getStepZ()));
        tryPlaceLeaf(reader, replacer, random, config, startPos.immutable().offset(randDirect.getStepX() + randDirect.getClockWise().getStepX(), heightRandA + 2, randDirect.getStepZ() + randDirect.getClockWise().getStepZ()));

        foliageAttachments.add(new FoliagePlacer.FoliageAttachment(startPos.offset(randDirect.getStepX(),heightRandA + heightRandB - 1, randDirect.getStepZ()), -3, false));
        foliageAttachments.add(new FoliagePlacer.FoliageAttachment(startPos.offset(randDirect.getStepX(),heightRandA + heightRandB, randDirect.getStepZ()), 0, false));

        return foliageAttachments;
    }

    private void placeLogIfFreeWithOffset(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, BlockPos.MutableBlockPos pos, TreeConfiguration config, BlockPos startPos, int x, int y, int z) {
        pos.setWithOffset(startPos, x, y, z);
        this.placeLogIfFree(reader, replacer, random, pos, config);
    }

    protected static boolean tryPlaceLeaf(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, TreeConfiguration config, BlockPos pos) {
        if (!TreeFeature.validTreePos(level, pos)) {
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

    @Override
    protected boolean validTreePos(LevelSimulatedReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, (blockState) -> {
            return blockState.isAir() || blockState.is(BlockTags.REPLACEABLE_BY_TREES) || blockState.is(BlockTags.DIRT);
        });
    }
}
