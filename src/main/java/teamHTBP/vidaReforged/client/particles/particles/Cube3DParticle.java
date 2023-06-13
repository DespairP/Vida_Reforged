package teamHTBP.vidaReforged.client.particles.particles;

import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.VidaColor;

/**
 * 3D方块粒子
 *
 * */
public class Cube3DParticle extends TextureSheetParticle {

    protected Cube3DParticle(ClientLevel level, double x, double y, double z, int a, int r, int g, int b, int size) {
        super(level, x, y, z);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void render(VertexConsumer buffer, Camera pRenderInfo, float partialTicks) {
         Vector3f[] cubeVector3f = new Vector3f[]{
                //背面
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 2.0F), new Vector3f(-1.0F, -1.0F, 2.0F),
                new Vector3f(-1.0F, -1.0F, 2.0F), new Vector3f(-1.0F, 1.0F, 2.0F), new Vector3f(1.0F, 1.0F, 2.0F), new Vector3f(1.0F, -1.0F, 2.0F),
                new Vector3f(1.0F, -1.0F, 2.0F), new Vector3f(1.0F, 1.0F, 2.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 2.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 2.0F),
                new Vector3f(-1.0F, -1.0F, 2.0F), new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 2.0F)

        };

        //uv
        float f7 = this.getU0();
        float f8 = this.getU1();
        float f5 = this.getV0();
        float f6 = this.getV1();

        //lightning
        int j = this.getLightColor(partialTicks);

        //下面(正反面都要渲染)
        buffer.vertex(cubeVector3f[0].x(), cubeVector3f[0].y(), cubeVector3f[0].z()).uv(f8, f6).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[8].x(), cubeVector3f[8].y(), cubeVector3f[8].z()).uv(f8, f5).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[11].x(), cubeVector3f[11].y(), cubeVector3f[11].z()).uv(f7, f5).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[3].x(), cubeVector3f[3].y(), cubeVector3f[3].z()).uv(f7, f6).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();

        buffer.vertex(cubeVector3f[3].x(), cubeVector3f[3].y(), cubeVector3f[3].z()).uv(f8, f6).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[11].x(), cubeVector3f[11].y(), cubeVector3f[11].z()).uv(f8, f5).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[8].x(), cubeVector3f[8].y(), cubeVector3f[8].z()).uv(f7, f5).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[0].x(), cubeVector3f[0].y(), cubeVector3f[0].z()).uv(f7, f6).color(this.rCol * 0.5f, this.gCol * 0.5f, this.bCol * 0.5f, this.alpha).uv2(j).endVertex();

        //背面
        buffer.vertex(cubeVector3f[0].x(), cubeVector3f[0].y(), cubeVector3f[0].z()).uv(f8, f6).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[1].x(), cubeVector3f[1].y(), cubeVector3f[1].z()).uv(f8, f5).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[2].x(), cubeVector3f[2].y(), cubeVector3f[2].z()).uv(f7, f5).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[3].x(), cubeVector3f[3].y(), cubeVector3f[3].z()).uv(f7, f6).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();

        //左侧
        buffer.vertex(cubeVector3f[1].x(), cubeVector3f[1].y(), cubeVector3f[1].z()).uv(f8, f6).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[0].x(), cubeVector3f[0].y(), cubeVector3f[0].z()).uv(f8, f5).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[8].x(), cubeVector3f[8].y(), cubeVector3f[8].z()).uv(f7, f5).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[9].x(), cubeVector3f[9].y(), cubeVector3f[9].z()).uv(f7, f6).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();

        //正面
        buffer.vertex(cubeVector3f[11].x(), cubeVector3f[11].y(), cubeVector3f[11].z()).uv(f8, f6).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[10].x(), cubeVector3f[10].y(), cubeVector3f[10].z()).uv(f8, f5).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[9].x(), cubeVector3f[9].y(), cubeVector3f[9].z()).uv(f7, f5).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[8].x(), cubeVector3f[8].y(), cubeVector3f[8].z()).uv(f7, f6).color(this.rCol * 0.8f, this.gCol * 0.8f, this.bCol * 0.8f, this.alpha).uv2(j).endVertex();

        //右侧
        buffer.vertex(cubeVector3f[10].x(), cubeVector3f[10].y(), cubeVector3f[10].z()).uv(f8, f6).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[11].x(), cubeVector3f[11].y(), cubeVector3f[11].z()).uv(f8, f5).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[3].x(), cubeVector3f[3].y(), cubeVector3f[3].z()).uv(f7, f5).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[2].x(), cubeVector3f[2].y(), cubeVector3f[2].z()).uv(f7, f6).color(this.rCol * 0.6f, this.gCol * 0.6f, this.bCol * 0.6f, this.alpha).uv2(j).endVertex();

        //上面(正反面都要渲染)
        buffer.vertex(cubeVector3f[2].x(), cubeVector3f[2].y(), cubeVector3f[2].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[10].x(), cubeVector3f[10].y(), cubeVector3f[10].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[9].x(), cubeVector3f[9].y(), cubeVector3f[9].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[1].x(), cubeVector3f[1].y(), cubeVector3f[1].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();

        buffer.vertex(cubeVector3f[1].x(), cubeVector3f[1].y(), cubeVector3f[1].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[9].x(), cubeVector3f[9].y(), cubeVector3f[9].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[10].x(), cubeVector3f[10].y(), cubeVector3f[10].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(cubeVector3f[2].x(), cubeVector3f[2].y(), cubeVector3f[2].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
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
}
