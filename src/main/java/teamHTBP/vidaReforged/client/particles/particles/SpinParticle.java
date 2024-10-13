package teamHTBP.vidaReforged.client.particles.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import teamHTBP.vidaReforged.client.RenderTypeHandler;

public class SpinParticle extends VidaBaseParticle{
    private float initScale;
    private float initAlpha;
    private float spinSpeed;

    public SpinParticle(ClientLevel level, double x, double y, double z, double speedX, double speedY, double speedZ, VidaParticleAttributes attributes) {
        super(level, x, y, z, 0, 0, 0);
        this.hasPhysics = false;
        this.attributes = attributes;
        this.spinSpeed = attributes.extraPos() == null ? 0.1f : Math.max(0.1f, attributes.extraPos().x);
        if (attributes.color() != null) {
            this.alpha = attributes.color().a() * 1.0f / 255.0f;
            this.rCol = attributes.color().r() * 1.0f / 255.0f;
            this.gCol = attributes.color().g() * 1.0f / 255.0f;
            this.bCol = attributes.color().b() * 1.0f / 255.0f;
            this.initAlpha = attributes.color().a() * 1.0f / 255.0f;
        }
        this.lifetime = (int) ((float) attributes.lifeTime() * 0.5f);
        this.quadSize = 0;
        this.initScale = attributes.scale();
        this.xd = speedX * 2.0f;
        this.yd = speedY * 2.0f;
        this.zd = speedZ * 2.0f;
    }

    @Override
    public void tick() {
        super.tick();

        if (level.random.nextInt(6) == 0) {
            this.age++;
        }
        float lifePercent = (float) this.age / (float) this.lifetime;
        this.quadSize = initScale - initScale * lifePercent;
        this.alpha = initAlpha * (1.0f - lifePercent);

        this.oRoll = roll;
        roll += this.spinSpeed;
    }

    @Override
    public boolean isAlive() {
        return this.age < this.lifetime;
    }



    @Override
    public ParticleRenderType getRenderType() {
        return RenderTypeHandler.PARTICLE_SHEET_NO_MASK;
    }
}
