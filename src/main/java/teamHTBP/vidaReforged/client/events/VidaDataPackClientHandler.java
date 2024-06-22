package teamHTBP.vidaReforged.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib.cache.GeckoLibCache;
import teamHTBP.vidaReforged.client.level.VidaBlueFoliageColorReloadListener;
import teamHTBP.vidaReforged.client.level.VidaFoliageColorReloadListener;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class VidaDataPackClientHandler {

    @SubscribeEvent
    public static void initColor(RegisterClientReloadListenersEvent event)  {
        try {
            event.registerReloadListener(new VidaBlueFoliageColorReloadListener());
            event.registerReloadListener(new VidaFoliageColorReloadListener());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
