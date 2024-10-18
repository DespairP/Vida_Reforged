package teamHTBP.vidaReforged.core;

import com.google.common.collect.ImmutableMap;
import teamHTBP.vidaReforged.core.api.VidaElement;

import java.util.LinkedHashMap;
import java.util.Map;

import static teamHTBP.vidaReforged.core.api.VidaElement.*;

public enum ElementInteract {

    GROWTH, CONFLICT, SAME, UNDEFINED, OTHER;

    public static final Map<VidaElement, VidaElement> GROWTH_MAP = ImmutableMap.of(
            WOOD,FIRE,
            FIRE,EARTH,
            EARTH,GOLD,
            GOLD,AQUA,
            AQUA,WOOD
    );

    public static final Map<VidaElement, VidaElement> CONFLICT_MAP = ImmutableMap.of(
            WOOD, EARTH,
            EARTH, AQUA,
            AQUA, FIRE,
            FIRE, GOLD,
            GOLD, WOOD
    );
}
