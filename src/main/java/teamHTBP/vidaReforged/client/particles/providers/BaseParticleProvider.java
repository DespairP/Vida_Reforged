package teamHTBP.vidaReforged.client.particles.providers;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;

/**粒子提供工厂*/
public class BaseParticleProvider implements ParticleProvider<BaseParticleType> {
    /**贴图集*/
    private SpriteSet spriteSet;

    private Class<? extends Particle> particleClass;
    /***/
    public BaseParticleProvider(SpriteSet spriteSet, Class<? extends Particle> particleClass) {
        this.spriteSet = spriteSet;
        this.particleClass = particleClass;
    }

    @Nullable
    @Override
    public Particle createParticle(BaseParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        return null;
    }
}
