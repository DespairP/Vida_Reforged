package teamHTBP.vidaReforged.client.events.registries;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import teamHTBP.vidaReforged.client.screen.*;
import teamHTBP.vidaReforged.client.screen.screens.common.PrismScreen;
import teamHTBP.vidaReforged.client.screen.screens.common.SherdResearchScreen;
import teamHTBP.vidaReforged.client.screen.screens.magicwordAchieve.MagicWordScreen;
import teamHTBP.vidaReforged.client.screen.screens.wandCrafting.VidaWandCraftingScreen;
import teamHTBP.vidaReforged.client.screen.screens.magicwordCrafting.MagicWordCraftingTableScreen;
import teamHTBP.vidaReforged.server.menu.VidaMenuContainerTypeLoader;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VidaScreenRegistryHandler {

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(VidaMenuContainerTypeLoader.TIME_ELEMENT_MENU.get(), TimeElementCraftTableScreenTest::new);
            MenuScreens.register(VidaMenuContainerTypeLoader.JIGSAW_EQUIP.get(), MagicJigsawScreen::new);
            MenuScreens.register(VidaMenuContainerTypeLoader.MAGIC_WORD_CRAFTING.get(), MagicWordCraftingTableScreen::new);
            MenuScreens.register(VidaMenuContainerTypeLoader.PRISM.get(), PrismScreen::new);
            MenuScreens.register(VidaMenuContainerTypeLoader.MAGIC_WORD_VIEWING.get(), MagicWordScreen::new);
            MenuScreens.register(VidaMenuContainerTypeLoader.VIDA_WAND_CRAFTING_TABLE.get(), VidaWandCraftingScreen::new);
            MenuScreens.register(VidaMenuContainerTypeLoader.SHERD_RESEARCH_TABLE.get(), SherdResearchScreen::new);
        });
    }
}
