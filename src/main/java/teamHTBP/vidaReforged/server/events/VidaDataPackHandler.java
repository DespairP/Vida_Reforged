package teamHTBP.vidaReforged.server.events;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.core.common.system.guidebook.TeaconGuideBook;
import teamHTBP.vidaReforged.server.providers.ElementPotentialManager;
import teamHTBP.vidaReforged.server.providers.MagicTemplateManager;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;
import teamHTBP.vidaReforged.server.providers.TeaconGuideBookManager;

/**
 * 注册datapack
 * */
@Mod.EventBusSubscriber
public class VidaDataPackHandler {
    public static final ElementPotentialManager ELEMENT_POTENTIAL_MANAGER = new ElementPotentialManager();
    public static final MagicTemplateManager MAGIC_TEMPLATE_MANAGER = new MagicTemplateManager();
    public static final MagicWordManager MAGIC_WORD_MANAGER = new MagicWordManager();
    public static final TeaconGuideBookManager GUIDE_BOOK_MANAGER = new TeaconGuideBookManager();

    @SubscribeEvent
    public static void onEvent(AddReloadListenerEvent event) {
        event.addListener(ELEMENT_POTENTIAL_MANAGER);
        event.addListener(MAGIC_TEMPLATE_MANAGER);
        event.addListener(MAGIC_WORD_MANAGER);
        event.addListener(GUIDE_BOOK_MANAGER);
    }
}
