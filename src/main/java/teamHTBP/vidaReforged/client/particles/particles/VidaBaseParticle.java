package teamHTBP.vidaReforged.client.particles.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.RandomSource;

public abstract class VidaBaseParticle extends TextureSheetParticle {
    protected VidaParticleAttributes attributes;
    protected RandomSource rand;

    protected float yaw;
    protected float oYaw;

    protected float pitch;
    protected float oPitch;

    protected VidaBaseParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    protected VidaBaseParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);
    }

    protected VidaBaseParticle(ClientLevel level, double x, double y, double z, VidaParticleAttributes attributes){
        this(level, x, y, z);
        this.attributes = attributes;
    }

    protected VidaBaseParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, VidaParticleAttributes attributes){
        this(level, x, y, z, xd, yd, zd);
        this.rand = RandomSource.create();
        this.attributes = attributes;
        this.alpha = attributes.color().a() / 255.0f;
        this.rCol = attributes.color().r() / 255.0f;
        this.gCol = attributes.color().g() / 255.0f;
        this.bCol = attributes.color().b() / 255.0f;
        this.lifetime = attributes.lifeTime() < 0 ? lifetime : attributes.lifeTime();
        this.quadSize = attributes.scale();
    }


}
