package teamHTBP.vidaReforged.server.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import teamHTBP.vidaReforged.server.levelgen.configuration.VidaBasicTreeConfiguration;

import java.util.Random;
import java.util.function.BiConsumer;

public class VidaBasicTreeFeature extends VidaTreeFeature<VidaBasicTreeConfiguration>{

    public VidaBasicTreeFeature(Codec<VidaBasicTreeConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean doPlaceTree(WorldGenLevel pLevel, Random pRandom, BlockPos pPos, BiConsumer<BlockPos, BlockState> pTrunkBlockSetter, BiConsumer<BlockPos, BlockState> pFoliageBlockSetter, TreeConfiguration pConfig) {
        //获取config
        VidaBasicTreeConfiguration treeConfig = (VidaBasicTreeConfiguration) pConfig;
        //半径
        int radius = treeConfig.radius;
        int height = treeConfig.minSize + pRandom.nextInt(1 + treeConfig.maxSize - treeConfig.minSize);
        //如果有方块在生成区域内，提示不能生成
        if(!checkSpace(pLevel,pPos,height,radius)){
            return false;
        }


        return true;
    }





}
