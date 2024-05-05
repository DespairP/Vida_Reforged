package teamHTBP.vidaReforged.client.events;


import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.core.common.system.multiblock.ScheduledMultiBlockJob;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.TwoValueGradientColor;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

import java.util.LinkedList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class VidaMultiBlockClientHandler {
    public static List<Vector3f> edges = null;


    @SubscribeEvent
    public static void onClientLevelTick(TickEvent.LevelTickEvent event){
        if(event.side != LogicalSide.CLIENT){
            return;
        }
        handle(event.level);
    }


    public static void handle(Level level){
        List<ScheduledMultiBlockJob> jobs = new LinkedList<>();
        level.getCapability(VidaCapabilityRegisterHandler.VIDA_MULTI_BLOCK).ifPresent(cap -> jobs.addAll(cap.getJobs()));
        for(ScheduledMultiBlockJob job : jobs){
            if(!level.dimension().equals(job.getLevel())){
                continue;
            }

            generateParticleFromJob(level, job.getStartPos(), RandomSource.createNewThreadLocalInstance());
        }
    }


    public static void generateParticleFromJob(Level level, BlockPos pos, RandomSource random) {
        List<Vector3f> edges = getOrCreateEdges();
        int index = random.nextInt(edges.size());
        Vector3f randomPos = edges.get(index);
        TwoValueGradientColor gradientColor = new TwoValueGradientColor(ARGBColor.argb(0xfff5f7fa), ARGBColor.argb(0xffc3cfe2));

        ARGBColor color = gradientColor.getColor(random.nextFloat());

        level.addParticle(new BaseParticleType(
                VidaParticleTypeLoader.TICKLE_PARTICLE.get(),
                color,
                new Vector3f(),
                0.06f,
                40
        ), randomPos.x + pos.getX(), randomPos.y + pos.getY(), randomPos.z + pos.getZ(), 0, 0, 0);

    }


    public static List<Vector3f> getOrCreateEdges(){
        if(edges == null){
            VidaMultiBlockClientHandler.edges = getEdges(0.02);
        }
        return edges;
    }



    public static List<Vector3f> getEdges(double particleStep) {
        List<Vector3f> result = Lists.newArrayList();
        double minX = -0.02;
        double minY = -0.02;
        double minZ = -0.02;
        double maxX = 1.04;
        double maxY = 1.04;
        double maxZ = 1.04;

        for (double x = minX; x <= maxX; x = Math.round((x + particleStep) * 1e2) / 1e2) {
            for (double y = minY; y <= maxY; y = Math.round((y + particleStep) * 1e2) / 1e2) {
                for (double z = minZ; z <= maxZ; z = Math.round((z + particleStep) * 1e2) / 1e2) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Vector3f((float) x, (float) y, (float) z));
                    }
                }
            }
        }
        return result;
    }
}
