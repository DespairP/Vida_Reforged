package teamHTBP.vidaReforged.core.common.level;

public class VidaLeavesColor {
    private static int[] pixels = new int[65536];

    public static void init(int[] p_46111_) {
        pixels = p_46111_;
    }

    public static int get(double rainFall, double temp) {
        rainFall = rainFall * temp;
        int i = (int)((1.0D - temp) * 255.0D);
        int j = (int)((1.0D - rainFall) * 255.0D);
        int k = j << 8 | i;
        return k > pixels.length ? -65281 : pixels[k];
    }

    public static int getEvergreenColor() {
        return 6396257;
    }

    public static int getBirchColor() {
        return 8431445;
    }

    public static int getDefaultColor() {
        return 4764952;
    }

    public static int getMangroveColor() {
        return 9619016;
    }
}
