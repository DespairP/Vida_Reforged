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
 * 魔法模板，
 * 包含魔法的基本信息，但是不包含魔法
 * 具体魔法释放逻辑见{@link VidaMagicManager}
 *
 * */
@Data
@Accessors(chain = true,fluent = true)
public class VidaMagic {
    /** 注册模板 */
    public final static String MAGIC_ID_IDENTIFY = "%s:%s";

    /** 魔法Id,具体为 MOD_ID:magicBasicName */
    private String magicId;
    /** 魔法唯一id */
    private ResourceLocation magicIdLocation;
    /** 实际注册路径 */
    private ResourceLocation path;
    /** 魔法名字 */
    private String magicBasicName;
    /** 魔法类别，显示魔法的类别 */
    private String magicType;
    /** 魔法图标精灵图路径 */
    private ResourceLocation spriteLocation;
    /** 整个精灵图大小 */
    private int spriteSize = 32;
    /** 魔法图标位于精灵图的第几个 */
    private int iconIndex;
    /** 图标大小 */
    private int iconSize = 32;
    /** 魔法介绍Key */
    private transient Component description;
    /** 所属元素 */
    private VidaElement element;
    /** 玩家是否可用 */
    private boolean isPlayerUsable = true;
    /** 找到invokable的正则，{@link VidaMagicManager} */
    private String regex;


    public Component getCommandHoverComponents(){
        return ComponentUtils.wrapInSquareBrackets(Component.translatable(this.magicBasicName)).withStyle((style) ->{
            return style.withColor(ChatFormatting.GREEN)
                    .withHoverEvent(
                            new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Component.empty()
                                            .append(this.magicBasicName).append("\n")
                                            .append(this.element.name).append("\n")
                                            .append(this.isPlayerUsable ? "player usable" : "player not usable").append("\n")
                                            .append(description.getString())
                            )
                    );
        });
    }


    public VidaMagic(String modId,String magicBasicName) {
        this.magicId = String.format(MAGIC_ID_IDENTIFY, modId, magicBasicName);
        this.magicBasicName = magicBasicName;
    }

    /**获取图标所在精灵图的U*/
    public int getIconU(){
        return (iconSize * iconIndex % spriteSize) * iconSize;
    }

    /**获取图标所在精灵图的V*/
    public int getIconV(){
        return (iconSize * iconIndex / spriteSize) * iconSize;
    }

    /**执行魔法*/
    public interface IInvokable {
        public void invokeMagic(ItemStack stack, VidaMagic invokeMagic, VidaMagicContainer container, IVidaManaCapability mana, Level level, Player player);
    }
}
