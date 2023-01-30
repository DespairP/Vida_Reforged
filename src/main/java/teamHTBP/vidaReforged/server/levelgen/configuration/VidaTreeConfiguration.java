package teamHTBP.vidaReforged.server.levelgen.configuration;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class VidaTreeConfiguration extends TreeConfiguration {
    /**额外原木*/
    public BlockStateProvider alterTrunkProvider;
    /**额外树叶*/
    public BlockStateProvider alterFoliageProvider;
    /**最小尺寸*/
    public Integer minSize;
    /**最大尺寸*/
    public Integer maxSize;
    /**半径*/
    public Integer radius;

    public VidaTreeConfiguration(
            BlockStateProvider pTrunkProvider,
            BlockStateProvider pAltTrunkProvider,
            BlockStateProvider pFoliageProvider,
            BlockStateProvider pAltFoliageProvider,
            int minSize,
            int maxSize,
            int radius
    ) {
        super(
                pTrunkProvider,                             //原木方块
                null,                                       //原木placer
                pFoliageProvider,                           //树叶方块
                null,                                       //树叶placer
                BlockStateProvider.simple(Blocks.DIRT),     //底座方块
                new TwoLayersFeatureSize(1,minSize,maxSize),//最小尺寸
                Lists.newArrayList(),                       //decorator
                false,                                      //不生成藤蔓
                false                                       //生成时是否强制需要泥土方块
        );
        this.alterTrunkProvider = pAltTrunkProvider;
        this.alterFoliageProvider = pAltFoliageProvider;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.radius = radius;
    }


    public static class VidaTreeConfigurationBuilder{

    }



}
