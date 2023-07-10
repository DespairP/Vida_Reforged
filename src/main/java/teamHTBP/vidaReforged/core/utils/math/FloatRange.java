package teamHTBP.vidaReforged.core.utils.math;

public class FloatRange {
    private float currentValue;
    private final float maxValue;
    private final float minValue;

    public FloatRange(float initialValue,float minValue,float maxValue){
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = set(initialValue);
    }

    public float get(){
        return currentValue;
    }

    public float set(float value){
        if(value <= maxValue && value >= minValue) currentValue = value;
        if(value < minValue) currentValue = minValue;
        if(value > maxValue) currentValue = maxValue;
        return currentValue;
    }

    public float increase(float step){
        return set(currentValue + step);
    }

    public float decrease(float step){
        return set(currentValue - step);
    }

    public float change(boolean isIncrease,float step){
        return isIncrease ? increase(step) : decrease(step);
    }
}
