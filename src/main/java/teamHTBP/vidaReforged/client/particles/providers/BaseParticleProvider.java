package teamHTBP.vidaReforged.client.particles.providers;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.particles.Cube3DParticle;
import teamHTBP.vidaReforged.client.particles.particles.CuboidParticle;
import teamHTBP.vidaReforged.client.particles.particles.VidaParticleAttributes;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.reg.RegisterParticleType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

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
        for(Field field : VidaParticleTypeLoader.class.getDeclaredFields()){
            //
            if(!field.isAnnotationPresent(RegisterParticleType.class)){
                continue;
            }
            try {
                RegistryObject<ParticleType<BaseParticleType>> type = (RegistryObject<ParticleType<BaseParticleType>>)field.get(null);
                if(pType.getType() == type.get()){
                    Class<? extends TextureSheetParticle> particleClass = field.getAnnotation(RegisterParticleType.class).value();
                    Constructor<? extends TextureSheetParticle> constructor = particleClass.getConstructor(
                            ClientLevel.class,
                            double.class,
                            double.class,
                            double.class,
                            double.class,
                            double.class,
                            double.class,
                            VidaParticleAttributes.class
                    );
                    TextureSheetParticle particle = constructor.newInstance(
                            pLevel,
                            pX,
                            pY,
                            pZ,
                            pXSpeed,
                            pYSpeed,
                            pZSpeed,
                            new VidaParticleAttributes(
                                    pType.getLifeTime(),
                                    pType.getScale(),
                                    new ARGBColor(
                                        pType.getAlpha(),
                                        pType.getColorRed(),
                                        pType.getColorGreen(),
                                        pType.getColorBlue()
                                    ),
                                    pType.getToColor(),
                                    pType.getToPos()
                            )
                    );
                    particle.pickSprite(spriteSet);
                    return particle;
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return null;
    }
}
