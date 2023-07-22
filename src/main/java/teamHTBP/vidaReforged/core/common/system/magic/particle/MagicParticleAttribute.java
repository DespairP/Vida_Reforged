package teamHTBP.vidaReforged.core.common.system.magic.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TT432
 */
@AllArgsConstructor
public class MagicParticleAttribute {
    public static final Codec<MagicParticleAttribute> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.FLOAT.fieldOf("baseValue").forGetter(mpa -> mpa.baseValue),
            Modifier.CODEC.listOf().fieldOf("modifier").forGetter(mpa -> mpa.modifier)
    ).apply(ins, MagicParticleAttribute::new));

    float baseValue;
    List<Modifier> modifier = new ArrayList<>();

    public MagicParticleAttribute(float baseValue) {
        this.baseValue = baseValue;
    }

    public float value() {
        float result = baseValue;
        float mulNum = 1;

        for (Modifier m : modifier) {
            if (m.operator == Operator.ADD)
                result += m.value;
            else if (m.operator == Operator.MUL)
                mulNum += m.value;
        }

        return result * (mulNum);
    }

    @AllArgsConstructor
    public static final class Modifier {
        public static final Codec<Modifier> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                Codec.FLOAT.fieldOf("value").forGetter(mod -> mod.value),
                Codec.BOOL.fieldOf("operator").forGetter(mod -> mod.operator == Operator.ADD)
        ).apply(ins, (v, o) -> new Modifier(v, o ? Operator.ADD : Operator.MUL)));

        float value;
        Operator operator;
    }

    public enum Operator {
        ADD,
        MUL
    }
}
