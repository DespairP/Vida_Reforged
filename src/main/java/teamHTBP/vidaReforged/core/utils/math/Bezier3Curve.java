package teamHTBP.vidaReforged.core.utils.math;

import lombok.AllArgsConstructor;
import org.joml.Vector3d;

/**
 * @author DustW
 */
@AllArgsConstructor
public class Bezier3Curve extends Curve {
    public final Vector3d pos0;
    public final Vector3d pos1;
    public final Vector3d pos2;
    public final Vector3d pos3;

    @Override
    public Vector3d getPoint(float t) {
        return bezier3(pos0, pos1, pos2, pos3, t);
    }

    /**
     * 进行贝塞尔曲线插值
     * @param pos0 控制点
     * @param pos1 控制点
     * @param pos2 控制点
     * @param pos3 控制点
     * @param t    0~1 时间
     * @return     插值结果
     */
    public static Vector3d bezier3(Vector3d pos0, Vector3d pos1, Vector3d pos2, Vector3d pos3, double t) {
        double k = 1 - t;

        Vector3d pos0f = pos0.mul(k * k * k);
        Vector3d pos1f = pos1.mul(3 * t * k * k);
        Vector3d pos2f = pos2.mul(3 * t * t * k);
        Vector3d pos3f = pos3.mul(t * t * t);

        return pos0f.add(pos1f).add(pos2f).add(pos3f);
    }
}
