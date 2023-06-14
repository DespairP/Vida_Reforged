package teamHTBP.vidaReforged.core.common.block.templates;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.common.block.DecoBlockProperties;

import java.util.function.Supplier;

public class DecoStairBlock extends StairBlock {

    public DecoStairBlock(Properties properties,DecoBlockProperties decoBlockProperties) {
        super(
                () -> decoBlockProperties.baseBlock().get().defaultBlockState(),
                properties
        );
    }
}
