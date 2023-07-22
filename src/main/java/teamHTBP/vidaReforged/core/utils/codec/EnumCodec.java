package teamHTBP.vidaReforged.core.utils.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

/**
 * @author TT432
 */
public class EnumCodec<T extends Enum<T>> implements Codec<T> {
    Class<T> enumClass;

    public EnumCodec(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public <T1> DataResult<Pair<T, T1>> decode(DynamicOps<T1> ops, T1 input) {
        return ops.getStringValue(input).map(s ->  Pair.of(Enum.valueOf(enumClass, s), ops.empty()));
    }

    @Override
    public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix) {
        return ops.mergeToPrimitive(prefix, ops.createString(input.name()));
    }

    @Override
    public String toString() {
        return "Enum";
    }
}
