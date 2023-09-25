package teamHTBP.vidaReforged.plugin;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HideJEIGuiHandler<T extends AbstractContainerScreen<?>> implements IGuiContainerHandler<T> {

    @Override
    public @NotNull List<Rect2i> getGuiExtraAreas(@NotNull T containerScreen) {
        return ImmutableList.of(new Rect2i(0,0, containerScreen.width, containerScreen.height));
    }

    public static <T extends AbstractContainerScreen<?>> HideJEIGuiHandler<T> create(Class<T> clazz){
        return new HideJEIGuiHandler<>();
    }

}
