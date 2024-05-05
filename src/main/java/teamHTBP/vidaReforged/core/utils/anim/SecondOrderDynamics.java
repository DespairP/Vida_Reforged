package teamHTBP.vidaReforged.core.utils.anim;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import static java.lang.Math.PI;

public class SecondOrderDynamics {
    private Vector3f xp;
    /**y是最终计算的结果*/
    private Vector3f y, yd;
    private float k1, k2, k3;

    /**
     * @param f 系数
     * @param r 系数
     * @param z 系数，相应度，=0带有加速的反应,=1迅速反应,>1回弹
     * @param x0 初始值
     * */
    public SecondOrderDynamics(float f, float z, float r, Vector3f x0){
        // 计算常系数
        k1 = z / ((float) PI * f);
        k2 = 1f / ((2f * (float)PI * f) * (2 * (float)PI * f));
        k3 = r * z / (2f * (float)PI * f);
        //
        xp = x0;
        y = x0;
        yd = new Vector3f(0);
    }


    public Vector3f update(float T, Vector3f x, Vector3f xd){
        if(xd == null){
            xd = new Vector3f(x).sub(xp).div(T);
            xp = x;
        }
        if(y.x() != y.x() || y.y() != y.y() || y.z() != y.z()){
            y = new Vector3f(0,0,0);
        }
        if(yd.x() != yd.x() || yd.y() != yd.y() || yd.z() != yd.z()){
            yd = new Vector3f(0,0,0);
        }
        float k2Stable = Math.max(k2, 1.1f * (T * T/4 + T * k1 / 2));
        // y = y + T * yd
        y = new Vector3f(y).add(new Vector3f(yd).mul(T));
        // yd = yd + T * (x + k3*xd - y - k1*yd) / k2;
        yd = new Vector3f(yd).add( (new Vector3f(x).add(new Vector3f(xd).mul(k3)).sub(y).sub(new Vector3f(yd).mul(k1))).mul(T).div(k2Stable) );
        return y;
    }

    public Vector3f currentPos(){
        return new Vector3f(y);
    }

}
