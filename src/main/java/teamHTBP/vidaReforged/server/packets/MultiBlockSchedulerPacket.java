package teamHTBP.vidaReforged.server.packets;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.common.system.multiblock.ScheduledBlock;
import teamHTBP.vidaReforged.core.common.system.multiblock.ScheduledMultiBlockJob;

import java.util.*;
import java.util.function.Supplier;

public class MultiBlockSchedulerPacket {
    List<ScheduledMultiBlockJob> jobs = new LinkedList<>();
    ResourceKey<Level> level = Level.OVERWORLD;

    public static final Logger LOGGER = LogManager.getLogger();

    public MultiBlockSchedulerPacket(List<ScheduledMultiBlockJob> jobs, ResourceKey<Level> level) {
        this.jobs = jobs;
        this.level = level;
    }

    public static MultiBlockSchedulerPacket fromBytes(FriendlyByteBuf buffer){
        try {
            ResourceKey<Level> level = buffer.readResourceKey(Registries.DIMENSION);
            int size = buffer.readInt();
            List<ScheduledMultiBlockJob> jobs = new LinkedList<>();
            for(int i = 0; i < size; i++){
                jobs.add(buffer.readJsonWithCodec(ScheduledMultiBlockJob.CODEC));
            }
            return new MultiBlockSchedulerPacket( jobs , level);
        }catch (Exception ex){
            LOGGER.error("Multiblock packet error:" + ex.getMessage());
        }
        return new MultiBlockSchedulerPacket( new LinkedList<>(), Level.OVERWORLD);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeResourceKey(level);
        buffer.writeInt(jobs.size());
        for(ScheduledMultiBlockJob job : jobs){
            buffer.writeJsonWithCodec(ScheduledMultiBlockJob.CODEC, job);
        }
    }


    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                VidaPacketClientHandler.handleMultiBlockSchedulerPacket(this);
            }catch (Exception ex){
                LOGGER.error("MagicWord packet error:" + ex.getMessage());
            }
        });
        ctx.get().setPacketHandled(true);
    }



}
