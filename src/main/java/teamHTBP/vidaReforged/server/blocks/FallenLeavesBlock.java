package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FallenLeavesBlock extends CarpetBlock {
    public FallenLeavesBlock(Properties p_152915_) {
        super(p_152915_);
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND);
    }


    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos p_51030_) {
        BlockPos blockpos = p_51030_.below();
        if (state.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return mayPlaceOn(reader.getBlockState(blockpos), reader, blockpos);
        return this.mayPlaceOn(reader.getBlockState(blockpos), reader, blockpos);
    }

}
