package teamHTBP.vidaReforged.client.particles.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import teamHTBP.vidaReforged.client.renderer.RenderTypeHandler;

/**飘动*/
public class LeavesLikeParticle extends VidaBaseParticle{
    private float rotSpeed;
    private final float particleRandom;
    private final float spinAcceleration;
    private final int maxLifeTime;
    private final int directX;
    private final int directZ;


    public LeavesLikeParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, VidaParticleAttributes attributes) {
        super(level, x, y, z, attributes);
        this.rotSpeed = (float)Math.toRadians(this.rand.nextBoolean() ? -30.0D : 30.0D);
        this.particleRandom = this.rand.nextFloat();
        this.spinAcceleration = (float)Math.toRadians(this.rand.nextBoolean() ? -5.0D : 5.0D);
        this.directX = this.rand.nextBoolean() ? 1 : -1;
        this.directZ = this.rand.nextBoolean() ? 1 : -1;
        this.maxLifeTime = rand.nextInt(200, 300);
        this.lifetime = maxLifeTime;
        this.gravity = 7.0E-4F;
        this.quadSize = 0.1f;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return RenderTypeHandler.PARTICLE_SHEET_NO_MASK;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        }

        if (!this.removed) {
            float f = (float)(maxLifeTime - this.lifetime);
            float f1 = Math.min(f / maxLifeTime, 1.0F);
            double d0 = Math.cos(Math.toRadians((double)(this.particleRandom * 60.0F))) * 2.0D * Math.pow((double)f1, 1.25D) * directX;
            double d1 = Math.sin(Math.toRadians((double)(this.particleRandom * 60.0F))) * 2.0D * Math.pow((double)f1, 1.25D) * directZ;
            this.xd += d0 * (double)0.0025F;
            this.zd += d1 * (double)0.0025F;
            this.yd -= (double)this.gravity;
            this.rotSpeed += this.spinAcceleration / 20.0F;
            this.oRoll = this.roll;
            this.roll += this.rotSpeed / 20.0F;
            this.move(this.xd, this.yd, this.zd);
            if (this.onGround || this.lifetime < 299 && (this.xd == 0.0D || this.zd == 0.0D)) {
                this.remove();
            }

            if (!this.removed) {
                this.xd *= (double)this.friction;
                this.yd *= (double)this.friction;
                this.zd *= (double)this.friction;
            }
        }
    }
}
