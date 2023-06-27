package teamHTBP.vidaReforged.core.utils.animation.calculator;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public interface IValueProvider<T>{
    public T addValue(T currentValue,float value);

    public T multiplyValue(T currentValue,float value);

    public T minusValue(T currentValue,float value);

    public T divideValue(T currentValue,float value);

    public final static IValueProvider<Integer> INTEGER_FLOOR_VALUE_PROVIDER = new IValueProvider<Integer>() {
        @Override
        public Integer addValue(Integer currentValue, float value) {
            return (int)(currentValue + value);
        }

        @Override
        public Integer multiplyValue(Integer currentValue, float value) {
            return (int)(currentValue * value);
        }

        @Override
        public Integer minusValue(Integer currentValue, float value) {
            return currentValue - (int)Math.floor(value);
        }

        @Override
        public Integer divideValue(Integer currentValue, float value) {
            return (int)Math.floor(value) == 0 ? 0 : currentValue / (int)Math.floor(value);
        }
    };
    public final static IValueProvider<Float> FLOAT_VALUE_PROVIDER = new IValueProvider<Float>() {
        @Override
        public Float addValue(Float currentValue, float value) {
            return currentValue + value;
        }

        @Override
        public Float multiplyValue(Float currentValue, float value) {
            return currentValue * value;
        }

        @Override
        public Float minusValue(Float currentValue, float value) {
            return currentValue - value;
        }

        @Override
        public Float divideValue(Float currentValue, float value) {
            return currentValue / value;
        }
    };

    public static final Map<Class<?>,IValueProvider<?>> VALUE_MAP = ImmutableMap.of(
            Float.class, FLOAT_VALUE_PROVIDER,
            Integer.class, INTEGER_FLOOR_VALUE_PROVIDER
    );
}
