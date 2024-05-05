package teamHTBP.vidaReforged.core.common.system.multiblock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record ScheduledBlock(BlockState targetBlockState, BlockPos targetBlockPos) {

    public static final Codec<ScheduledBlock> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            BlockState.CODEC.fieldOf("targetBlockState").orElseGet(Blocks.AIR::defaultBlockState).forGetter(ScheduledBlock::getTargetBlock),
            BlockPos.CODEC.fieldOf("targetBlockPos").orElse(BlockPos.ZERO).forGetter(ScheduledBlock::getTargetPos)
    ).apply(ins, ScheduledBlock::new));


    public BlockPos getTargetPos() {
        return targetBlockPos;
    }

    public BlockState getTargetBlock() {
        return targetBlockState;
    }
}
