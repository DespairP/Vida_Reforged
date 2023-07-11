package teamHTBP.vidaReforged.server.events;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.server.providers.ElementPotentialManager;
import teamHTBP.vidaReforged.server.providers.MagicTemplateManager;

/**
 * 注册datapack
 * */
@Mod.EventBusSubscriber
public class VidaDataPackHandler {
    public static final ElementPotentialManager ELEMENT_POTENTIAL_MANAGER = new ElementPotentialManager();
    public static final MagicTemplateManager MAGIC_TEMPLATE_MANAGER = new MagicTemplateManager();

    @SubscribeEvent
    public static void onEvent(AddReloadListenerEvent event) {
        event.addListener(ELEMENT_POTENTIAL_MANAGER);
        event.addListener(MAGIC_TEMPLATE_MANAGER);
    }
}
