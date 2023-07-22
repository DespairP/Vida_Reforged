package teamHTBP.vidaReforged.helper;

import com.google.common.collect.ImmutableList;
import teamHTBP.vidaReforged.core.api.VidaElement;

import java.util.List;

public class VidaElementHelper {
    private static final List<VidaElement> NORMAL_ELEMENTS = ImmutableList.of(
            VidaElement.GOLD,
            VidaElement.WOOD,
            VidaElement.AQUA,
            VidaElement.FIRE,
            VidaElement.EARTH
    );


    public static List<VidaElement> getNormalElements(){
        return NORMAL_ELEMENTS;
    }
}
