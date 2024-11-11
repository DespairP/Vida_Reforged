package teamHTBP.vidaReforged.helper;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.joml.Math;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.VidaReforged;
import net.minecraft.client.gui.GuiGraphics;
import teamHTBP.vidaReforged.client.renderer.RenderTypeHandler;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.client.events.ShadersHandler;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class VidaGuiHelper {
    public static Font FONT = Minecraft.getInstance().font;
    /***/
    public final static ResourceLocation DUNGEON_FONT = new ResourceLocation(VidaReforged.MOD_ID, "dungeonfont");
    //public final static ResourceLocation fusionpixel = new ResourceLocation("vida", "fusionpixel");
    /***/
    public static long newTime = System.currentTimeMillis();

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
    public static void renderCircle(GuiGraphics graphics, PoseStack poseStack, int posX, int posY, float radius, float degree, ARGBColor color){
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
            ).color(color.r() / 255.0f, color.g() / 255.0f, color.b() / 255.0f, color.a() / 255.0f).endVertex();
        }

        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    /**
     * 画圆圈
     * */
    public static void renderHollowCircle(GuiGraphics graphics, PoseStack poseStack, int posX, int posY, float lineWidth, float radius, float degree, ARGBColor fromColor, ARGBColor toColor){
        poseStack.pushPose();
        poseStack.translate(posX, posY,0);
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer buffer = graphics.bufferSource().getBuffer(RenderTypeHandler.GUI_LINE);
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        // 可能没有用...
        RenderSystem.lineWidth(lineWidth);

        final double _360Radians = Math.toRadians(360);

        for (double i = Math.toRadians(0); i < Math.toRadians(degree); i += 0.01)   {
            float percent = (float) (i / _360Radians);
            float r = (fromColor.r() + (toColor.r() - fromColor.r()) * percent) / 255.0f;
            float g = (fromColor.g() + (toColor.g() - fromColor.g()) * percent) / 255.0f;
            float b = (fromColor.b() + (toColor.b() - fromColor.b()) * percent) / 255.0f;

            //double angle = (TWICE_PI * i / sides) + Math.toRadians(180);
            buffer.vertex(
                    matrix4f,
                    (float) (radius * cos(i - Math.toRadians(90))),
                    (float) (radius * sin(i - Math.toRadians(90))),
                    (float) 0
            ).color(r, g, b, fromColor.a() / 255.0f).endVertex();

            buffer.vertex(
                    matrix4f,
                    (float) ((radius - lineWidth) * cos(i - Math.toRadians(90))),
                    (float) ((radius - lineWidth) * sin(i - Math.toRadians(90))),
                    (float) 0
            ).color(r, g, b, fromColor.a() / 255.0f).endVertex();
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

    public static void fillGradient(GuiGraphics graphics, float p_283414_, float p_281397_, float p_283587_, float p_281521_, float p_283505_, int fromColor, int toColor) {
        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());

        float fromA = (float) FastColor.ARGB32.alpha(fromColor) / 255.0F;
        float fromR = (float)FastColor.ARGB32.red(fromColor) / 255.0F;
        float fromG = (float)FastColor.ARGB32.green(fromColor) / 255.0F;
        float fromB = (float)FastColor.ARGB32.blue(fromColor) / 255.0F;
        float toA = (float)FastColor.ARGB32.alpha(toColor) / 255.0F;
        float toR = (float)FastColor.ARGB32.red(toColor) / 255.0F;
        float toG = (float)FastColor.ARGB32.green(toColor) / 255.0F;
        float toB = (float)FastColor.ARGB32.blue(toColor) / 255.0F;
        Matrix4f matrix4f = graphics.pose().last().pose();
        consumer.vertex(matrix4f, (float)p_283414_, (float)p_281397_, (float)p_283505_).color(fromR, fromG, fromB, fromA).endVertex();
        consumer.vertex(matrix4f, (float)p_283414_, (float)p_281521_, (float)p_283505_).color(fromR, fromG, fromB, fromA).endVertex();
        consumer.vertex(matrix4f, (float)p_283587_, (float)p_281521_, (float)p_283505_).color(toR, toG, toB, toA).endVertex();
        consumer.vertex(matrix4f, (float)p_283587_, (float)p_281397_, (float)p_283505_).color(toR, toG, toB, toA).endVertex();
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

    public static void blitWithTexture(GuiGraphics graphics, int x, int y, int z, TextureSection section){
        graphics.blit(
                section.location(),
                x, y, z,
                section.minU(), section.minV(),
                section.w(), section.h(),
                section.texWidth(), section.texHeight()
        );
    }

    public static void drawStringWithFont(GuiGraphics graphics, int x, int y, int z, net.minecraft.network.chat.Component component){
        graphics.pose().pushPose();
        graphics.drawString(Minecraft.getInstance().font, component, x, y, 0xFFFFFFFF);
        graphics.pose().popPose();
    }


    public static void blitWithShader(GuiGraphics graphics, ShaderInstance shader, int x, int y, int z, int width, int height, int ticks, float partialTicks){
        RenderTarget tar = Minecraft.getInstance().getMainRenderTarget();
        float aWidth = tar.width;
        float aHeight = tar.height;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShader(() -> shader);
        ShadersHandler.setUniforms(shader, new ShadersHandler.Point2f(aWidth, aHeight), new ShadersHandler.Point2f(0, 0), ticks, partialTicks);

        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder builder = Tesselator.getInstance().getBuilder();

        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        float eX = (float) x + width;
        float eY = (float) y + height;

        builder.vertex(matrix4f, (float) x, eY, 1).endVertex();
        builder.vertex(matrix4f, eX, eY, 1).endVertex();
        builder.vertex(matrix4f, eX, (float) y, 1).endVertex();
        builder.vertex(matrix4f, (float) x, (float) y, 1).endVertex();

        BufferUploader.drawWithShader(builder.end());
        RenderSystem.disableBlend();
    }

    public static class Style{
        public static int vw(float count){
            return (int)((Minecraft.getInstance().getWindow().getGuiScaledWidth() / 100.0f) * count);
        }

        public static int vh(float count){
            return (int)((Minecraft.getInstance().getWindow().getGuiScaledHeight() / 100.0f) * count);
        }
    }

    /**tick计数器*/
    public static class TickHelper{
        public long lastTick = 0L;
        public long currentTick = 0L;
        public float lastPartialTicks = 0f;
        public float currentPartialTicks = 0f;
        public boolean isInit = false;
        public long startTick = 0L;

        /**计数*/
        public void tick(float partialTicks){
            if(!isInit){
                startTick = currentTick = lastTick = ClientTickHandler.ticks;
                currentPartialTicks = lastPartialTicks = partialTicks;
                this.isInit = true;
                return;
            }
            lastTick = currentTick;
            currentTick = ClientTickHandler.ticks;
            lastPartialTicks = currentPartialTicks;
            currentPartialTicks = partialTicks;
        }

        /**
         * 计算该frame下需要增长多少
         * @param step 每个ticks的增长步长
         * @see Minecraft#getDeltaFrameTime()
         * */
        public float getTickPercent(float step){
            return step * (float) ((currentTick - lastTick) + (currentPartialTicks - lastPartialTicks));
        }

        /** 计算从开始tick到现在已经过来多少个ticks了 */
        public long getTicksLength(){
            return currentTick - startTick;
        }

        /**重置计数器*/
        public void reset(){
            this.isInit = false;
            this.startTick = 0L;
            this.currentTick = 0L;
        }
    }

}
