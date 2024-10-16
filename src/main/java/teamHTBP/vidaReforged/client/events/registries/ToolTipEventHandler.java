package teamHTBP.vidaReforged.client.events.registries;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ToolTipEventHandler {
    @SubscribeEvent
    public static void onPreRenderToolTips(RenderTooltipEvent.Pre event){
    }

}
