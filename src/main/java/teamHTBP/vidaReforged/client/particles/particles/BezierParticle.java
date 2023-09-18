package teamHTBP.vidaReforged.client.particles.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.joml.Vector3d;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.Bezier3Curve;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BezierParticle extends TrailParticle {
    public List<Vector3d> tails = new ArrayList<>();

    public BezierParticle(ClientLevel level, double x, double y, double z, double speedX, double speedY, double speedZ, int a, int r, int g, int b, int size, int age, List<Vector3d> tails) {
        super(level, x, y ,z, 0, 0, 0, a, r, g, b, size, age);
        this.tails = tails;
        this.lifetime = 5;
        this.rCol = r / 255.0F;
        this.gCol = g / 255.0F;
        this.bCol = b / 255.0F;

    }

    public void render(@Nonnull VertexConsumer pBuffer, @Nonnull Camera camera, float partialTicks) {
        int texture = RenderSystem.getShaderTexture(0);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();


        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderColor(rCol, gCol, bCol,1);
        builder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.PARTICLE);
        renderTails(pBuffer, camera, partialTicks);
        tesselator.end();
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.disableBlend();
    }


    public void renderTails(@Nonnull VertexConsumer pBuffer, @Nonnull Camera camera, float partialTicks){
        Vector3d[] verts = new Vector3d[tails.size() * 2];
        double x = (Mth.lerp(partialTicks, this.xo, this.x));
        double y = (Mth.lerp(partialTicks, this.yo, this.y));
        double z = (Mth.lerp(partialTicks, this.zo, this.z));
        Vector3d lastTail = new Vector3d(x, y, z);
        Vector3d cameraPos = new Vector3d(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
        int size = tails.size() - 1;
        for (int i = size; i >= 0; i--) {
            Vector3d tail = new Vector3d(tails.get(i));
            renderTail(verts, size - i, cameraPos, lastTail, tail, partialTicks);
            lastTail = tail;
        }
        for (int i = 0; i < (verts.length / 2) - 1; i++) {
            Vector3d currentU = verts[i * 2];
            Vector3d currentD = verts[i * 2 + 1];
            Vector3d nextU = verts[(i + 1) * 2];
            Vector3d nextD = verts[(i + 1) * 2 + 1];

            float u0 = getU0(i, partialTicks);
            float u1 = getU1(i, partialTicks);
            float v0 = getV0(i, partialTicks);
            float v1 = getV1(i, partialTicks);
            int light = getLightColor(i, partialTicks);

            pBuffer.vertex(currentD.x, currentD.y, currentD.z).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
            pBuffer.vertex(currentU.x, currentU.y, currentU.z).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
            pBuffer.vertex(nextD.x, nextD.y, nextD.z).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();

            pBuffer.vertex(nextD.x, nextD.y, nextD.z).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
            pBuffer.vertex(currentU.x, currentU.y, currentU.z).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(15728880).endVertex();
            pBuffer.vertex(nextU.x, nextU.y, nextU.z).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
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

}
