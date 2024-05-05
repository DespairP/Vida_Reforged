package teamHTBP.vidaReforged.core.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import teamHTBP.vidaReforged.core.common.system.multiblock.ScheduledMultiBlockJob;

import java.util.LinkedList;
import java.util.List;

public interface IVidaMultiBlockCapability extends INBTSerializable<CompoundTag> {

    public LinkedList<ScheduledMultiBlockJob> getJobs();

    public void addJob(ScheduledMultiBlockJob job);

    public void setJobs(List<ScheduledMultiBlockJob> jobs);

    public List<ScheduledMultiBlockJob> getJobNearByPlayer(Player player, int dist);

    public boolean removeJob(ScheduledMultiBlockJob job);
}
