package teamHTBP.vidaReforged.core.common.system.magic;


import com.google.gson.annotations.Expose;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

/**
 * 魔法模板，
 * 包含魔法的基本信息，但是不包含魔法
 * 具体魔法释放逻辑见{@link VidaMagicInvokableManager}
 *
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true,fluent = true)
public class VidaMagic {
    /** 注册模板 */
    @Expose(serialize = false, deserialize = false)
    public final static String MAGIC_ID_IDENTIFY = "%s:%s";
    @Expose(serialize = false, deserialize = false)
    public final static ResourceLocation MAGIC_UNKNOWN = new ResourceLocation(VidaReforged.MOD_ID, "magic/null");
    /** 魔法id */
    private ResourceLocation magicId;
    /* 魔法内部Id,具体为 MOD_ID:magicBasicName */
    /** 找到invokable的正则，{@link VidaMagicInvokableManager} */
    @Deprecated
    private String id;
    /** 魔法属性 */
    VidaMagicAttribute attribute;
    /** 魔法类别，显示魔法的类别 */
    private String type;
    /** 魔法图标精灵图路径 */
    private TextureSection icon;
    /** 魔法介绍 */
    private Component description;
    /** 所属元素 */
    private VidaElement element;
    /** 玩家是否可用,主动可以释放 */
    private boolean isManuallyUsable = true;


    public static Codec<VidaMagic> codec = RecordCodecBuilder.create(ins -> ins.group(
            ResourceLocation.CODEC.fieldOf("magicId").orElse(MAGIC_UNKNOWN).forGetter(VidaMagic::magicId),
            Codec.STRING.fieldOf("id").orElse("undefined").forGetter(VidaMagic::id),
            VidaMagicAttribute.codec.fieldOf("attribute").orElse(VidaMagicAttribute.empty(VidaMagicAttributeType.MAGIC)).forGetter(VidaMagic::attribute),
            Codec.STRING.fieldOf("type").orElse("").forGetter(VidaMagic::type),
            TextureSection.codec.fieldOf("icon").forGetter(VidaMagic::icon),
            ExtraCodecs.COMPONENT.fieldOf("description").orElse(Component.empty()).forGetter(VidaMagic::description),
            VidaElement.CODEC.fieldOf("element").orElse(VidaElement.EMPTY).forGetter(VidaMagic::element),
            Codec.BOOL.fieldOf("isManuallyUsable").orElse(true).forGetter(VidaMagic::isManuallyUsable)
    ).apply(ins,VidaMagic::new));


    public Component getCommandHoverComponents(){
        return ComponentUtils.wrapInSquareBrackets(getFormattedDisplayName()).withStyle((style) ->{
            return style.withColor(ChatFormatting.GREEN)
                    .withHoverEvent(
                            new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Component.empty()
                                            .append(this.element.name).append("\n")
                                            .append(this.isManuallyUsable ? "manually usable" : "not usable").append("\n")
                                            .append(description)
                            )
                    );
        });
    }

    public Component getFormattedDisplayName() {
        return Component.translatable("magic.%s", magicId.toString());
    }

    public static VidaMagic empty(){
        return new VidaMagic().magicId(MAGIC_UNKNOWN);
    }

    /**执行魔法*/
    public interface IInvokable {
        public void invokeMagic(ItemStack stack, VidaMagic invokeMagic, VidaMagicAttribute container, IVidaManaCapability mana, Level level, Player player);
    }
}
