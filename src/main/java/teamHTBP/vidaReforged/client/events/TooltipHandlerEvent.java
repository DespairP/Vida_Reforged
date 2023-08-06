package teamHTBP.vidaReforged.client.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.hud.VidaWandClientTooltipScreen;
import teamHTBP.vidaReforged.server.components.VidaWandTooltipComponent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TooltipHandlerEvent {

    @SubscribeEvent
    public static void onRegisterTooltip(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(VidaWandTooltipComponent.class, VidaWandClientTooltipScreen::new);
    }
}
