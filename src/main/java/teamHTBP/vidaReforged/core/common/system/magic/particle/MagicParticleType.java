package teamHTBP.vidaReforged.core.common.system.magic.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * @author TT432
 */
public class MagicParticleType {
    // TODO
    public static  Codec<MagicParticleType> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.BOOL.fieldOf("aa").forGetter(mpt -> true)
    ).apply(ins , a -> new MagicParticleType()));
}
