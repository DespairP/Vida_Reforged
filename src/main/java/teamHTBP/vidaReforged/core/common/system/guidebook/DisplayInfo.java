package teamHTBP.vidaReforged.core.common.system.guidebook;

import com.mojang.serialization.Codec;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

/** 图标信息 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DisplayInfo {
    /**物品图标*/
    public ItemStack itemIcon;
    /**自定义位置图标*/
    public ResourceLocation texIcon;
    /**自定义图标材质大小*/
    public int texIconSize = 64;
    /**处于的位置*/
    public int x;
    public int y;

    public static final Codec<DisplayInfo> CODES = RecordCodecBuilder.create( ins -> ins.group(
        ItemStack.CODEC.fieldOf("itemIcon").orElse(new ItemStack(() -> Items.AIR)).forGetter(DisplayInfo::getItemIcon),
        ResourceLocation.CODEC.optionalFieldOf("texIcon", new ResourceLocation(MOD_ID, "")).forGetter(DisplayInfo::getTexIcon),
        Codec.INT.fieldOf("texIconSize").orElse(64).forGetter(DisplayInfo::getTexIconSize),
        Codec.INT.fieldOf("x").orElse(0).forGetter(DisplayInfo::getX),
        Codec.INT.fieldOf("y").orElse(0).forGetter(DisplayInfo::getY)
    ).apply(ins, DisplayInfo::new));
}
