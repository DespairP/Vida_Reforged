package teamHTBP.vidaReforged.helper;

public class VidaMathHelper {

    public static double getCyclicalityNumber(double x, int cyclicality){
        return 0.5 + 0.5 * Math.sin(Math.PI * x / cyclicality - 0.5 * Math.PI);
    }
}
