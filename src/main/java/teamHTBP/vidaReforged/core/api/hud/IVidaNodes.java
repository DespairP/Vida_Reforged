package teamHTBP.vidaReforged.core.api.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.core.common.ui.component.IViewModelStoreProvider;

import java.util.Collection;
import java.util.List;

public interface IVidaNodes {
    default Collection<? extends GuiEventListener> children(){
        return List.of();
    }

    default Collection<VidaWidget> childrenNode() { return  List.of(); }

    public ResourceLocation getId();

    default <T extends IViewModelStoreProvider> T requireParent() {
        Screen parent = Minecraft.getInstance().screen;
        if(parent instanceof IViewModelStoreProvider){
            return (T) parent;
        }
        return null;
    }
}
