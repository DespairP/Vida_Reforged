package teamHTBP.vidaReforged.core.api;

import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.VidaColor;

public enum VidaElement implements IVidaElement{
    VOID("void", ARGBColor.BLACK),
    GOLD("gold", ARGBColor.BLACK);

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
}
