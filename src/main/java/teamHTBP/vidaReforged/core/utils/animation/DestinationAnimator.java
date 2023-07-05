package teamHTBP.vidaReforged.core.utils.animation;

import com.google.common.collect.Range;
import teamHTBP.vidaReforged.core.utils.animation.calculator.IValueProvider;

import static java.lang.Math.abs;
import static java.lang.Math.round;

public class DestinationAnimator<T extends Comparable> extends Animator<T>{
    private final T fromValue;
    private final T toValue;
    private final IValueProvider<T> provider;

    @Override
    public void start() {
        if(!this.isEnded){
            this.isStarted = true;
            this.isRunning = true;
        }
    }

    public DestinationAnimator(T fromValue, T toValue, IValueProvider<T> provider, T initialValue, float maxTick, int mode) {
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.provider = provider;
        this.value = initialValue;
        this.maxTick = maxTick;
        this.mode = mode;
    }

    /**
     * 相当于继续动画，需要手动调用下
     * */
    @Override
    public void tick(float partialTicks) {
        //检查是否开始
        if(!this.isStarted || this.isEnded){
            return;
        }

        // 计算动画开始经过了几帧数
        this.isRunning = true;

        // 计算已经过了多久
        float currentTick = (float) existingTick + partialTicks;
        if(this.mode >= 0){
            currentTick = (float) Math.min(maxTick, existingTick + partialTicks);
        }

        this.existingTick = currentTick;

        //延迟帧检查
        if(this.delayFrames > this.existingTick){
            return;
        }

        // 三角波函数：f(x) = 1 - 2 |round(1 / t) * x - (1 / t) * x|
        float percent = squareWave(currentTick, maxTick * 2.0);
        // value = from + (to - from) * percent
        this.value =
                provider.addValue(
                        this.fromValue,
                        (float) provider.multiplyValue(provider.minusValue(this.toValue,(float) this.fromValue), this.interpolator.interpolation((percent)))
                );
        // 检查是否越界了，如果已经越界，动画结束
        Range<T> range = Range.open(fromValue, toValue);
        if(this.mode != INFINITE && !range.contains(value)){
            end();
        }
    }

    /**
     * 三角波函数
     * @param x 参量x
     * @param t 周期
     * */
    private float squareWave(double x,double t){
        return (float)( 1 - 2 * abs((1 / t) * (x - t / 2) - round((1 / t) * (x - t / 2))) );
    }


    public static <T extends Comparable<T>> DestinationAnimator<T> of(float ticks, T from,T to){
        Class<T> clazz = (Class<T>) from.getClass();
        return new DestinationAnimator.Builder<T>()
                //设置起始值
                .fromValue(from)
                //设置终点值
                .toValue(to)
                //设置值计算器，因为值都被泛化了，需要一个值计算器去提供泛化值的加减乘除
                .provider((IValueProvider<T>) IValueProvider.VALUE_MAP.get(clazz))
                //设置插值器
                .interpolator(TimeInterpolator.LINEAR)
                //设置动画初始值
                .init(from)
                //最大要多少ticks完成这个动画
                .maxTick(ticks)
                .build();
    }


    public static class Builder<T extends Comparable>{
        private T initialValue;
        private T toValue;
        private T fromValue;
        private TimeInterpolator interpolator;
        private IValueProvider<T> provider;

        private int mode = 0;

        private float maxTick;

        public Builder(){}

        public Builder<T> init(T initialValue) {
            this.initialValue = initialValue;
            return this;
        }

        public Builder<T> toValue(T toValue) {
            this.toValue = toValue;
            return this;
        }


        public Builder<T> fromValue(T fromValue) {
            this.fromValue = fromValue;
            return this;
        }

        public Builder<T> interpolator(TimeInterpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public Builder<T> maxTick(float maxTick){
            this.maxTick = maxTick;
            return this;
        }

        public Builder<T> provider(IValueProvider<T> provider) {
            this.provider = provider;
            return this;
        }

        public Builder<T> mode(int mode){
            this.mode = mode;
            return this;
        }

        public DestinationAnimator<T> build(){
            if(provider == null){
                throw new NullPointerException("provider is null,please have a check");
            }
            DestinationAnimator<T> anim = new DestinationAnimator<T>(
                    fromValue,
                    toValue,
                    provider,
                    initialValue,
                    maxTick,
                    mode
            );
            anim.interpolator = interpolator;
            return anim;
        }
    }
}
