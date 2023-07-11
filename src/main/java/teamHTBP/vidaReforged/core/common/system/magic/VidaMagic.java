package teamHTBP.vidaReforged.core.common.system.magic;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.core.api.VidaElement;

/**
 * 魔法模板
 * */
@Data
@Accessors(chain = true,fluent = true)
public class VidaMagic {
    /**魔法名字*/
    private String magicName;
    /**魔法唯一id*/
    private ResourceLocation magicLocation;
    /**魔法类别*/
    private VidaMagicType magicType;
    /**魔法图标*/
    private ResourceLocation icon;
    /***/
    private Integer iconIndex;
    /**魔法介绍*/
    private Component description;
    /**所属元素*/
    private VidaElement element;
    /**玩家是否可用*/
    private boolean isPlayerUsable;
    /***/
    private ResourceLocation location;


    public Component getCommandHoverComponents(){
        return ComponentUtils.wrapInSquareBrackets(Component.translatable(this.magicName)).withStyle((style) ->{
            return style.withColor(ChatFormatting.GREEN)
                    .withHoverEvent(
                            new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Component.empty()
                                            .append("name:" + this.magicName).append("\n")
                                            .append("element:" + this.element.name).append("\n")
                                            .append("isUsable:" + this.isPlayerUsable).append("\n")
                                            .append("description:" + description)
                            )
                    );
        });
    }

    public VidaMagic(String magicName) {
        this.magicName = magicName;
    }

    public enum VidaMagicType{
        ATTACK_SHOOTABLE,
        ATTACK_NORMAL,
        BUILD,
        HEAL,
        SUMMON,
        OTHER,
        NONE;

        public static VidaMagicType of(String value){
            try{
                if(value == null){
                    return NONE;
                }
                return VidaMagic.VidaMagicType.valueOf(value.toUpperCase());
            }catch (Exception ex){
                return NONE;
            }
        }
    }
}
