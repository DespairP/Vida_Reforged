package teamHTBP.vidaReforged.core.api.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import teamHTBP.vidaReforged.core.common.ui.component.IViewModelStoreProvider;

import java.util.Collection;
import java.util.List;

public interface IVidaNodes {
    default Collection<? extends GuiEventListener> children(){
        return List.of();
    }

    default <T extends IViewModelStoreProvider> T requireParent() {
        Screen parent = Minecraft.getInstance().screen;
        if(parent instanceof IViewModelStoreProvider){
            return (T) parent;
        }
        return null;
    }
}
