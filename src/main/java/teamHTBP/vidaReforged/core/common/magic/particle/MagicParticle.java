package teamHTBP.vidaReforged.core.common.magic.particle;

import teamHTBP.vidaReforged.core.api.IVidaElement;
import teamHTBP.vidaReforged.core.api.VidaElement;

/**
 * @author TT432
 */
public record MagicParticle(
        int colorA,
        int colorB,
        MagicParticleAttribute speed,
        MagicParticleAttribute amount,
        MagicParticleAttribute maxAge,
        MagicParticleType type,
        MagicParticleAttribute damage,
        IVidaElement element
) {

    // TODO  序列化反序列化

    public static final MagicParticle EMPTY = new MagicParticle(
            0xFF_FF_FF_FF,
            0xFF_FF_FF_FF,
            new MagicParticleAttribute(0.2F),
            new MagicParticleAttribute(10),
            new MagicParticleAttribute(200),
            new MagicParticleType(),
            new MagicParticleAttribute(3),
            VidaElement.EMPTY
    );
}
