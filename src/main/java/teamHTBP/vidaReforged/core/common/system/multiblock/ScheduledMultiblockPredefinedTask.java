package teamHTBP.vidaReforged.core.common.system.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.VidaReforged;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduledMultiblockPredefinedTask {
    private ResourceLocation id = new ResourceLocation(VidaReforged.MOD_ID, "muti_block_task/null");
    private List<ScheduledBlock> requiredBlocks = new ArrayList<>();
    private List<ScheduledBlock> resultBlocks = new ArrayList<>();
    private BlockState startState;
    private long tickLength = 20;


    public ResourceLocation getId() {
        return id;
    }

    public List<ScheduledBlock> getRequiredBlocks() {
        return requiredBlocks;
    }

    public List<ScheduledBlock> getResultBlocks() {
        return resultBlocks;
    }

    public long getTickLength() {
        return tickLength;
    }


    public ScheduledMultiblockPredefinedTask(BlockState startState) {
        this.startState = startState;
    }

    public ScheduledMultiblockPredefinedTask(ResourceLocation id, List<ScheduledBlock> requiredBlocks, List<ScheduledBlock> resultBlocks, BlockState startState, long tickLength) {
        this.id = id;
        this.requiredBlocks = requiredBlocks;
        this.resultBlocks = resultBlocks;
        this.startState = startState;
        this.tickLength = tickLength;
    }

    public BlockState getStartState() {
        return startState;
    }

    public static class TaskBuilder{
        ResourceLocation id;
        List<ScheduledBlock> requiredBlocks = new ArrayList<>();
        List<ScheduledBlock> resultBlocks = new ArrayList<>();
        BlockState startState;
        long time;

        public TaskRequiredBlocksBuilder start(ResourceLocation id, BlockState startBlock){
            this.id = id;
            this.requiredBlocks.add(new ScheduledBlock(startBlock, BlockPos.ZERO));
            TaskRequiredBlocksBuilder requiredBuilder = new TaskRequiredBlocksBuilder(this);
            this.startState = startBlock;
            return requiredBuilder;
        }


        public void setRequiredBlocks(List<ScheduledBlock> blocks){
            this.requiredBlocks.addAll(blocks);
        }


        public void setResultBlocks(List<ScheduledBlock> blocks){
            this.resultBlocks.addAll(blocks);
        }


        public TaskBuilder ticks(long time){
            this.time = time;
            return this;
        }

        public TaskResultBuilder result(){
            TaskResultBuilder resultBuilder = new TaskResultBuilder(this);
            return resultBuilder;
        }


        public ScheduledMultiblockPredefinedTask build(){
            return new ScheduledMultiblockPredefinedTask(id, requiredBlocks, resultBlocks, startState, time);
        }
    }

    public static class TaskRequiredBlocksBuilder{
        Map<BlockPos, ScheduledBlock> requiredBlocks = new HashMap<>();
        BlockPos lastAddPos = BlockPos.ZERO;
        TaskBuilder parent;

        public TaskRequiredBlocksBuilder(TaskBuilder parent) {
            this.parent = parent;
        }

        public TaskRequiredBlocksBuilder addValidateBlock(BlockState state, BlockPos offset){
            requiredBlocks.put(offset, new ScheduledBlock(state ,offset));
            lastAddPos = offset;
            return this;
        }

        public TaskBuilder end(){
            parent.setRequiredBlocks(requiredBlocks.values().stream().toList());
            return parent;
        }
    }

    public static class TaskResultBuilder{
        Map<BlockPos, ScheduledBlock> resultBlocks = new HashMap<>();
        TaskBuilder parent;

        public TaskResultBuilder(TaskBuilder parent) {
            this.parent = parent;
        }

        public TaskResultBuilder addResultBlock(BlockState state, BlockPos offset){
            resultBlocks.put(offset, new ScheduledBlock(state ,offset));
            return this;
        }

        public TaskBuilder end(){
            parent.setResultBlocks(resultBlocks.values().stream().toList());
            return parent;
        }
    }


}

