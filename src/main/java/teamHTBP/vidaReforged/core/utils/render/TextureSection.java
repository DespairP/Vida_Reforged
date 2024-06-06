package teamHTBP.vidaReforged.core.utils.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.VidaReforged;


/**
 * 贴图管理器
 * @param location 贴图路径
 * @param minU 最小U
 * @param minV 最小V
 * @param width 长度
 * @param height 高度
 */
public record TextureSection(ResourceLocation location, int minU, int minV, int width, int height, int texWidth, int texHeight) {
    /**开始的U*/
    public int mu() {
        return minU;
    }

    /**开始的V*/
    public int mv() {
        return minV;
    }

    /**长度*/
    public int w(){
        return width;
    }

    /**宽度*/
    public int h(){
        return height;
    }

    /**获取中央位置*/
    public int cw(){
        return width / 2;
    }

    /**获取中央位置*/
    public int ch(){
        return height / 2;
    }

    public static Codec<TextureSection> codec = RecordCodecBuilder.create(ins -> ins.group(
            ResourceLocation.CODEC.fieldOf("location").forGetter(TextureSection::location),
            Codec.INT.fieldOf("minU").orElse(0).forGetter(TextureSection::minU),
            Codec.INT.fieldOf("minV").orElse(0).forGetter(TextureSection::minV),
            Codec.INT.fieldOf("width").orElse(16).forGetter(TextureSection::width),
            Codec.INT.fieldOf("height").orElse(16).forGetter(TextureSection::height),
            Codec.INT.fieldOf("texWidth").orElse(16).forGetter(TextureSection::texWidth),
            Codec.INT.fieldOf("texHeight").orElse(16).forGetter(TextureSection::texHeight)
    ).apply(ins, TextureSection::new));

    public static TextureSection empty(){
        return new TextureSection(new ResourceLocation(VidaReforged.MOD_ID, "textures/icons/magic_word/question_mark.png"), 0, 0, 0, 0, 0,0);
    }
}
