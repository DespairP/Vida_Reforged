package teamHTBP.vidaReforged.client.events;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.hud.tooltip.VidaClientTooltip;
import teamHTBP.vidaReforged.client.hud.tooltip.VidaWandClientTooltipScreen;
import teamHTBP.vidaReforged.core.api.items.IVidaItemWithToolTip;
import teamHTBP.vidaReforged.server.components.VidaTooltipComponent;
import teamHTBP.vidaReforged.server.components.VidaWandTooltipComponent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TooltipHandlerEvent {

    private static VidaClientTooltip tooltip;

    @SubscribeEvent
    public static void onRegisterTooltip(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(VidaWandTooltipComponent.class, VidaWandClientTooltipScreen::new);
        event.register(VidaTooltipComponent.class, TooltipHandlerEvent::createOrGetToolTip);
    }

    private static VidaClientTooltip createOrGetToolTip(VidaTooltipComponent component){
        if(tooltip == null){
            tooltip = new VidaClientTooltip(component);
        }
        tooltip.setComponent(component);
        return tooltip;
    }
}
