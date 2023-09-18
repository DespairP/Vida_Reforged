package teamHTBP.vidaReforged.core.utils.math;

import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DustW
 */
public abstract class Curve {
    public abstract Vector3d getPoint(float t);

    public List<Vector3d> getPoints(int size) {
        List<Vector3d> points = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            points.add(getPoint((float) i / size));
        }
        return points;
    }
}
