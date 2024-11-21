package teamHTBP.vidaReforged.client.screen.screens.factions;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.client.renderer.ui.BorderRendererManager;
import teamHTBP.vidaReforged.client.renderer.ui.IBorderRenderer;
import teamHTBP.vidaReforged.client.screen.components.VidaLifecycleSection;
import teamHTBP.vidaReforged.client.shaders.GradientShader;
import teamHTBP.vidaReforged.core.api.screen.StyleSheet;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;

public class TradeFactionBasedSection extends FactionBasedSection {
    @StyleSheet
    GradientShader background;
    @StyleSheet
    IBorderRenderer renderer;
    @StyleSheet
    SecondOrderDynamics offset;
    @StyleSheet
    IBorderRenderer border;

    public TradeFactionBasedSection(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.background = new GradientShader.FlowGradient()
                .addColor(0, 0xFF4C5F7A)
                .addColor(1, 0xFF393E6F)
                .addColor(2, 0xFF3D2E4F)
                .addColor(3, 0xFF321D2F)
                .build();
        offset = new SecondOrderDynamics(1, 1, 0.5f, new Vector3f(150, 0, 0));
        border = BorderRendererManager.getRender(new ResourceLocation(VidaReforged.MOD_ID, "arrows_border"));
    }

    protected void renderBackground(GuiGraphics graphics, float partialTicks){
        background.render(graphics, getX(), getY(), 2, getWidth(), getHeight(), 1F, (long) (ClientTickHandler.ticks * 0.8), partialTicks);
        VidaGuiHelper.fillGradient(graphics , getX(), getY(), getX() + getWidth(), getY() + getHeight(), 20, 0x50000000, 0x50000000);
    }

    protected void renderBorder(GuiGraphics graphics){
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(0,0,10);
        border.renderBorder(graphics, getX(), getY(), getWidth(), getHeight(), 0xDDC8ACD6);
        poseStack.popPose();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Vector3f offsetCurrent = offset.update(Minecraft.getInstance().getDeltaFrameTime() * 0.1F, new Vector3f(), null);
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(offsetCurrent.x, 0,0);
        this.renderBorder(graphics);
        this.renderBackground(graphics, partialTicks);
        poseStack.translate(0, 0, 0);
        poseStack.popPose();
    }
}
