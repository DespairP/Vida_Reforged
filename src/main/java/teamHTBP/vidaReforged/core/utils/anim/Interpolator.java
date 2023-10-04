package teamHTBP.vidaReforged.core.utils.anim;

/**插值器*/
public interface Interpolator {
    /**
     *
     * @param progress 动画进度
     * */
    float getInterpolator(float progress);

    Interpolator LINEAR = (progress) -> progress;

    Interpolator ACCELERATE_DECELERATE = (progress) -> (float)(Math.cos((progress + 1) * Math.PI) / 2.0f) + 0.5f;

}
