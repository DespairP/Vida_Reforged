package teamHTBP.vidaReforged.core.api;

import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.ElementInteract;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.utils.codec.EnumCodec;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.VidaColor;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaElementHelper;

import java.util.Random;

public enum VidaElement implements IVidaElement{
    EMPTY("empty", ARGBColor.BLACK, TextureSection.empty(), null),
    VOID("void", ARGBColor.BLACK, TextureSection.empty(), null),
    GOLD("gold", ARGBColor.rgba(0xFFDA47FF), new TextureSection(new ResourceLocation(VidaReforged.MOD_ID, "textures/icons/goldlogo.png"), 0, 0, 32, 32, 32, 32), Direction.WEST),
    WOOD("wood",ARGBColor.rgba(0x6BC73AFF), new TextureSection(new ResourceLocation(VidaReforged.MOD_ID, "textures/icons/woodlogo.png"), 0, 0, 32, 32, 32, 32), Direction.EAST),
    AQUA("aqua",ARGBColor.rgba(0x00D5FFFF), new TextureSection(new ResourceLocation(VidaReforged.MOD_ID, "textures/icons/aqualogo.png"), 0, 0, 32, 32, 32, 32), Direction.NORTH),
    FIRE("fire",ARGBColor.DARK_RED, new TextureSection(new ResourceLocation(VidaReforged.MOD_ID, "textures/icons/firelogo.png"), 0, 0, 32, 32, 32, 32), Direction.SOUTH),
    EARTH("earth",ARGBColor.LIGHT_BROWN, new TextureSection(new ResourceLocation(VidaReforged.MOD_ID, "textures/icons/earthlogo.png"), 0, 0, 32, 32, 32, 32), null);

    public static final Codec<VidaElement> CODEC = new EnumCodec<>(VidaElement.class);

    /**元素名称*/
    public String name;
    /**元素颜色*/
    public VidaColor baseColor;
    /***/
    TextureSection icon;

    Direction direction;

    VidaElement(String name, VidaColor baseColor, TextureSection icon, Direction direction) {
        this.name = name;
        this.baseColor = baseColor;
        this.icon = icon;
        this.direction = direction;
    }

    @Override
    public String getElementName() {
        return this.name;
    }

    @Override
    public VidaColor getBaseColor() {
        return this.baseColor;
    }

    public TextureSection getIcon() {
        return icon;
    }

    public static VidaElement of(String value){
        try{
            if(value == null){
                return EMPTY;
            }
            return valueOf(value.toUpperCase());
        }catch (Exception ex){
            return EMPTY;
        }
    }

    /**切换到下一个元素*/
    public VidaElement next(){
        return switch (this) {
            case EMPTY -> GOLD;
            case VOID, EARTH -> EMPTY;
            case GOLD -> WOOD;
            case WOOD -> AQUA;
            case AQUA -> FIRE;
            case FIRE -> EARTH;
        };
    }

    public Direction getDirection() {
        return direction;
    }

    public static VidaElement randomValue() {
        return VidaElementHelper.getNormalElements().get(new Random().nextInt(VidaElementHelper.getNormalElements().size()));
    }

    public static ElementInteract getInteract(VidaElement elementL, VidaElement elementR){
        if(elementL == elementR) {
            return ElementInteract.SAME;
        }
        if(ElementInteract.GROWTH_MAP.get(elementL).equals(elementR) || ElementInteract.GROWTH_MAP.get(elementR).equals(elementL)){
            return ElementInteract.GROWTH;
        }
        if(ElementInteract.CONFLICT_MAP.get(elementL).equals(elementR) || ElementInteract.CONFLICT_MAP.get(elementR).equals(elementL)){
            return ElementInteract.CONFLICT;
        }
        return ElementInteract.UNDEFINED;
    }

}
