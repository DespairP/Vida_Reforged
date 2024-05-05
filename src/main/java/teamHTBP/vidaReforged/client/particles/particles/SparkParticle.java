package teamHTBP.vidaReforged.client.particles.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

import static teamHTBP.vidaReforged.client.RenderTypeHandler.EMBER_RENDER;

public class SparkParticle extends VidaBaseParticle {
    public SparkParticle(ClientLevel level, double x, double y, double z, double speedX, double speedY, double speedZ, VidaParticleAttributes attributes) {
        super(level, x, y ,z, 0, 0, 0, attributes);
        this.setParticleSpeed(0,0,0);
    }

    @Override
    public int getLightColor(float pTicks) {
        return 255;
    }


    @Override
    public void tick() {
        super.tick();
        if(this.age < 40){
            this.alpha += 0.025f;
            return;
        }
        float lifeDecreaseStep = (float) this.age / (float) this.lifetime;
        this.alpha = 1.0f - lifeDecreaseStep;
    }


    @Override
    public ParticleRenderType getRenderType() {
        return EMBER_RENDER;
    }
}
