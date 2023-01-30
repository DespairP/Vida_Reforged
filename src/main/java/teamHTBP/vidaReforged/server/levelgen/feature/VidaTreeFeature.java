package teamHTBP.vidaReforged.server.levelgen.feature;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import teamHTBP.vidaReforged.server.levelgen.configuration.VidaTreeConfiguration;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Vida mod的树抽象类
 * */
public abstract class VidaTreeFeature<FC extends VidaTreeConfiguration> extends TreeFeature {
    public VidaTreeFeature(Codec<FC> codec) {
        super((Codec)codec);
    }

    /**
     * 检查生成时候如果有方块再生成区域内是否能替代，来自BOP代码
     *
     * @param level 所处世界
     * @param pos 需要代替的位置
     * */
    protected boolean canReplace(LevelAccessor level, BlockPos pos) {
        return TreeFeature.isAirOrLeaves(level, pos) || level.isStateAtPosition(pos, (state) -> {
            Material material = state.getMaterial();
            Block block = state.getBlock();
            //只有标记了树苗，可被替代的，藤蔓，菌丝，树丛方块可以被替代
            return material == Material.REPLACEABLE_PLANT || state.is(BlockTags.SAPLINGS) || block == Blocks.VINE || block == Blocks.MOSS_CARPET || block instanceof BushBlock;
        });
    }

    /**
     * 摆放树叶，来自BOP代码
     *
     * @param level 所处世界
     * @param pos 放置位置
     * @param leaves 树叶摆放函数，需要提供pos和blockstate
     * @param config 此树摆放特性entity
     *
     * @return 是否摆放成功
     * */
    public boolean placeLeaves(LevelAccessor level, BlockPos pos, BiConsumer<BlockPos, BlockState> leaves, FC config) {
        if (canReplace(level, pos)) {
            leaves.accept(pos, config.foliageProvider.getState(level.getRandom(), pos));
            return true;
        }
        return false;
    }

    /**
     * 摆放第二品种树叶，来自BOP代码
     *
     * @param level 所处世界
     * @param pos 放置位置
     * @param leaves 树叶摆放函数，需要提供pos和blockstate
     * @param config 此树摆放特性entity
     *
     * @return 是否摆放成功
     * */
    public boolean placeAltLeaves(LevelAccessor level, BlockPos pos, BiConsumer<BlockPos, BlockState> leaves, FC config) {
        if (canReplace(level, pos)) {
            leaves.accept(pos, config.alterFoliageProvider.getState(level.getRandom(), pos));
            return true;
        }
        return false;
    }

    /**
     * 摆放原木
     *
     * @param level 所处世界
     * @param pos 放置位置
     * @param logs 原木摆放函数，需要提供pos和blockstate
     * @param config 此树摆放特性entity
     *
     * @return 是否摆放成功
     * */
    public boolean placeLog(LevelAccessor level, BlockPos pos, BiConsumer<BlockPos, BlockState> logs, FC config) {
        return this.placeLog(level, pos, null, logs, config);
    }

    /**
     * 摆放原木，但是原木摆放时的旋转方向不同
     *
     * @param level 所处世界
     * @param pos 放置位置
     * @param axis 对应的BlockState
     * @param logs 原木摆放函数，需要提供pos和blockstate
     * @param config 此树摆放特性entity
     *
     * @return 是否摆放成功
     * */
    public boolean placeLog(LevelAccessor level, BlockPos pos, Direction.Axis axis, BiConsumer<BlockPos, BlockState> logs, FC config) {
        Property logAxisProperty = this.getLogAxisProperty(level, pos, config);
        BlockState log = config.trunkProvider.getState(level.getRandom(), pos);
        BlockState directedLog = (axis != null && logAxisProperty != null) ? log.setValue(logAxisProperty, axis) : log;

        if (canReplace(level, pos)) {
            logs.accept(pos, directedLog);
            return true;
        }
        return false;
    }

    /**获取原木的Axis BlockState*/
    protected Property getLogAxisProperty(LevelAccessor level, BlockPos pos, FC config) {
        BlockState log = config.trunkProvider.getState(level.getRandom(), pos);

        for (Property property : log.getProperties()) {
            Collection allowedValues = property.getPossibleValues();
            if (allowedValues.contains(Direction.Axis.X) && allowedValues.contains(Direction.Axis.Y) && allowedValues.contains(Direction.Axis.Z)) {
                return property;
            }
        }
        return null;
    }

    /**
     * 放置树
     * */
    @Override
    public boolean place(TreeConfiguration pConfig, WorldGenLevel pLevel, ChunkGenerator pChunkGenerator, Random pRandom, BlockPos pOrigin) {
        return pLevel.ensureCanWrite(pOrigin) && this.placeTree(new FeaturePlaceContext<>(Optional.empty(), pLevel, pChunkGenerator, pRandom, pOrigin, pConfig));
    }

    /**/
    public boolean placeTree(FeaturePlaceContext<TreeConfiguration> pContext){
        WorldGenLevel worldgenlevel = pContext.level();
        Random random = pContext.random();
        BlockPos blockpos = pContext.origin();
        TreeConfiguration treeconfiguration = pContext.config();
        Set<BlockPos> set = Sets.newHashSet();
        Set<BlockPos> set1 = Sets.newHashSet();
        Set<BlockPos> set2 = Sets.newHashSet();
        //trunk consumer
        BiConsumer<BlockPos, BlockState> biconsumer = (p_160555_, p_160556_) -> {
            set.add(p_160555_.immutable());
            worldgenlevel.setBlock(p_160555_, p_160556_, 19);
        };
        //foliage consumer
        BiConsumer<BlockPos, BlockState> biconsumer1 = (p_160548_, p_160549_) -> {
            set1.add(p_160548_.immutable());
            worldgenlevel.setBlock(p_160548_, p_160549_, 19);
        };
        //tree decorator consumer?
        BiConsumer<BlockPos, BlockState> biconsumer2 = (p_160543_, p_160544_) -> {
            set2.add(p_160543_.immutable());
            worldgenlevel.setBlock(p_160543_, p_160544_, 19);
        };
        boolean flag = this.doPlaceTree(worldgenlevel, random, blockpos, biconsumer, biconsumer1, treeconfiguration);
        if (flag && (!set.isEmpty() || !set1.isEmpty())) {
            if (!treeconfiguration.decorators.isEmpty()) {
                List<BlockPos> list = Lists.newArrayList(set);
                List<BlockPos> list1 = Lists.newArrayList(set1);
                list.sort(Comparator.comparingInt(Vec3i::getY));
                list1.sort(Comparator.comparingInt(Vec3i::getY));
                treeconfiguration.decorators.forEach((p_160528_) -> {
                    p_160528_.place(worldgenlevel, biconsumer2, random, list, list1);
                });
            }

            return BoundingBox.encapsulatingPositions(Iterables.concat(set, set1, set2)).map((p_160521_) -> {
                //DiscreteVoxelShape discretevoxelshape = updateLeaves(worldgenlevel, p_160521_, set, set2);
                //StructureTemplate.updateShapeAtEdge(worldgenlevel, 3, discretevoxelshape, p_160521_.minX(), p_160521_.minY(), p_160521_.minZ());
                return true;
            }).orElse(false);
        } else {
            return false;
        }
    }

    /**
     * 构造树结构
     *
     * @param pLevel 所处世界
     * @param pRandom 随机
     * @param pPos 开始生成位置
     * @param pTrunkBlockSetter Trunk(原木)放置函数
     * @param pFoliageBlockSetter Foliage(树叶)放置函数
     * @param pConfig 此树摆放特性entity
     * */
    public abstract boolean doPlaceTree(WorldGenLevel pLevel, Random pRandom, BlockPos pPos, BiConsumer<BlockPos, BlockState> pTrunkBlockSetter, BiConsumer<BlockPos, BlockState> pFoliageBlockSetter, TreeConfiguration pConfig);

    /**
     * 确认是否能在这个空间中生成树，来自BOP代码
     *
     * @param world 所处世界
     * @param pos 生成世界
     * @param height 检测高度
     * @param radius 检测半径
     * */
    public boolean checkSpace(LevelAccessor world, BlockPos pos, int height, int radius) {
        for (int y = 0; y <= height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos1 = pos.offset(x, y, z);
                    if (pos1.getY() >= 255 || !this.canReplace(world, pos1)) {
                        return false;
                    }
                }
            }
        }

        BlockPos pos2 = pos.offset(0, height - 2,0);
        return TreeFeature.isAirOrLeaves(world, pos2);
    }

}
