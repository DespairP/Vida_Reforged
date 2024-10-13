package teamHTBP.vidaReforged.server.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.LazyOptional;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.particles.VidaParticleAttributes;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaMultiBlockCapability;
import teamHTBP.vidaReforged.core.common.system.multiblock.ScheduledMultiBlockJob;
import teamHTBP.vidaReforged.core.utils.color.ColorTheme;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.events.VidaMultiBlockHandler;
import teamHTBP.vidaReforged.server.packets.MultiBlockSchedulerPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;

public class MultiblockSparkEntity extends LazerEntity {
    private static final EntityDataAccessor<VidaElement> ELEMENT = SynchedEntityData.defineId(MultiblockSparkEntity.class, VidaEntityDataSerializer.ELEMENT);

    public MultiblockSparkEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void init(Player player, VidaElement element) {
        init(player);
        this.entityData.set(ELEMENT, element);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ELEMENT, VidaElement.EMPTY);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(ELEMENT, VidaElement.valueOf(tag.getString("element")), true);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("element", this.entityData.get(ELEMENT).name());
    }

    @Override
    public ParticleOptions getParticle() {
        return new BaseParticleType(
                VidaParticleTypeLoader.ORB_PARTICLE,
                new VidaParticleAttributes(20, 0.6F, true, ColorTheme.getColorThemeByElement(getElement()).baseColor().toARGB(), null, new Vector3f(), new Vector3f(1))
        );
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
    }

    public VidaElement getElement() {
        return this.entityData.get(ELEMENT);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        BlockState blockstate = this.level().getBlockState(blockHitResult.getBlockPos());

        LazyOptional<IVidaMultiBlockCapability> capability = this.level().getCapability(VidaCapabilityRegisterHandler.VIDA_MULTI_BLOCK);

        capability.ifPresent(cap -> {
            ScheduledMultiBlockJob job = VidaMultiBlockHandler.validateAndAddJob(level(), blockHitResult.getBlockPos(), level().getBlockState(blockHitResult.getBlockPos()));
            if (job.getResult() == null || job.getResult().size() == 0) {
                return;
            }
            cap.addJob(job);
            Player player = level().getNearestPlayer(blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ(), 100.0D, false);
            if (player != null) {
                VidaPacketManager.sendToPlayer(new MultiBlockSchedulerPacket(cap.getJobs(), level().dimension()), player);
            }
        });

        if (!this.level().isClientSide) {
            discard();
        }
    }
}
