package teamHTBP.vidaReforged.client.particles.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.TwoValueGradientColor;

import java.util.Random;

public class Cube2DParticle extends VidaBaseParticle{
    private float initScale;
    private float initAlpha;
    private boolean rollBack;
    private float rollSpeed;
    private TwoValueGradientColor gradient;

    public Cube2DParticle(ClientLevel level, double x, double y, double z, double speedX, double speedY, double speedZ, VidaParticleAttributes attributes) {
        super(level, x, y, z, 0, 0, 0);
        this.hasPhysics = false;
        this.attributes = attributes;

        if (attributes.color() != null) {
            this.alpha = attributes.color().a()/ 255.0f;
            this.rCol = attributes.color().r() / 255.0f;
            this.gCol = attributes.color().g() / 255.0f;
            this.bCol = attributes.color().b() / 255.0f;
            this.initAlpha = attributes.color().a() / 255.0f;
        }

        if(attributes.color() != null && attributes.toColor() != null){
            this.gradient = new TwoValueGradientColor(attributes.color(), attributes.toColor());
        }

        Random random = new Random();
        this.rollSpeed = random.nextFloat() * 0.05f;
        this.roll = random.nextInt(180) * (random.nextBoolean() ? 1 : -1);
        this.rollBack = random.nextBoolean();
        this.oRoll = this.roll;
        this.lifetime = attributes.lifeTime();
        this.quadSize = attributes.scale();
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

        if(this.gradient != null){
            ARGBColor newColor = this.gradient.getColor(lifePercent);
            this.rCol = newColor.getR() / 255.0f;
            this.gCol = newColor.getG() / 255.0f;
            this.bCol = newColor.getB() / 255.0f;
        }

        this.oRoll = roll;
        roll += rollSpeed * (rollBack ? 1 : -1);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
