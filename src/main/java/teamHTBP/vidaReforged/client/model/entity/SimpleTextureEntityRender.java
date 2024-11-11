package teamHTBP.vidaReforged.client.model.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import static teamHTBP.vidaReforged.client.renderer.RenderTypeHandler.PARTICLE_SHEET_NO_MASK;

public abstract class SimpleTextureEntityRender<T extends Entity> extends EntityRenderer<T> {
    private TextureSection texture;

    protected SimpleTextureEntityRender(EntityRendererProvider.Context context, TextureSection texture) {
        super(context);
        this.texture = texture;
    }

    @Override
    public void render(T entity, float p1, float p2, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getParticleShader);
        Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();

        PoseStack stack = RenderSystem.getModelViewStack();

        stack.pushPose();
        stack.mulPoseMatrix(poseStack.last().pose());
        stack.pushPose();

        stack.translate(0, 0.3F, 0);
        RenderSystem.applyModelViewMatrix();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();

        PARTICLE_SHEET_NO_MASK.begin(builder, Minecraft.getInstance().textureManager);
        ARGBColor color = ARGBColor.argb(argbColor(entity));
        RenderSystem.depthMask(true);
        RenderSystem.setShaderTexture(0, getTextureLocation(entity));
        RenderSystem.setShaderColor(color.r() / 255.0F, color.g() / 255.0F, color.b() / 255.0F, color.a() / 255.0F);

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f3 = size(entity);

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(this.entityRenderDispatcher.cameraOrientation());
            vector3f.mul(f3);
        }

        float uMin = 0;
        float uMax = 1;
        float vMin = 0;
        float vMax = 1;

        builder.vertex((double)avector3f[0].x(), (double)avector3f[0].y(), (double)avector3f[0].z()).uv(uMin, vMin).color(1, 1, 1, 1.0f).uv2(240, 240).endVertex();
        builder.vertex((double)avector3f[1].x(), (double)avector3f[1].y(), (double)avector3f[1].z()).uv(uMax, vMin).color(1, 1, 1, 1.0f).uv2(240, 240).endVertex();
        builder.vertex((double)avector3f[2].x(), (double)avector3f[2].y(), (double)avector3f[2].z()).uv(uMax, vMax).color(1, 1, 1, 1.0f).uv2(240, 240).endVertex();
        builder.vertex((double)avector3f[3].x(), (double)avector3f[3].y(), (double)avector3f[3].z()).uv(uMin, vMax).color(1, 1, 1, 1.0f).uv2(240, 240).endVertex();

        PARTICLE_SHEET_NO_MASK.end(tesselator);
        stack.popPose();
        stack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer();
    }

    @Override
    public ResourceLocation getTextureLocation(Entity p_114482_) {
        return texture.location();
    }

    public abstract int argbColor(T entity);

    public abstract float size(T entity);
}
