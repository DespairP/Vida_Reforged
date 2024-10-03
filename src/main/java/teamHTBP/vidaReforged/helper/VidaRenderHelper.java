package teamHTBP.vidaReforged.helper;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

@OnlyIn(Dist.CLIENT)
public class VidaRenderHelper {
    private final static Minecraft minecraft = Minecraft.getInstance();
    private final static Logger LOGGER = LogManager.getLogger();

    public static VertexBuffer createLightSky() {
        VertexBuffer skyBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer = buildSkyDisc(bufferbuilder, 16.0F);
        skyBuffer.bind();
        skyBuffer.upload(bufferbuilder$renderedbuffer);
        VertexBuffer.unbind();

        return skyBuffer;
    }

    public static VertexBuffer createDarkSky() {
        VertexBuffer darkBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer = buildSkyDisc(bufferbuilder, -16.0F);
        darkBuffer.bind();
        darkBuffer.upload(bufferbuilder$renderedbuffer);
        VertexBuffer.unbind();

        return darkBuffer;
    }

    public static VertexBuffer createStars(RandomSource randomSource, int starCount, double distance) {
        VertexBuffer starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer = drawStars(bufferbuilder, randomSource, starCount, distance);
        starBuffer.bind();
        starBuffer.upload(bufferbuilder$renderedbuffer);
        VertexBuffer.unbind();
        return starBuffer;
    }

    private static BufferBuilder.RenderedBuffer drawStars(BufferBuilder bufferBuilder, RandomSource randomsource, int starCount, double distance) {
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        for(int i = 0; i < starCount; ++i) {
            double d0 = (double)(randomsource.nextFloat() * 2.0F - 1.0F);
            double d1 = (double)(randomsource.nextFloat() * 2.0F - 1.0F);
            double d2 = (double)(randomsource.nextFloat() * 2.0F - 1.0F);
            double d3 = (double)(0.15F + randomsource.nextFloat() * 0.1F);
            double dist = d0 * d0 + d1 * d1 + d2 * d2;
            if (dist < 1.0D && dist > 0.01D) {
                dist = 1.0D / Math.sqrt(dist);
                d0 *= dist;
                d1 *= dist;
                d2 *= dist;
                double x = d0 * distance;
                double y = d1 * distance;
                double z = d2 * distance;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = randomsource.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for(int j = 0; j < 4; ++j) {
                    double d17 = 0.0D;
                    double d18 = (double)((j & 2) - 1) * d3;
                    double d19 = (double)((j + 1 & 2) - 1) * d3;
                    double d20 = 0.0D;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0D * d13;
                    double d24 = 0.0D * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    bufferBuilder.vertex(x + d25, y + d23, z + d26).endVertex();
                }
            }
        }

        return bufferBuilder.end();
    }

    private static BufferBuilder.RenderedBuffer buildSkyDisc(BufferBuilder bufferBuilder, float v) {
        float f = Math.signum(v) * 512.0F;
        float f1 = 512.0F;
        RenderSystem.setShader(GameRenderer::getPositionShader);
        bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
        bufferBuilder.vertex(0.0D, (double)v, 0.0D).endVertex();

        for(int i = -180; i <= 180; i += 45) {
            bufferBuilder.vertex((double)(f * Mth.cos((float)i * ((float)Math.PI / 180F))), (double)v, (double)(512.0F * Mth.sin((float)i * ((float)Math.PI / 180F)))).endVertex();
        }

        return bufferBuilder.end();

    }

    public static VarHandle colorTextureIdHandle;
    public static VarHandle depthBufferIdHandle;

    static {
        try{
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(RenderTarget.class, MethodHandles.lookup());
            colorTextureIdHandle = lookup.findVarHandle(RenderTarget.class, "colorTextureId", int.class);
            depthBufferIdHandle = lookup.findVarHandle(RenderTarget.class, "depthBufferId", int.class);
        } catch (Exception exception){
            LOGGER.error(exception);
        }
    }

    public static void swapBuffer(RenderTarget targetA, RenderTarget targetB){
        int height = targetA.height;
        int width = targetA.width;
        int viewHeight = targetA.viewHeight;
        int viewWidth = targetA.viewWidth;
        int frameBufferId = targetA.frameBufferId;
        int colorTextureId = targetA.getColorTextureId();
        int depthBufferId = targetA.getDepthTextureId();

        targetA.height = targetB.height;
        targetA.width = targetB.width;
        targetA.viewHeight = targetB.viewHeight;
        targetA.viewWidth = targetB.viewWidth;
        targetA.frameBufferId = targetB.frameBufferId;
        colorTextureIdHandle.set(targetA, targetB.getColorTextureId());
        colorTextureIdHandle.set(targetA, targetB.getDepthTextureId());


        targetB.height = height;
        targetB.width = width;
        targetB.viewHeight = viewHeight;
        targetB.viewWidth = viewWidth;
        targetB.frameBufferId = frameBufferId;
        colorTextureIdHandle.set(targetB, colorTextureId);
        colorTextureIdHandle.set(targetB, depthBufferId);
    }
}
