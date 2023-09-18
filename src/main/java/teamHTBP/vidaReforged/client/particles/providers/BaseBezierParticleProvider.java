package teamHTBP.vidaReforged.client.particles.providers;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.client.particles.options.BaseBezierParticleType;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.particles.BezierParticle;

public class BaseBezierParticleProvider  implements ParticleProvider<BaseBezierParticleType> {
    private SpriteSet spriteSet;

    private Class<? extends Particle> particleClass;
    /***/
    public BaseBezierParticleProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }
    @Nullable
    @Override
    public Particle createParticle(BaseBezierParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        return new BezierParticle(pLevel,pX,pY,pZ,pXSpeed,pYSpeed,pZSpeed, pType.getAlpha(), pType.getColorRed(), pType.getColorGreen(), pType.getColorBlue(), pType.age, (int) pType.size, pType.getTails());
    }
}
