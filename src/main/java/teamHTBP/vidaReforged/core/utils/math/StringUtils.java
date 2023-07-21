package teamHTBP.vidaReforged.core.utils.math;

public class StringUtils {
    public static boolean compareString(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }
}
