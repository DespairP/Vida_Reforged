package teamHTBP.vidaReforged.core.utils.animation;

import com.google.common.collect.Range;
import teamHTBP.vidaReforged.core.utils.animation.calculator.IValueProvider;

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

    public DestinationAnimator(T fromValue, T toValue, IValueProvider<T> provider, T initialValue, float maxTick) {
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.provider = provider;
        this.value = initialValue;
        this.maxTick = maxTick;
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

        //计算动画开始经过了几帧数
        this.isRunning = true;
        float step = (float) Math.min(maxTick, existingTick + partialTicks);
        this.existingTick += partialTicks;

        //延迟帧检查
        if(this.delayFrames > this.existingTick){
            return;
        }

        // value = (to - from) * percent
        this.value = provider.multiplyValue(provider.minusValue(this.toValue,(float) this.fromValue), this.interpolator.interpolation((step / maxTick)));
        // 检查是否越界了，如果已经越界，动画结束
        Range<T> range = Range.open(fromValue, toValue);
        if(!range.contains(value)){
            end();
        }
    }


    public static <T extends Comparable> DestinationAnimator<T> of(float ticks, T from,T to){
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

        public DestinationAnimator<T> build(){
            if(provider == null){
                throw new NullPointerException("provider is null,please have a check");
            }
            DestinationAnimator<T> anim = new DestinationAnimator<T>(
                    fromValue,
                    toValue,
                    provider,
                    initialValue,
                    maxTick
            );
            anim.interpolator = interpolator;
            return anim;
        }
    }
}
