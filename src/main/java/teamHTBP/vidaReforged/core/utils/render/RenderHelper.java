package teamHTBP.vidaReforged.core.utils.render;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.VidaReforged;
import net.minecraft.client.gui.GuiGraphics;
import teamHTBP.vidaReforged.client.events.RenderTypeHandler;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class RenderHelper {
    public static Font fontRenderer = Minecraft.getInstance().font;
    /***/
    public final static ResourceLocation DUNGEON_FONT = new ResourceLocation(VidaReforged.MOD_ID, "dungeonfont");
    //public final static ResourceLocation fusionpixel = new ResourceLocation("vida", "fusionpixel");

    /**
     * 裁剪屏幕
     * 由于GL20的scissor的xy与MC的xy不同,所以请使用这个方法裁剪
     * @param posX 起始X位置
     * @param posY 起始Y位置
     * @param width 裁剪宽度
     * @param height 裁剪高度
     * */
    public static void renderScissor(int posX,int posY,int width,int height){
        Window window = Minecraft.getInstance().getWindow();
        int scaledHeight = window.getGuiScaledHeight();
        int scaledWidth = window.getGuiScaledWidth();
        double scaledFactor = window.getGuiScale();
        RenderSystem.enableScissor(
                (int)(posX * scaledFactor),
                (int)((scaledHeight - posY - height) * scaledFactor),
                (int)(width * scaledFactor),
                (int)(height * scaledFactor)
        );
    }

    /**
     * 画圆圈
     * */
    public static void renderCircle(GuiGraphics graphics,PoseStack poseStack, int posX, int posY,float radius,float degree){
        poseStack.pushPose();
        poseStack.translate(posX, posY,0);
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer buffer = graphics.bufferSource().getBuffer(RenderTypeHandler.TRIANGLE_FAN);
        RenderSystem.enableBlend();
        RenderSystem.disableCull();

        buffer.vertex(matrix4f,0,0,0).color(0.4f, 0.4f, 0.4f, 0.5F).endVertex();

        for (double i = Math.toRadians(0); i < Math.toRadians(degree); i += 0.01)   {
            //double angle = (TWICE_PI * i / sides) + Math.toRadians(180);
            buffer.vertex(
                    matrix4f,
                    (float) (radius * cos(i - Math.toRadians(90))),
                    (float) (radius * sin(i - Math.toRadians(90))),
                    (float) 0
            ).color(0.4f, 0.4f, 0.4f, 1).endVertex();
        }

        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    public static void blitVertical(PoseStack poseStack, int x1, int y1, int height, int weight, int z, TextureAtlasSprite sprite, double percent) {
        innerBlit(
                poseStack,
                x1, x1 + weight,
                (int) (y1 + height * (1 - percent)), y1 + height,
                z,
                sprite.getU0(), sprite.getU1(),
                (float) (sprite.getV0() + (sprite.getV1() - sprite.getV0()) * (1 - percent)), sprite.getV1()
        );
    }

    private static void innerBlit(PoseStack poseStack, float x1, float x2, float y1, float y2, float z,
                                  float minU, float maxU, float minV, float maxV) {
        Matrix4f m4 = poseStack.last().pose();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(m4, x1, y2, z).uv(minU, maxV).endVertex();
        bufferbuilder.vertex(m4, x2, y2, z).uv(maxU, maxV).endVertex();
        bufferbuilder.vertex(m4, x2, y1, z).uv(maxU, minV).endVertex();
        bufferbuilder.vertex(m4, x1, y1, z).uv(minU, minV).endVertex();
        bufferbuilder.end();
    }
}
