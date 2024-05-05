package teamHTBP.vidaReforged.server.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.api.capability.IVidaMultiBlockCapability;
import teamHTBP.vidaReforged.core.common.system.multiblock.ScheduledMultiBlockJob;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/***/
public class VidaMultiBlockCapability implements IVidaMultiBlockCapability, INBTSerializable<CompoundTag> {
    LinkedList<ScheduledMultiBlockJob> jobQueue = new LinkedList<>();
    Logger LOGGER = LogManager.getLogger();

    public static final Codec<VidaMultiBlockCapability> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            ScheduledMultiBlockJob.CODEC.listOf().fieldOf("jobQueue").forGetter(VidaMultiBlockCapability::getJobs)
    ).apply(ins, VidaMultiBlockCapability::new));


    public VidaMultiBlockCapability(List<ScheduledMultiBlockJob> queue) {
        this.jobQueue = new LinkedList<>();
        this.jobQueue.addAll(queue);
    }


    @Override
    public void addJob(ScheduledMultiBlockJob job) {
        this.jobQueue.add(job);
    }

    public void set(VidaMultiBlockCapability capability) {
        this.jobQueue = new LinkedList<>();
        this.jobQueue.addAll(capability.jobQueue);
    }


    @Override
    public LinkedList<ScheduledMultiBlockJob> getJobs() {
        return jobQueue;
    }


    @Override
    public void setJobs(List<ScheduledMultiBlockJob> jobs) {
        this.jobQueue.clear();
        this.jobQueue.addAll(jobs);
    }

    @Override
    public boolean removeJob(ScheduledMultiBlockJob job) {
        return jobQueue.remove(job);
    }

    @Override
    public List<ScheduledMultiBlockJob> getJobNearByPlayer(Player player, int dist) {
        Vec3 playerPos = player.getPosition(0);
        ResourceKey<Level> dim = player.getCommandSenderWorld().dimension();
        return jobQueue.stream().filter(job -> dim.equals(job.getLevel()) && playerPos.distanceToSqr(playerPos) < dist).collect(Collectors.toList());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        try {
            tag = (CompoundTag) CODEC.encode(this, NbtOps.INSTANCE, tag).result().get();
        } catch (Exception ex){
            LOGGER.error(ex);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        try {
            VidaMultiBlockCapability cap = CODEC.parse(NbtOps.INSTANCE, nbt).get().orThrow();
            set(cap);
        } catch (Exception ex){
            LOGGER.error(ex);
        }
    }
}
