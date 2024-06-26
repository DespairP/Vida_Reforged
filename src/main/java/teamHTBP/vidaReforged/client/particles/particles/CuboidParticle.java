package teamHTBP.vidaReforged.client.particles.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;

public class CuboidParticle extends VidaBaseParticle {
    private float spinSpeed = 0;
    private float rotation = 0;
    private float extraYLength = 0;


    public CuboidParticle(ClientLevel level, double x, double y, double z, double speedX, double speedY, double speedZ, VidaParticleAttributes attributes) {
        super(level, x, y ,z, speedX, speedY, speedZ, attributes);
        this.xd = speedX;
        this.yd = speedY;
        this.zd = speedZ;
        this.spinSpeed = rand.nextInt(8);
        this.lifetime = attributes.lifeTime() <= 0 ? rand.nextInt(150) + 30 : attributes.lifeTime();
        this.extraYLength = rand.nextInt(4) + 3;
    }

    @Override
    public void render(VertexConsumer buffer, Camera cameraInfo, float partialTicks) {
        Vec3 vec3d = cameraInfo.getPosition();
        float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3d.x());
        float f1 = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3d.y());
        float f2 = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3d.z());


        Quaternionf quaternion = Axis.YN.rotation(this.rotation);


        Vector3f[] avector3f = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F + extraYLength, 0.0F), new Vector3f(1.0F, 1.0F + extraYLength, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F + extraYLength, 0.0F), new Vector3f(-1.0F, 1.0F + extraYLength, 2.0F), new Vector3f(-1.0F, -1.0F, 2.0F),
                new Vector3f(-1.0F, -1.0F, 2.0F), new Vector3f(-1.0F, 1.0F + extraYLength, 2.0F), new Vector3f(1.0F, 1.0F + extraYLength, 2.0F), new Vector3f(1.0F, -1.0F, 2.0F),
                new Vector3f(1.0F, -1.0F, 2.0F), new Vector3f(1.0F, 1.0F + extraYLength, 2.0F), new Vector3f(1.0F, 1.0F + extraYLength, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F + extraYLength, 2.0F), new Vector3f(-1.0F, 1.0F + extraYLength, 0.0F), new Vector3f(1.0F, 1.0F + extraYLength, 0.0F), new Vector3f(1.0F, 1.0F + extraYLength, 2.0F),
                new Vector3f(-1.0F, -1.0F, 1.0F), new Vector3f(-1.0F, -1.0F, -1.0F), new Vector3f(1.0F, -1.0F, -1.0F), new Vector3f(1.0F, -1.0F, 1.0F)

        };

        Vector3f[] vec = new Vector3f[]{
                //后面
                new Vector3f(-1.0F, -1.0F, -1.0F), new Vector3f(-1.0F, 1.0F + extraYLength, -1.0F), new Vector3f(1.0F, 1.0F + extraYLength, -1.0F), new Vector3f(1.0F, -1.0F, -1.0F),
                //前面
                new Vector3f(-1.0F, -1.0F, 1.0F), new Vector3f(-1.0F, 1.0F + extraYLength, 1.0F), new Vector3f(1.0F, 1.0F + extraYLength, 1.0F), new Vector3f(1.0F, -1.0F, 1.0F)

        };
        float f4 = 0.06F * this.getQuadSize(partialTicks);

        for (Vector3f vector3f : vec) {
            vector3f.rotate(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float f7 = this.getU0();
        float f8 = this.getU1();
        float f5 = this.getV0();
        float f6 = this.getV1();
        int j = this.getLightColor(partialTicks);
        //下面
        buffer.vertex(vec[4].x(), vec[4].y(), vec[4].z()).uv(f8, f6).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[0].x(), vec[0].y(), vec[0].z()).uv(f8, f5).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[3].x(), vec[3].y(), vec[3].z()).uv(f7, f5).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[7].x(), vec[7].y(), vec[7].z()).uv(f7, f6).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();

        //背面
        buffer.vertex(vec[0].x(), vec[0].y(), vec[0].z()).uv(f8, f6).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[1].x(), vec[1].y(), vec[1].z()).uv(f8, f5).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[2].x(), vec[2].y(), vec[2].z()).uv(f7, f5).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[3].x(), vec[3].y(), vec[3].z()).uv(f7, f6).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();

        //左侧
        buffer.vertex(vec[5].x(), vec[5].y(), vec[5].z()).uv(f8, f6).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[1].x(), vec[1].y(), vec[1].z()).uv(f8, f5).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[0].x(), vec[0].y(), vec[0].z()).uv(f7, f5).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[4].x(), vec[4].y(), vec[4].z()).uv(f7, f6).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();

        //正面
        buffer.vertex(vec[5].x(), vec[5].y(), vec[5].z()).uv(f8, f6).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[4].x(), vec[4].y(), vec[4].z()).uv(f8, f5).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[7].x(), vec[7].y(), vec[7].z()).uv(f7, f5).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[6].x(), vec[6].y(), vec[6].z()).uv(f7, f6).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();

        //右侧
        buffer.vertex(vec[2].x(), vec[2].y(), vec[2].z()).uv(f8, f6).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[6].x(), vec[6].y(), vec[6].z()).uv(f8, f5).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[7].x(), vec[7].y(), vec[7].z()).uv(f7, f5).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[3].x(), vec[3].y(), vec[3].z()).uv(f7, f6).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();

        //上面
        buffer.vertex(vec[1].x(), vec[1].y(), vec[1].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[5].x(), vec[5].y(), vec[5].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[6].x(), vec[6].y(), vec[6].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(vec[2].x(), vec[2].y(), vec[2].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.y += this.yd;
            this.x += this.xd;
            this.z += this.zd;
            this.rotation += 0.01f * spinSpeed;
            this.rotation %= 360;
            if (this.lifetime - 20 < this.age) this.alpha = Math.max(this.alpha - 0.05f, 0);
        }
    }
}
