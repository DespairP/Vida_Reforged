package teamHTBP.vidaReforged.core.utils.color;

/**二值渐变色*/
public class TwoValueTransitionColor {
    private final ARGBColor fromColor;
    private final ARGBColor toColor;


    public TwoValueTransitionColor(ARGBColor fromColor, ARGBColor toColor) {
        this.fromColor = fromColor;
        this.toColor = toColor;
    }

    public ARGBColor getColor(float progress){
        return null;
    }
}
