package teamHTBP.vidaReforged.server.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.util.LazyOptional;
import teamHTBP.vidaReforged.core.api.capability.IVidaMultiBlockCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.events.VidaMultiBlockHandler;
import teamHTBP.vidaReforged.server.packets.MultiBlockSchedulerPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;

public class MultiblockLazerEntity extends LazerEntity{
    public MultiblockLazerEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {

    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        BlockState blockstate = this.level().getBlockState(blockHitResult.getBlockPos());

        LazyOptional<IVidaMultiBlockCapability> capability = this.level().getCapability(VidaCapabilityRegisterHandler.VIDA_MULTI_BLOCK);

        capability.ifPresent(cap -> {
            cap.addJob(VidaMultiBlockHandler.validateAndAddJob(level(), blockHitResult.getBlockPos(), level().getBlockState(blockHitResult.getBlockPos())));
            Player player = level().getNearestPlayer(blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ(), 100.0D, false);
            if (player != null){
                VidaPacketManager.sendToPlayer(new MultiBlockSchedulerPacket(cap.getJobs(), level().dimension()), player);
            }
        });
    }
}
