package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SearchableBlock extends BrushableBlock {
    public SearchableBlock(Block block, Properties properties, SoundEvent bushSound, SoundEvent bushCompleteSound) {
        super(block ,properties, bushSound, bushCompleteSound);
    }

    public Block getTurnsInto() {
        return Blocks.AIR;
    }

    public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource rand) {
        BlockEntity blockentity = serverLevel.getBlockEntity(pos);
        if (blockentity instanceof BrushableBlockEntity brushableblockentity) {
            brushableblockentity.checkReset();
        }
    }

}
