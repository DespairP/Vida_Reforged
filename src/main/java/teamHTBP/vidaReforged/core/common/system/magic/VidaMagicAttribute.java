package teamHTBP.vidaReforged.core.common.system.magic;

import com.google.gson.annotations.Expose;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 法器饰品属性
 * 每个魔法强度上都有不同,MagicContainer是为了方便存储每个物品而准备的
 * */
@AllArgsConstructor
@Data
@Accessors(chain = true,fluent = true)
public class VidaMagicAttribute {
    /**基础伤害*/
    private double baseDamage;
    /**基础消耗能量*/
    private double baseCostMana;
    /**发射数量*/
    private int baseInvokeProjectileAmount;
    /**基础发射*/
    private double baseInvokeProjectileSpeed;
    /**伤害倍数*/
    private double damageFactor;
    /**消耗能量，倍数*/
    private double costManaFactor;
    /**发射（如有）数量倍数*/
    private int invokeProjectileAmountFactor;
    /**发射（如有）速度，倍数*/
    private double invokeProjectileSpeedFactor;
    /**法器饰品级别*/
    private int level = 0;
    /**类型*/
    private VidaMagicAttributeType type;

    @Expose(serialize = false, deserialize = false)
    public final static Codec<VidaMagicAttribute> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.DOUBLE.fieldOf("baseDamage").orElseGet(() -> 1.00).forGetter(VidaMagicAttribute::baseDamage),
            Codec.DOUBLE.fieldOf("baseCostMana").orElseGet(() -> 1.00).forGetter(VidaMagicAttribute::baseCostMana),
            Codec.INT.fieldOf("baseInvokeProjectileAmount").orElseGet(() -> 1).forGetter(VidaMagicAttribute::baseInvokeProjectileAmount),
            Codec.DOUBLE.fieldOf("baseInvokeProjectileSpeed").orElseGet(() -> 1.00).forGetter(VidaMagicAttribute::baseInvokeProjectileSpeed),
            Codec.DOUBLE.fieldOf("damageFactor").orElseGet(() -> 1.00).forGetter(VidaMagicAttribute::damageFactor),
            Codec.DOUBLE.fieldOf("costManaFactor").orElseGet(() -> 1.00).forGetter(VidaMagicAttribute::costManaFactor),
            Codec.INT.fieldOf("invokeProjectileAmountFactor").orElseGet(() -> 1).forGetter(VidaMagicAttribute::invokeProjectileAmountFactor),
            Codec.DOUBLE.fieldOf("invokeProjectileSpeedFactor").orElseGet(() -> 1.00).forGetter(VidaMagicAttribute::invokeProjectileSpeedFactor),
            Codec.INT.fieldOf("level").orElseGet(() -> 0).forGetter(VidaMagicAttribute::level),
            VidaMagicAttributeType.CODEC.fieldOf("type").orElseGet(() -> VidaMagicAttributeType.UNDEFINED).forGetter(VidaMagicAttribute::type)
    ).apply(ins, VidaMagicAttribute::new));


    protected VidaMagicAttribute(){}

    public enum MagicContainerArgument{
        DAMAGE_FACTOR,
        COST_MANA_FACTOR,
        INVOKE_PROJECTILE_AMOUNT_FACTOR,
        INVOKE_PROJECTILE_SPEED_FACTOR,
        LEVEL
    }

    public static VidaMagicAttribute empty(VidaMagicAttributeType type){
        return new VidaMagicAttribute(
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                0,
                type
        );
    }
}
