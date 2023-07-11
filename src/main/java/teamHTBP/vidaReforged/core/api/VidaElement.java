package teamHTBP.vidaReforged.core.api;

import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.VidaColor;

public enum VidaElement implements IVidaElement{
    EMPTY("empty", ARGBColor.BLACK),
    VOID("void", ARGBColor.BLACK),
    GOLD("gold", ARGBColor.rgba(0xFFDA47FF)),
    WOOD("wood",ARGBColor.rgba(0x6BC73AFF)),
    AQUA("aqua",ARGBColor.rgba(0x00D5FFFF)),
    FIRE("fire",ARGBColor.DARK_RED),
    EARTH("earth",ARGBColor.LIGHT_BROWN);

    /**元素名称*/
    public String name;
    /**元素颜色*/
    public VidaColor baseColor;

    VidaElement(String name, VidaColor baseColor) {
        this.name = name;
        this.baseColor = baseColor;
    }

    @Override
    public String getElementName() {
        return this.name;
    }

    @Override
    public VidaColor getBaseColor() {
        return this.baseColor;
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
}
