package teamHTBP.vidaReforged.client.events;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import teamHTBP.vidaReforged.client.screen.TimeElementCraftTableScreen;
import teamHTBP.vidaReforged.server.menu.VidaMenuContainerTypeLoader;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VidaScreenHandler {

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(VidaMenuContainerTypeLoader.TIME_ELEMENT_MENU.get(), TimeElementCraftTableScreen::new);
        });
    }
}