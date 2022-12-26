package teamHTBP.vidaReforged.core.common.block;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class DecoStairBlock extends StairBlock {

    public DecoStairBlock(Supplier<BlockState> state, Properties properties) {
        super(state, properties);
    }
}
