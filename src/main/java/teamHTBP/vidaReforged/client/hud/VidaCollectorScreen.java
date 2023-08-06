package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;

public class VidaCollectorScreen extends GuiGraphics implements IVidaScreen {
    public VidaCollectorScreen(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
    }

    @Override
    public void render(PoseStack poseStack) {

    }
}
