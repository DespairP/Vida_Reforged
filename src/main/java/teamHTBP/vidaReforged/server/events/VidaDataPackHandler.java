package teamHTBP.vidaReforged.server.events;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.core.common.system.guidebook.*;
import teamHTBP.vidaReforged.server.providers.*;

import static teamHTBP.vidaReforged.core.common.VidaConstant.*;

/**
 * 注册datapack
 * */
@Mod.EventBusSubscriber
public class VidaDataPackHandler {
    public static final ElementPotentialManager ELEMENT_POTENTIAL_MANAGER = new ElementPotentialManager();
    public static final VidaMagicManager MAGIC_TEMPLATE_MANAGER = new VidaMagicManager();
    public static final MagicWordManager MAGIC_WORD_MANAGER = new MagicWordManager();
    public static final TeaconGuideBookManager GUIDE_BOOK_MANAGER = new TeaconGuideBookManager();
    public static final VidaGuidebookManager<VidaPageSection> SECTION_MANAGER = new VidaGuidebookManager<>(
            DATA_PAGE_SECTION_SELECTOR,
            VidaGuidebookManager.VIDA_PAGE_SECTION_CODEC,
            VidaGuidebookManager.SECTION_MAP,
            null
    );


    public static final VidaGuidebookManager<VidaPageList> LIST_MANAGER = new VidaGuidebookManager<>(
            DATA_PAGE_LIST_SELECTOR,
            VidaGuidebookManager.VIDA_PAGE_LIST_CODEC,
            VidaGuidebookManager.LIST_MAP,
            null
    );

    public static final VidaGuidebookManager<VidaPageListItem> GUIDE_LIST_ITEM_MANAGER = new VidaGuidebookManager<>(
            DATA_PAGE_LIST_ITEM_SELECTOR,
            VidaGuidebookManager.VIDA_PAGE_LIST_ITEM_CODEC,
            VidaGuidebookManager.LIST_ITEM_MAP,
            VidaGuidebookManager::handleListItem
    );

    public static final VidaGuidebookManager<VidaPageDetail> DETAIL_MANAGER = new VidaGuidebookManager<>(
            DATA_PAGE_DETAIL_SELECTOR,
            VidaGuidebookManager.VIDA_PAGE_DETAIL_CODEC,
            VidaGuidebookManager.DETAIL_PAGE_MAP,
            VidaGuidebookManager::handleDetail
    );

    @SubscribeEvent
    public static void onEvent(AddReloadListenerEvent event) {
        event.addListener(ELEMENT_POTENTIAL_MANAGER);
        event.addListener(MAGIC_TEMPLATE_MANAGER);
        event.addListener(MAGIC_WORD_MANAGER);
        event.addListener(GUIDE_BOOK_MANAGER);
        event.addListener(SECTION_MANAGER);
        event.addListener(LIST_MANAGER);
        event.addListener(GUIDE_LIST_ITEM_MANAGER);
        event.addListener(DETAIL_MANAGER);
    }
}
