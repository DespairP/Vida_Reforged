package teamHTBP.vidaReforged.core.common.system.magic;


import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.core.api.VidaElement;

/**
 * 魔法
 *
 * */
@Getter
@Setter
public class VidaMagic {
    /**魔法名字*/
    private String magicName;
    /**魔法唯一id*/
    private ResourceLocation magicLocation;
    /**魔法类别*/
    private VidaMagicType magicType;
    /**魔法图标*/
    private ResourceLocation icon;
    /**魔法介绍*/
    private Component description;
    /**所属元素*/
    private VidaElement element;
    /**玩家是否可用*/
    private boolean isPlayerUsable;

    public VidaMagic(String magicName) {
        this.magicName = magicName;
    }

    public enum VidaMagicType{
        ATTACK_SHOOTABLE,
        ATTACK_NORMAL,
        BUILD,
        HEAL,
        SUMMON,
        OTHER;
    }
}
