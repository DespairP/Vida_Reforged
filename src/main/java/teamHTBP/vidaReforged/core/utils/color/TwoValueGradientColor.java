package teamHTBP.vidaReforged.core.utils.color;

/**二值渐变色*/
public class TwoValueGradientColor {
    private final ARGBColor fromColor;
    private final ARGBColor toColor;


    public TwoValueGradientColor(ARGBColor fromColor, ARGBColor toColor) {
        this.fromColor = fromColor;
        this.toColor = toColor;
    }

    public ARGBColor getColor(float progress){
        return null;
    }

    public ARGBColor getFromColor() {
        return fromColor;
    }

    public ARGBColor getToColor() {
        return toColor;
    }
}
