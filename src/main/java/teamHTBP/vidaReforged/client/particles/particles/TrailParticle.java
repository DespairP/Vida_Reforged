package teamHTBP.vidaReforged.client.particles.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.joml.Vector3d;
import teamHTBP.vidaReforged.core.utils.math.Bezier3Curve;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.Consumer;

import static teamHTBP.vidaReforged.client.renderer.RenderTypeHandler.TRAIL_SHADER;

/**四阶Bezier曲线*/
public class TrailParticle extends VidaBaseParticle {
    public Minecraft mc = Minecraft.getInstance();
    private Bezier3Curve curve;
    protected ArrayList<Vector3d> tails = new ArrayList<>();
    protected int maxTail;
    protected int freq;
    protected boolean cull = true;
    protected float width;
    protected Consumer<TrailParticle> onUpdate;
    protected double speedX = 0;

    public TrailParticle(ClientLevel level, double x, double y, double z, double speedX, double speedY, double speedZ, VidaParticleAttributes attributes) {
        super(level, x, y ,z, speedX, speedY, speedZ, attributes);
        maxTail = 15;
        freq = 1;
        width = 1f;
        cull = false;
        this.curve = new Bezier3Curve(
                new Vector3d(x, y, z),
                new Vector3d(x + 10, y , z + 10),
                new Vector3d(x + 30, y, z + 30),
                new Vector3d(x + 50, y , z + 50)
        );
        this.rCol = 1;
        this.gCol = 1;
        this.bCol = 1;
        this.alpha = 1F;
        this.lifetime = 50;
        this.speedX = speedX;
    }


    @Override
    public ParticleRenderType getRenderType() {
        return TRAIL_SHADER;
    }

    public void render(@Nonnull VertexConsumer pBuffer, @Nonnull Camera camera, float partialTicks) {
        int texture = RenderSystem.getShaderTexture(0);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();


        RenderSystem.setShader(GameRenderer::getParticleShader);
        builder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.PARTICLE);
        renderTails(pBuffer, camera, partialTicks);
        tesselator.end();
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

            pBuffer.vertex(currentD.x, currentD.y, currentD.z).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(15728880).endVertex();
            pBuffer.vertex(currentU.x, currentU.y, currentU.z).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(15728880).endVertex();
            pBuffer.vertex(nextD.x, nextD.y, nextD.z).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(15728880).endVertex();

            pBuffer.vertex(nextD.x, nextD.y, nextD.z).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(15728880).endVertex();
            pBuffer.vertex(currentU.x, currentU.y, currentU.z).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(15728880).endVertex();
            pBuffer.vertex(nextU.x, nextU.y, nextU.z).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(15728880).endVertex();
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
        return width;
    }
    protected Vector3d getTail() {
        return new Vector3d(this.xo, this.yo, this.zo);
    }
    @Override
    public void tick() {
        tails.add(getTail());
        while (tails.size() > maxTail) {
            tails.remove(0);
        }

        this.age += 1;

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime && lifetime > 0) {
            this.remove();
        } else if (onUpdate == null) {
            update();
        } else {
            onUpdate.accept(this);
        }
    }

    public void setOnUpdate(Consumer<TrailParticle> onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void update() {
        if (getLifetime() > 0) {
            var t = (mc.getFrameTime() + age) / lifetime;
            Vector3d point = curve.getPoint(t);
            setPos(point.x, point.y, point.z);
        }

        if (this.lifetime - this.age < 15) {
            tails.remove(0);
        }
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    public int getLightColor(int tail, float pPartialTicks) {
        return getLightColor(pPartialTicks);
    }

    protected float getU0(int tail, float pPartialTicks) {
        return  1 - (tail + 1 + pPartialTicks) / (maxTail - 1f);
    }

    protected float getV0(int tail, float pPartialTicks) {
        return 0;
    }

    protected float getU1(int tail, float pPartialTicks) {
        return  1 - (tail + pPartialTicks) / (maxTail - 1f);
    }

    protected float getV1(int tail, float pPartialTicks) {
        return 1;
    }
    protected final float getU0(float pPartialTicks) {
        return 0;
    }
    protected final float getU1(float pPartialTicks) {
        return 0;
    }
    protected final float getV0(float pPartialTicks) {
        return 0;
    }
    protected final float getV1(float pPartialTicks) {
        return 0;
    }
}
