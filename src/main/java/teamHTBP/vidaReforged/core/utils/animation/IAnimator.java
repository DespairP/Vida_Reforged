package teamHTBP.vidaReforged.core.utils.animation;


/**
 * 动画机接口
 */
public interface IAnimator {
    /**开始动画*/
    void start();
    /**暂停动画*/
    void pause();
    /**结束动画*/
    void end();
    /**下一帧*/
    void tick(float partialTicks);
}
