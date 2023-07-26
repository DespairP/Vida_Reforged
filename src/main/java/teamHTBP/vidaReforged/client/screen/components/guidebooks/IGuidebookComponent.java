package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collection;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public interface IGuidebookComponent extends GuiEventListener{
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks);

    default Collection<? extends GuiEventListener> getChildren(){
        return List.of();
    }
}
