package teamHTBP.vidaReforged.server.events;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.server.providers.ElementPotentialManager;

@Mod.EventBusSubscriber
public class JsonResourceLoadHandler {
    public static final ElementPotentialManager ELEMENT_POTENTIAL_MANAGER = new ElementPotentialManager();

    @SubscribeEvent
    public static void onEvent(AddReloadListenerEvent event) {
        event.addListener(ELEMENT_POTENTIAL_MANAGER);
    }
}
