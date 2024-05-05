package teamHTBP.vidaReforged.client.particles.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import teamHTBP.vidaReforged.client.RenderTypeHandler;

public class TinkleParticle extends VidaBaseParticle{
    private float initSize = 0;

    public TinkleParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, VidaParticleAttributes attributes) {
        super(level, x, y, z, 0, 0, 0);
        this.hasPhysics = true;
        this.attributes = attributes;

        if (attributes.color() != null) {
            this.alpha = attributes.color().a() * 1.0f / 255.0f;
            this.rCol = attributes.color().r() * 1.0f / 255.0f;
            this.gCol = attributes.color().g() * 1.0f / 255.0f;
            this.bCol = attributes.color().b() * 1.0f / 255.0f;
        }
        this.quadSize = attributes.scale();
        this.initSize = attributes.scale();
        this.lifetime = attributes.lifeTime();
        this.gravity = (float) yd;
    }

    public void tick() {
        if (this.age >= this.lifetime) {
            this.remove();
        }

        this.age ++;

        float lifePercent = (float) this.age / (float) this.lifetime;
        this.quadSize = initSize + 0.6F * lifePercent;
        this.alpha = (1.0f - lifePercent);
    }


    @Override
    public ParticleRenderType getRenderType() {
        return RenderTypeHandler.PARTICLE_SHEET_NO_MASK;
    }
}
