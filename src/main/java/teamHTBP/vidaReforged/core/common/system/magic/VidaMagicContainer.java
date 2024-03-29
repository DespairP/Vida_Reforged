package teamHTBP.vidaReforged.core.common.system.magic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 法器组合
 * 每个魔法强度上都有不同,MagicContainer是为了方便存储每个物品而准备的
 * */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true,fluent = true)
public class VidaMagicContainer{
    /**Container存储的魔法*/
    private List<String> magic = new LinkedList<>();
    /**伤害*/
    private double damage;
    /**伤害倍数，增益*/
    private double multiplier;
    /**伤害减数，减益*/
    private double decreaser;
    /**需要消耗的能量*/
    private double costMana;
    /**一次启动的数量*/
    private int amount;
    /**已经启动了多少次*/
    private int invokeCount;
    /**最大启动次数*/
    private int maxInvokeCount;
    /**无限次*/
    private static final int INFINITE = -1;
    /**需要的冷却时间*/
    private long coolDown = 0;
    /**上一次施法时间*/
    private long lastInvokeMillSec = 0;
    /**级别*/
    private int level = 0;
    /**施法速度*/
    private double speed = 0;
    /**最大*/
    private int maxAge = 20;


    public enum MagicContainerArgument{
        MAGIC,
        DAMAGE,
        MULTIPLIER,
        DECREASER,
        COST_MANA,
        AMOUNT,
        INVOKE_COUNT,
        MAX_INVOKE_COUNT,
        COOLDOWN,
        LAST_INVOKE_MILLSEC,
        LEVEL,
        MAX_AGE,
        SPEED;
    }

    public static VidaMagicContainer empty(){
        return new VidaMagicContainer();
    }
}
