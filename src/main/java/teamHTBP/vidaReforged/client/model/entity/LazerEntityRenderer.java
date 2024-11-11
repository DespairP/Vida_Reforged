package teamHTBP.vidaReforged.client.model.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Vector3d;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.server.entity.LazerEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static teamHTBP.vidaReforged.client.renderer.RenderTypeHandler.TRAIL_SHADER;

/**激光*/
public class LazerEntityRenderer extends EntityRenderer<LazerEntity> {

    protected List<Vector3d> tails = new ArrayList<>();


    public LazerEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(LazerEntity p_114482_) {
        return new ResourceLocation(VidaReforged.MOD_ID, "textures/particle/spark.png");
    }


    @Override
    public void render(LazerEntity entity, float p1, float p2, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        tails = new ArrayList<>();
        tails.addAll(entity.getEntityData().get(LazerEntity.TRAILS));
        if(tails.size() >= 0){
            return;
        }

        Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE2);
        RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE0);


        PoseStack stack = RenderSystem.getModelViewStack();

        stack.pushPose();
        stack.mulPoseMatrix(poseStack.last().pose());
        stack.translate(0, 1,0);
        RenderSystem.applyModelViewMatrix();

        RenderSystem.setShader(GameRenderer::getParticleShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        TRAIL_SHADER.begin(builder, Minecraft.getInstance().textureManager);
        RenderSystem.setShaderTexture(0, getTextureLocation(entity));
        RenderSystem.depthMask(false);



        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        renderTails(entity, builder, Minecraft.getInstance().gameRenderer.getMainCamera(), Minecraft.getInstance().getPartialTick(), light);
        tesselator.end();

        TRAIL_SHADER.end(tesselator);
        stack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();

        RenderSystem.disableBlend();
        Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer();

    }

    public void renderTails(LazerEntity entity, @Nonnull VertexConsumer pBuffer, @Nonnull Camera camera, float partialTicks, int lightIn){
        Vector3d[] verts = new Vector3d[tails.size() * 2];
        double x = (Mth.lerp(partialTicks, entity.xo, entity.getX()));
        double y = (Mth.lerp(partialTicks, entity.yo, entity.getY()));
        double z = (Mth.lerp(partialTicks, entity.zo, entity.getZ()));
        Vector3d lastTail = new Vector3d(x, y, z);
        Vector3d cameraPos = new Vector3d(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
        int size = tails.size() - 1;
        int index = 0;

        for(Vector3d tail : tails){
            Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
            float f3 = 1;

            for(int i = 0; i < 4; ++i) {
                Vector3f vector3f = avector3f[i];
                vector3f.rotate(camera.rotation());
                vector3f.mul(f3);
                vector3f.add((float) tail.x, (float) tail.y, (float) tail.z).sub(new Vector3f((float)x, (float)y, (float)z));
            }


            float f6 = 0;
            float f7 = 1;
            float f4 = 0;
            float f5 = 1;
            float alpha = 1.0f - (0.08f * index++);
            int j = 15728880;
            pBuffer.vertex((double)avector3f[0].x(), (double)avector3f[0].y(), (double)avector3f[0].z()).uv(f7, f5).color(1.0f, 1.0f, 1.0f, alpha).uv2(j).endVertex();
            pBuffer.vertex((double)avector3f[1].x(), (double)avector3f[1].y(), (double)avector3f[1].z()).uv(f7, f4).color(1.0f, 1.0f, 1.0f, alpha).uv2(j).endVertex();
            pBuffer.vertex((double)avector3f[2].x(), (double)avector3f[2].y(), (double)avector3f[2].z()).uv(f6, f4).color(1.0f, 1.0f, 1.0f, alpha).uv2(j).endVertex();
            pBuffer.vertex((double)avector3f[3].x(), (double)avector3f[3].y(), (double)avector3f[3].z()).uv(f6, f5).color(1.0f, 1.0f, 1.0f, alpha).uv2(j).endVertex();
        }
    }


    public void renderTail(Vector3d[] verts, int i, Vector3d cameraPos, Vector3d current, Vector3d nextTail, float partialTicks) {
        float size = getWidth(i, partialTicks);
        Vector3d direction = new Vector3d(nextTail).sub(current);
        Vector3d toTail = new Vector3d(current).sub(cameraPos);
        Vector3d normal = new Vector3d(toTail).cross(direction).normalize();
        verts[i * 2] = new Vector3d(current).add(new Vector3d(normal).mul(size)).sub(cameraPos);
        verts[i * 2 + 1] = new Vector3d(current).add(new Vector3d(normal).mul(-size)).sub(cameraPos);
    }

    public float getWidth(int tail, float pPartialTicks) {
        return 1;
    }


    protected float getU0(int tail, float pPartialTicks) {
        return  1 - (tail + 1 + pPartialTicks) / (15 - 1f);
    }

    protected float getV0(int tail, float pPartialTicks) {
        return 0;
    }

    protected float getU1(int tail, float pPartialTicks) {
        return  1 - (tail + pPartialTicks) / (15 - 1f);
    }

    protected float getV1(int tail, float pPartialTicks) {
        return 1;
    }

    @Override
    public boolean shouldRender(LazerEntity p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }
}
