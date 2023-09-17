package teamHTBP.vidaReforged.core.common.system.magic;


import lombok.Data;
import lombok.experimental.Accessors;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;

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
    private transient Component description;
    /**所属元素*/
    private VidaElement element;
    /**玩家是否可用*/
    private boolean isPlayerUsable;
    /***/
    private ResourceLocation location;
    /**找到invokable的正则*/
    private String regex;


    public Component getCommandHoverComponents(){
        return ComponentUtils.wrapInSquareBrackets(Component.translatable(this.magicName)).withStyle((style) ->{
            return style.withColor(ChatFormatting.GREEN)
                    .withHoverEvent(
                            new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Component.empty()
                                            .append(this.magicName).append("\n")
                                            .append(this.element.name).append("\n")
                                            .append(this.isPlayerUsable ? "player usable" : "player not usable").append("\n")
                                            .append(description.getString())
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

    /**执行魔法*/
    public interface IInvokable {
        public void invokeMagic(ItemStack stack, VidaMagic invokeMagic, VidaMagicContainer container, IVidaManaCapability mana, Level level, Player player);
    }
}
