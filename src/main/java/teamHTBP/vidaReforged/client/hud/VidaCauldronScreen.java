package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;

/**
 * 纯净锅HUD
 * */
public class VidaCauldronScreen extends GuiGraphics implements IVidaScreen {
    public VidaCauldronScreen(Minecraft p_283406_, MultiBufferSource.BufferSource p_282238_) {
        super(p_283406_, p_282238_);
    }

    @Override
    public void render(PoseStack poseStack) {

    }
}
