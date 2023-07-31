package teamHTBP.vidaReforged.plugin;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.handlers.IGlobalGuiHandler;
import mezz.jei.api.gui.handlers.IGuiProperties;
import mezz.jei.api.gui.handlers.IScreenHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.client.screen.TeaconGuideBookScreen;
import teamHTBP.vidaReforged.core.common.system.guidebook.TeaconGuideBook;

import java.util.Collection;

public class HideJEIScreenHandler implements IGlobalGuiHandler {
    @Override
    public Collection<Rect2i> getGuiExtraAreas() {
        if(Minecraft.getInstance().screen == null){
            return ImmutableList.of(new Rect2i(0,0,0,0));
        }
        return ImmutableList.of(new Rect2i(0, 0, Minecraft.getInstance().screen.width,Minecraft.getInstance().screen.height));
    }
}
