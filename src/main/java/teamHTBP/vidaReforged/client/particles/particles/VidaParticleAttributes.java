package teamHTBP.vidaReforged.client.particles.particles;

import org.joml.Vector3f;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;


public record VidaParticleAttributes(int lifeTime, float scale, boolean fullBright, ARGBColor color, ARGBColor toColor, Vector3f toPos, Vector3f extraPos) {
    public VidaParticleAttributes(int lifeTime, float scale, ARGBColor color, ARGBColor toColor, Vector3f toPos){
        this(lifeTime, scale, true, color, toColor, toPos, null);
    }

    public VidaParticleAttributes(int lifeTime, float scale, ARGBColor color, ARGBColor toColor, Vector3f toPos, Vector3f extraPos){
        this(lifeTime, scale, true, color, toColor, toPos, extraPos);
    }


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
