package teamHTBP.vidaReforged.server.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.core.common.system.multiblock.ScheduledBlock;
import teamHTBP.vidaReforged.core.common.system.multiblock.ScheduledMultiBlockJob;
import teamHTBP.vidaReforged.core.common.system.multiblock.ScheduledMultiblockPredefinedTask;
import teamHTBP.vidaReforged.server.packets.MultiBlockSchedulerPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;
import teamHTBP.vidaReforged.server.providers.ScheduledMultiblockPredefinedTaskManager;

import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber()
public class VidaMultiBlockHandler {

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event){
        if(event.phase != TickEvent.Phase.START){
            return;
        }

        List<ScheduledMultiBlockJob> jobs = new LinkedList<>();
        event.level.getCapability(VidaCapabilityRegisterHandler.VIDA_MULTI_BLOCK).ifPresent(cap -> jobs.addAll(cap.getJobs()));
        // 定时处理任务
        for(ScheduledMultiBlockJob job : jobs){
            BlockPos startPos = job.getStartPos();
            Level level = event.level;
            // 检测区块是否加载
            if(!level.isLoaded(startPos)){
                continue;
            }
            // 每5秒验证
            if(level.getGameTime() % 100 == 0 && !handleValidate(level, job)){
                event.level.getCapability(VidaCapabilityRegisterHandler.VIDA_MULTI_BLOCK).ifPresent(cap -> cap.removeJob(job));
                continue;
            }
            // 如果现在游戏刻没有到达job执行时间，跳过
            if(job.getStartJobTime() > 0 && job.getTickLength() > 0 && level.getGameTime() < job.getStartJobTime() + job.getTickLength()){
                continue;
            }

            // 服务端更新方块
            if(!event.level.isClientSide && handleValidate(level, job)) {
                handleUpdateBlock(level, job);
            }
            // 客户端/服务端移除效果
            event.level.getCapability(VidaCapabilityRegisterHandler.VIDA_MULTI_BLOCK).ifPresent(cap -> cap.removeJob(job));
        }
    }


    public static void handleUpdateBlock(Level level, ScheduledMultiBlockJob job){
        if(job.getResult().size() == 0){
            return;
        }
        for(ScheduledBlock block : job.getMutiBlockPos()){
            level.destroyBlock(block.targetBlockPos(), true);
        }

        for(ScheduledBlock result : job.getResult()){
            level.setBlockAndUpdate(result.targetBlockPos(), result.getTargetBlock());
        }
    }

    public static boolean handleValidate(Level level, ScheduledMultiBlockJob job){
        for(ScheduledBlock block : job.getMutiBlockPos()){
            if(!level.getBlockState(block.targetBlockPos()).equals(block.getTargetBlock())){
                return false;
            }
        }
        return true;
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        if(event.getEntity() instanceof ServerPlayer player){
            Level level = player.getCommandSenderWorld();
            if(level.isClientSide()){
                return;
            }

            level.getCapability(VidaCapabilityRegisterHandler.VIDA_MULTI_BLOCK).ifPresent(cap -> {
                List<ScheduledMultiBlockJob> jobs = cap.getJobNearByPlayer(player, 100);
                VidaPacketManager.sendToPlayer(new MultiBlockSchedulerPacket(jobs, level.dimension()),  player);
            });
        }
    }


    public static ScheduledMultiBlockJob validateAndAddJob(Level level, BlockPos startPos, BlockState startPosBlock){
        List<ScheduledMultiblockPredefinedTask> preTasks = ScheduledMultiblockPredefinedTaskManager.TASK.values().stream().toList();
        BlockPos start = startPos.immutable();
        ScheduledMultiBlockJob job = ScheduledMultiBlockJob.singleBlockJob(level, startPos, 200, null);
        for(ScheduledMultiblockPredefinedTask preTask : preTasks){
            if(!preTask.getStartState().equals(startPosBlock)){
                continue;
            }
            boolean isSame = true;
            for(ScheduledBlock block : preTask.getRequiredBlocks()){
                BlockPos offsetPos = block.targetBlockPos().offset(start);
                if(!level.getBlockState(offsetPos).equals(block.getTargetBlock())){
                    isSame = false;
                }
            }

            if(isSame){
                job = ScheduledMultiBlockJob.preJob(level, start, preTask);
            }
        }

        return job;
    }
}
