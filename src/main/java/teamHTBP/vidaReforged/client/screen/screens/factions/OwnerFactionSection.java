package teamHTBP.vidaReforged.client.screen.screens.factions;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.client.events.ShadersHandler;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;

public class OwnerFactionSection extends FactionBasedSection{
    /**进度*/
    private float tradeProgress = 282.5f;
    /**进度动画*/
    private SecondOrderDynamics displayProgress = new SecondOrderDynamics(1, 1, 0.4f, new Vector3f(1F));
    /**ICON大小*/
    private SecondOrderDynamics displaySizeIcon = new SecondOrderDynamics(1, 1, 0.4f, new Vector3f(0F));
    /**环厚度*/
    private SecondOrderDynamics displaySizeProgress = new SecondOrderDynamics(1, 1, 0.4f, new Vector3f(0.05F));
    /**光圈*/
    private static ShaderInstance ringShader = ShadersHandler.ringProgressBar;
    /**图标*/
    private static final ResourceLocation ICON = new ResourceLocation(VidaReforged.MOD_ID, "textures/icons/trade.png");
    private static TextureSection ICON_SECTION = new TextureSection(ICON, 0, 0, 28, 32,28, 32);

    public OwnerFactionSection(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        float roll = tradeProgress % 100.0f;
        Vector3f nextRoll = displayProgress.update(mc.getDeltaFrameTime() * 0.05f, new Vector3f(roll), null);
        Vector3f nextThickness = displaySizeProgress.update(mc.getDeltaFrameTime() * 0.05f, isHovered ? new Vector3f(0.06f) : new Vector3f(0.05f), null);

        ShadersHandler.setFloat(ringShader, "progress", nextRoll.x);
        ShadersHandler.setVector4fParam(ringShader, "fromColor", new Vector4f(0.54F,0.57F,0.71F,1));
        ShadersHandler.setVector4fParam(ringShader, "toColor", new Vector4f(0.37F,0.51F,0.51F,1));
        ShadersHandler.setFloat(ringShader, "thickness", nextThickness.x);
        blitWithShader(
                graphics,
                ringShader,
                getX(),
                getY(),
                1,
                (int) (getWidth() * mc.getInstance().getWindow().getGuiScale()),
                (int) (getHeight() * mc.getInstance().getWindow().getGuiScale()),
                (int) ClientTickHandler.ticks,
                partialTicks
        );
        renderIcon(graphics, partialTicks);
    }

    protected void renderIcon(GuiGraphics graphics, float partialTicks){
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        Vector3f currentSize = displaySizeIcon.update(mc.getDeltaFrameTime() * 0.08f, isHovered ? new Vector3f(0.5f) : new Vector3f(0.6F), null);
        poseStack.translate(12f, 12f, 0);
        poseStack.scale(currentSize.x, currentSize.y, 3);
        poseStack.translate(-12f, -12f, 0);
        graphics.blit(
                ICON_SECTION.location(),
                (int)((getX() + (getWidth() - ICON_SECTION.w()) / 2 + 1) * (1.0f / currentSize.x)), (int) ((getY() + (getHeight() - ICON_SECTION.h()) / 2 + 1) * (1.0f / currentSize.y)), 2,
                ICON_SECTION.minU(), ICON_SECTION.minV(),
                ICON_SECTION.w(), ICON_SECTION.h(),
                ICON_SECTION.texWidth(), ICON_SECTION.texHeight()
        );
        poseStack.popPose();
    }

    private static void blitWithShader(GuiGraphics graphics, ShaderInstance shader, int x, int y, int z, int width, int height, int ticks, float partialTicks){
        RenderSystem.enableBlend();
        RenderSystem.setShader(() -> shader);
        RenderSystem.defaultBlendFunc();
        Uniform iResolution = shader.getUniform("iResolution");
        if (iResolution != null) {
            iResolution.set((float) width, (float) height);
        }

        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder builder = Tesselator.getInstance().getBuilder();

        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        float eX = (float) x + width;
        float eY = (float) y + height;

        builder.vertex(matrix4f, (float) x, eY, z).endVertex();
        builder.vertex(matrix4f, eX, eY, z).endVertex();
        builder.vertex(matrix4f, eX, (float) y, z).endVertex();
        builder.vertex(matrix4f, (float) x, (float) y, z).endVertex();

        BufferUploader.drawWithShader(builder.end());
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }
}
