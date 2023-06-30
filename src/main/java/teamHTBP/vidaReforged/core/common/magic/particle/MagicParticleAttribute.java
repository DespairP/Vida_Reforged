package teamHTBP.vidaReforged.core.common.magic.particle;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TT432
 */
@AllArgsConstructor
public class MagicParticleAttribute {
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

    public static final class Modifier {
        float value;
        Operator operator;
    }

    public enum Operator {
        ADD,
        MUL
    }
}
