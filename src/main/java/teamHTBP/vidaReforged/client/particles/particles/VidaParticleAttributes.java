package teamHTBP.vidaReforged.client.particles.particles;

import net.minecraft.core.particles.ParticleType;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.options.ParticleOptionType;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

import java.util.function.Supplier;

/**
 * */
public record VidaParticleAttributes(int lifeTime, float scale, ARGBColor color,Vector3f toPos) {
    @Override
    public int lifeTime() {
        return lifeTime;
    }

    @Override
    public float scale() {
        return scale;
    }

    @Override
    public ARGBColor color() {
        return color;
    }

    @Override
    public Vector3f toPos() {
        return toPos;
    }
}
