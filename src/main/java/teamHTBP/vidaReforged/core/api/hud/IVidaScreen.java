package teamHTBP.vidaReforged.core.api.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public interface IVidaScreen {
    public final Minecraft mc = Minecraft.getInstance();

    void render(GuiGraphics graphics, float partialTicks);
}
