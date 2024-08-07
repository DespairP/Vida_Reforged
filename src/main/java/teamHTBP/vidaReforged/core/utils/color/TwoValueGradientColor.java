package teamHTBP.vidaReforged.core.utils.color;

/**二值渐变色*/
public class TwoValueGradientColor {
    private final ARGBColor fromColor;
    private final ARGBColor toColor;

    public TwoValueGradientColor(VidaColor fromColor, VidaColor toColor) {
        this.fromColor = fromColor.toARGB();
        this.toColor = toColor.toARGB();
    }

    public TwoValueGradientColor(ARGBColor fromColor, ARGBColor toColor) {
        this.fromColor = fromColor;
        this.toColor = toColor;
    }

    public TwoValueGradientColor(int fromColorHex, int toColorHex){
        this.fromColor = ARGBColor.argb(fromColorHex);
        this.toColor = ARGBColor.argb(toColorHex);
    }

    public ARGBColor getColor(float progress){
        int a = Math.round(fromColor.a() + (toColor.a() - fromColor.a()) * progress);
        int r = Math.round(fromColor.r() + (toColor.r() - fromColor.r()) * progress);
        int g = Math.round(fromColor.g() + (toColor.g() - fromColor.g()) * progress);
        int b = Math.round(fromColor.b() + (toColor.b() - fromColor.b()) * progress);

        return new ARGBColor(a, r, g, b);
    }

    public ARGBColor getFromColor() {
        return fromColor;
    }

    public ARGBColor getToColor() {
        return toColor;
    }
}
