package teamHTBP.vidaReforged.server.levelgen.configuration;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;

public class VidaBasicTreeConfiguration extends VidaTreeConfiguration{
    public VidaBasicTreeConfiguration() {
        super(
                BlockStateProvider.simple(VidaBlockLoader.VIDA_LOG.get()),
                BlockStateProvider.simple(Blocks.AIR),
                BlockStateProvider.simple(VidaBlockLoader.VIDA_LEAVES.get()),
                BlockStateProvider.simple(Blocks.AIR),
                8,
                15,
                6
        );
    }
}
