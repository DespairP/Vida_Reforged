package teamHTBP.vidaReforged.plugin;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.handlers.IGlobalGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.client.screen.TeaconGuideBookScreen;

import java.util.Collection;

public class HideJEIScreenHandler implements IGlobalGuiHandler {
    @Override
    public @NotNull Collection<Rect2i> getGuiExtraAreas() {
        if(Minecraft.getInstance().screen == null || !(Minecraft.getInstance().screen instanceof TeaconGuideBookScreen)){
            return ImmutableList.of(new Rect2i(0,0,0,0));
        }
        return ImmutableList.of(new Rect2i(0, 0, Minecraft.getInstance().screen.width,Minecraft.getInstance().screen.height));
    }
}
