package teamHTBP.vidaReforged.client.shaders;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import teamHTBP.vidaReforged.client.events.ShadersHandler;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

import java.util.ArrayList;
import java.util.List;

public class GradientShader {
    /***/
    public final ShaderInstance instance;
    public final LinearGradient config;

    public GradientShader(ShaderInstance instance, LinearGradient config) {
        this.instance = instance;
        this.config = config;
    }

    public void render(GuiGraphics graphics, int x, int y, int z, int width, int height, float alpha, long times, float partialTicks){
        RenderTarget tar = Minecraft.getInstance().getMainRenderTarget();
        float aWidth = tar.width;
        float aHeight = tar.height;


        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShader(() -> instance);

        ShadersHandler.setPoint2f(instance, "iResolution",  aWidth, aHeight);
        for(int i = 0; i < config.nums && i < 3; i ++){
            Vector4f color = new Vector4f(config.colors.get(i));
            color.w = alpha;
            ShadersHandler.setFloat(instance, "stop" + i, config.steps.get(i));
            ShadersHandler.setVector4fParam(instance, "color" + i, color);
        }
        ShadersHandler.setFloat(instance, "angle", config.angle);
        ShadersHandler.setInteger(instance, "num_stops", Math.min(3, config.steps.size()));
        ShadersHandler.setFloat(instance, "iTime", times * 0.125f);

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
        RenderSystem.disableBlend();
    }


    public static class LinearGradient {
        public List<Vector4f> colors = new ArrayList<>();
        public List<Float> steps = new ArrayList<>();
        public int nums = 0;
        public float angle = 0;

        public LinearGradient(float angle){
            this.angle = angle;
        }

        public LinearGradient addColor(float step, int argb){
            float a = (argb >>> 24) / 255.0f , r = (argb >> 16 & 255) / 255.0f, g = (argb >> 8 & 255) / 255.0f, b = (argb & 255) / 255.0f;
            this.colors.add(new Vector4f(r, g, b, a));
            this.steps.add(step);
            nums += 1;
            return this;
        }

        public void addColor(float step, ARGBColor argb){
            float a = argb.a() / 255.0f , r = argb.r() / 255.0f, g = argb.g() / 255.0f, b = argb.b() / 255.0f;
            this.colors.add(new Vector4f(r, g, b, a));
            this.steps.add(step);
            nums += 1;
        }
        public GradientShader build(){
            return new GradientShader(ShadersHandler.gradientLinear, this);
        }

        public float getStep(int index) {
            return steps.get(index);
        }
    }

    public static class FlowGradient extends LinearGradient{

        public FlowGradient() {
            super(180);
        }

        @Override
        public GradientShader build() {
            return new GradientShader(ShadersHandler.gradientFlow, this);
        }
    }
}
