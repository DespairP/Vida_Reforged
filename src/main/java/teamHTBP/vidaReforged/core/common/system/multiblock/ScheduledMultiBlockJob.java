package teamHTBP.vidaReforged.core.common.system.multiblock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**预定要开始的工作*/
public class ScheduledMultiBlockJob {
    ResourceKey<Level> level = Level.OVERWORLD;
    /**开始位置*/
    protected BlockPos startPos = BlockPos.ZERO;
    /**所有方块位置*/
    protected List<ScheduledBlock> mutiBlockPos = new ArrayList<>();
    /**开始时间*/
    protected long startJobTime;
    /***/
    protected long tickLength;
    /***/
    protected List<ScheduledBlock> result = new ArrayList<>();


    public static final Codec<ScheduledMultiBlockJob> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Level.RESOURCE_KEY_CODEC.fieldOf("level").orElse(Level.OVERWORLD).forGetter(ScheduledMultiBlockJob::getLevel),
            BlockPos.CODEC.fieldOf("startPos").orElse(BlockPos.ZERO).forGetter(ScheduledMultiBlockJob::getStartPos),
            ScheduledBlock.CODEC.listOf().fieldOf("mutiBlockPos").orElseGet(ArrayList::new).forGetter(ScheduledMultiBlockJob::getMutiBlockPos),
            Codec.LONG.fieldOf("startJobTime").forGetter(ScheduledMultiBlockJob::getStartJobTime),
            Codec.LONG.fieldOf("tickLength").forGetter(ScheduledMultiBlockJob::getTickLength),
            ScheduledBlock.CODEC.listOf().fieldOf("result").orElseGet(ArrayList::new).forGetter(ScheduledMultiBlockJob::getResult)
    ).apply(ins, ScheduledMultiBlockJob::new));


    protected ScheduledMultiBlockJob(ResourceKey<Level> level, BlockPos startPos, long startJobTime, long tickLength){
        this.startPos = startPos;
        this.startJobTime = startJobTime;
        this.tickLength = tickLength;
    }

    public ScheduledMultiBlockJob(ResourceKey<Level> level, BlockPos startPos, List<ScheduledBlock> mutiBlockPos, long startJobTime, long tickLength, List<ScheduledBlock> result) {
        this.level = level;
        this.startPos = startPos;
        this.mutiBlockPos = mutiBlockPos;
        this.startJobTime = startJobTime;
        this.tickLength = tickLength;
        this.result = result;
    }

    public static ScheduledMultiBlockJob singleBlockJob(Level level, BlockPos startPos, long timeLength, List<ScheduledBlock> blockResult){
        ScheduledMultiBlockJob job = new ScheduledMultiBlockJob(level.dimension(), startPos, level.getGameTime(), timeLength);
        ScheduledBlock blockJob = new ScheduledBlock(level.getBlockState(startPos),  startPos);
        job.mutiBlockPos.add(blockJob);
        if(blockResult != null){
            job.result.addAll(blockResult);
        }

        return job;
    }


    public static ScheduledMultiBlockJob preJob(Level level, BlockPos startPos, ScheduledMultiblockPredefinedTask task){
        ScheduledMultiBlockJob newJob = new ScheduledMultiBlockJob(level.dimension(), startPos, level.getGameTime(), task.getTickLength());
        BlockPos pos = startPos.immutable();
        for(ScheduledBlock block : task.getRequiredBlocks()){
            newJob.mutiBlockPos.add(new ScheduledBlock(block.targetBlockState(), pos.offset(block.getTargetPos())));
        }
        for(ScheduledBlock block : task.getResultBlocks()){
            newJob.result.add(new ScheduledBlock(block.targetBlockState(), pos.offset(block.getTargetPos())));
        }

        return newJob;
    }


    public ResourceKey<Level> getLevel() {
        return level;
    }

    public BlockPos getStartPos() {
        return startPos;
    }

    public List<ScheduledBlock> getMutiBlockPos() {
        return mutiBlockPos;
    }

    public long getTickLength() {
        return tickLength;
    }

    public long getStartJobTime() {
        return startJobTime;
    }

    public List<ScheduledBlock> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ScheduledMutiBlockJob{" +
                "level=" + level +
                ", startPos=" + startPos +
                ", mutiBlockPos=" + mutiBlockPos +
                ", startJobTime=" + startJobTime +
                ", tickLength=" + tickLength +
                ", result=" + result +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ScheduledMultiBlockJob job) {
            return level.equals(job.getLevel()) && startPos.equals(job.startPos);
        }

        return false;
    }

}
