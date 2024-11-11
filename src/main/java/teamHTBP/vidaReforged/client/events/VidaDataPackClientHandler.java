package teamHTBP.vidaReforged.client.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.providers.VidaBlueFoliageColorReloadListener;
import teamHTBP.vidaReforged.client.providers.VidaFoliageColorReloadListener;
import teamHTBP.vidaReforged.client.providers.ScreenComponentStyleSheetManager;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class VidaDataPackClientHandler {

    @SubscribeEvent
    public static void initColor(RegisterClientReloadListenersEvent event)  {
        try {
            event.registerReloadListener(new VidaBlueFoliageColorReloadListener());
            event.registerReloadListener(new VidaFoliageColorReloadListener());
            event.registerReloadListener(new ScreenComponentStyleSheetManager());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
