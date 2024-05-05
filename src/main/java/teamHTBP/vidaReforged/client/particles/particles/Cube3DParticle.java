package teamHTBP.vidaReforged.client.particles.particles;

import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Random;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.VidaColor;

/**
 * 3D方块粒子
 *
 * */
public class Cube3DParticle extends VidaBaseParticle {
    private int type = 0;
    private float spinSpeed = 0;
    private float alterSpinSpeed = 0;

    public Cube3DParticle(ClientLevel level, double x, double y, double z, double speedX, double speedY, double speedZ, VidaParticleAttributes attributes) {
        super(level, x, y ,z, speedX, speedY, speedZ, attributes);
        this.quadSize = 0.1f;
        this.type = rand.nextInt(10);
        this.hasPhysics = true;
        this.spinSpeed = rand.nextInt(8) * 0.01f;
        this.alterSpinSpeed = rand.nextInt(8) * 0.01f;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void render(VertexConsumer buffer, Camera pRenderInfo, float partialTicks) {
        Vec3 vec3d = pRenderInfo.getPosition();
        float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3d.x());
        float f1 = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3d.y());
        float f2 = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3d.z());

        Quaternionf quaternion = new Quaternionf(pRenderInfo.rotation());

        Vector3f[] avector3f = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, -1.0F), new Vector3f(-1.0F, -1.0F, 1.0F), new Vector3f(1.0F, -1.0F, 1.0F), new Vector3f(1.0F, -1.0F, -1.0F),
                new Vector3f(-1.0F, 1.0F, -1.0F), new Vector3f(-1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, -1.0F)
        };
        float f4 = this.getQuadSize(partialTicks);

        for (int i = 0; i < avector3f.length; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(Axis.YP.rotation(this.yaw));
            vector3f.rotate(Axis.XP.rotation(this.roll));

            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float f7 = this.getU0();
        float f8 = this.getU1();
        float f5 = this.getV0();
        float f6 = this.getV1();
        int j = this.getLightColor(partialTicks);
        //下面(正反面都要渲染)
        buffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f8, f6).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f8, f5).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f7, f5).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f7, f6).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();

        buffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f8, f6).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f8, f5).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f7, f5).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f7, f6).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        //背面
        buffer.vertex(avector3f[7].x(), avector3f[7].y(), avector3f[7].z()).uv(f7, f6).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f7, f5).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f8, f5).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[4].x(), avector3f[4].y(), avector3f[4].z()).uv(f8, f6).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();

        //左侧
        buffer.vertex(avector3f[4].x(), avector3f[4].y(), avector3f[4].z()).uv(f7, f6).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f7, f5).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f8, f5).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[5].x(), avector3f[5].y(), avector3f[5].z()).uv(f8, f6).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        //正面
        buffer.vertex(avector3f[5].x(), avector3f[5].y(), avector3f[5].z()).uv(f8, f6).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f8, f5).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f7, f5).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[6].x(), avector3f[6].y(), avector3f[6].z()).uv(f7, f6).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();

        //右侧
        buffer.vertex(avector3f[6].x(), avector3f[6].y(), avector3f[6].z()).uv(f8, f6).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f8, f5).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f7, f5).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[7].x(), avector3f[7].y(), avector3f[7].z()).uv(f7, f6).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();

        //上面(正反面都要渲染)
        buffer.vertex(avector3f[4].x(), avector3f[4].y(), avector3f[4].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[5].x(), avector3f[5].y(), avector3f[5].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[6].x(), avector3f[6].y(), avector3f[6].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[7].x(), avector3f[7].y(), avector3f[7].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();

        buffer.vertex(avector3f[7].x(), avector3f[7].y(), avector3f[7].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[6].x(), avector3f[6].y(), avector3f[6].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[5].x(), avector3f[5].y(), avector3f[5].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[4].x(), avector3f[4].y(), avector3f[4].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
    }


    /**
     * 建立节点
     * @param buffer 节点缓冲器
     * @param vec    矢量坐标
     * @param u      U
     * @param v      V
     * @param colorFactor 颜色深度,最大为1
     * @param lightning   光照
     */
    public void buildVertex(VertexConsumer buffer, Vector3f vec, float u, float v, float colorFactor, int lightning){
        buffer.vertex(vec.x(), vec.y(), vec.z()).uv(u, v).color(this.rCol * colorFactor, this.gCol * colorFactor, this.bCol * colorFactor, this.alpha).uv2(lightning).endVertex();
    }


    @Override
    public void move(double pX, double pY, double pZ) {
        super.move(pX, pY, pZ);
    }

    @Override
    public void tick() {
        super.tick();
        this.quadSize -= 0.0001f;
        if (!this.onGround){
            switch (type) {
                case 1 -> {
                    this.roll += this.spinSpeed;
                    this.yaw += this.spinSpeed;
                }
                case 2 -> {
                    this.roll += this.spinSpeed;
                    this.yaw += this.spinSpeed;
                    this.pitch += this.spinSpeed;
                }
                case 3 -> {
                    this.roll += this.alterSpinSpeed;
                    this.yaw += this.spinSpeed;
                    this.pitch += this.spinSpeed;
                }
                case 4 -> {
                    this.roll += this.alterSpinSpeed;
                    this.yaw += this.alterSpinSpeed;
                    this.pitch += this.spinSpeed;
                }
                case 5 -> {
                    this.roll += this.alterSpinSpeed;
                    this.yaw += this.alterSpinSpeed;
                    this.pitch += this.alterSpinSpeed;
                }
                case 6 -> {
                    this.yaw += this.spinSpeed;
                    this.pitch += this.alterSpinSpeed;
                }
                case 7 -> {
                    this.pitch += this.alterSpinSpeed;
                }
                case 8 -> {
                    this.roll += this.spinSpeed;
                    this.pitch += this.alterSpinSpeed;
                }
                case 9 -> {
                    this.pitch += this.spinSpeed;
                }
                case 10 -> {}
            }
        }
        if (this.roll >= 360) this.roll = 0;
        if (this.yaw >= 360) this.yaw = 0;
        if (this.pitch >= 360) this.pitch = 0;
        if (this.onGround) {
            this.quadSize -= 0.0005f;
            this.alpha -= 0.1f;
        }
        if (this.alpha <= 0)
            this.alpha = 0;
        if (this.quadSize <= 0)
            this.quadSize = 0;
    }
}
