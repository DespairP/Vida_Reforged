package teamHTBP.vidaReforged.client.particles.providers;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.particles.Cube3DParticle;
import teamHTBP.vidaReforged.client.particles.particles.CuboidParticle;

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
        if(pType.getType() == VidaParticleTypeLoader.CUBE_PARTICLE_TYPE.get()) {
            Cube3DParticle particleCube = new Cube3DParticle(
                    pLevel,
                    pX,
                    pY,
                    pZ,
                    pXSpeed,
                    pYSpeed,
                    pZSpeed,
                    pType.getAlpha(),
                    pType.getColorRed(),
                    pType.getColorGreen(),
                    pType.getColorBlue(),
                    (int) pType.getSize()
            );
            particleCube.pickSprite(spriteSet);
            return particleCube;
        }
        CuboidParticle particleCuboid = new CuboidParticle(
                pLevel,
                pX,
                pY,
                pZ,
                pXSpeed,
                pYSpeed,
                pZSpeed,
                pType.getAlpha(),
                pType.getColorRed(),
                pType.getColorGreen(),
                pType.getColorBlue(),
                (int) pType.getSize()
        );
        particleCuboid.pickSprite(spriteSet);
        return particleCuboid;
    }
}
