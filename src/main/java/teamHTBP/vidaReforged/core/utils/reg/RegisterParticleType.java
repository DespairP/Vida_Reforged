package teamHTBP.vidaReforged.core.utils.reg;

import net.minecraft.client.particle.Particle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RegisterParticleType {
    Class<? extends Particle> value();
}
