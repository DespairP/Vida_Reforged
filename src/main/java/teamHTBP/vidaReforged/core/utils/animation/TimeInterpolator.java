package teamHTBP.vidaReforged.core.utils.animation;


import net.minecraft.util.Mth;
import teamHTBP.vidaReforged.core.utils.math.VidaMath;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

/**
 * 时间插值器
 * @author DustW
 */
@Deprecated
@FunctionalInterface
public interface TimeInterpolator {

    /** 线性插值 */
    TimeInterpolator LINEAR = in -> in;
    /** 加速 */
    TimeInterpolator ACCELERATE = in -> in * in;
    /** 减速 */
    TimeInterpolator DECELERATE = in -> 1.0f - (1.0f - in) * (1.0f - in);
    /** 减速立方 */
    TimeInterpolator DECELERATE_CUBIC = in -> 1.0f - (1.0f - in) * (1.0f - in) * (1.0f - in);
    /** 开始和结尾时减速，中间加速 */
    TimeInterpolator ACCELERATE_DECELERATE = in -> Mth.cos((in + 1.0f) * VidaMath.PI) * 0.5f + 0.5f;
    /** sin */
    TimeInterpolator SINE = in -> (float) (0.5 * (sin((2 * PI * in - PI / 2.0)) + 1));

    TimeInterpolator ANTICIPATE = in -> in * in * (3.0f * in - 2.0f);

    TimeInterpolator OVERSHOOT = in -> (in - 1.0f) * (in - 1.0f) * (3.0f * (in - 1.0f) + 2.0f) + 1.0f;

    /**
     * 获取插值结果
     * @param value 待插值的值 (0 ~ 1)
     * @return      插值的结果
     */
    float interpolation(float value);
}
