package teamHTBP.vidaReforged.core.utils.animation;

/**
 * 动画机
 * 具体使用见：{@link  DestinationAnimator#of}
 * */
public abstract class Animator<T> implements IAnimator{
    /**是否真正开始*/
    protected boolean isStarted = false;
    /**是否结束*/
    protected boolean isEnded = false;
    /**是否开始帧动画*/
    protected boolean isRunning = true;
    /**线性插值器*/
    protected TimeInterpolator interpolator;
    /**变量*/
    protected T value;
    /**偏移帧*/
    protected double delayFrames = 0;
    /**持续帧*/
    protected double existingTick = 0;
    /***/
    protected boolean isReverse = false;
    /***/
    protected float maxTick = 6;

    @Override
    public void start() {
        this.isStarted = true;
        this.isRunning = true;
    }

    @Override
    public void pause() {
        if(this.isStarted && this.isRunning){
            this.isStarted = false;
        }
        this.isRunning = false;
    }

    @Override
    public void end() {
        this.isRunning = false;
        this.isEnded = true;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void setFrameDelay(long frameDelay){
        this.delayFrames = frameDelay;
    }

    @Override
    public abstract void tick(float partialTicks);

    /**倒放*/
    public void reverse(){
        this.isReverse = !this.isReverse;
    }

    public T getValue(){
       return this.value;
    }
}
