package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IGuidebookComponent extends GuiEventListener{
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks);
}
