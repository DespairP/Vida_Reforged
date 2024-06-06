package teamHTBP.vidaReforged.server.events;


import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.server.mobs.GlowLight;
import teamHTBP.vidaReforged.server.mobs.VidaMobsLoader;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VidaSpawnPlacementHandler {
    @SubscribeEvent
    public static void onRegisterSpawnPlacement(SpawnPlacementRegisterEvent event){
        event.register(VidaMobsLoader.GLOW_LIGHT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GlowLight::checkGlowSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
