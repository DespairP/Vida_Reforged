package teamHTBP.vidaReforged.client.events.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.core.common.ui.component.IViewModelStoreProvider;

/**当Screen被关闭时，自动通知Screen相关的ViewModelStore注销Screen相关ViewModel*/
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ScreenViewModelPluginHandler {
    @SubscribeEvent
    public static void onClosing(ScreenEvent.Closing event){
        if(event.getScreen() instanceof IViewModelStoreProvider holder){
            holder.getStore().clear();
        }
    }
}
