package teamHTBP.vidaReforged.core.utils.color;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.RandomSource;
import teamHTBP.vidaReforged.core.api.VidaElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ColorTheme(String themeName, VidaColor baseColor, List<VidaColor> randomPickColor , List<TwoValueGradientColor> randomPickGradientColor) {
    public static final Map<String, ColorTheme> THEMES = new HashMap<>();

    public static final Map<VidaElement, ColorTheme> ELEMENT_COLOR_THEME = ImmutableMap.of(
            VidaElement.EMPTY, registerTheme("empty", ARGBColor.BLACK,List.of(), List.of()),
            VidaElement.GOLD, registerTheme("gold",  ARGBColor.rgba(0xFFDA47FF), List.of(), List.of()),
            VidaElement.WOOD, registerTheme("wood", ARGBColor.rgba(0x6BC73AFF), List.of(), List.of()),
            VidaElement.AQUA, registerTheme("aqua", ARGBColor.rgba(0x00D5FFFF), List.of(), List.of(new TwoValueGradientColor(0xFF00d2ff, 0xFF3a47d5), new TwoValueGradientColor(0xFF1CB5E0, 0xDD000851))),
            VidaElement.FIRE, registerTheme( "fire", ARGBColor.DARK_RED, List.of(), List.of()),
            VidaElement.EARTH, registerTheme("earth",ARGBColor.LIGHT_BROWN, List.of(), List.of()),
            VidaElement.VOID, registerTheme("void", ARGBColor.BLACK, List.of(), List.of())
    );

    public TwoValueGradientColor getRandomGradient(RandomSource randomSource){
        if(randomPickGradientColor.size() == 0){
            return new TwoValueGradientColor(baseColor, baseColor);
        }
        return randomPickGradientColor.get(randomSource.nextInt(randomPickGradientColor.size()));
    }

    public static ColorTheme getColorThemeByElement(VidaElement element){
        return ELEMENT_COLOR_THEME.get(element);
    }

    public static ColorTheme registerTheme(String themeName, VidaColor baseColor, List<VidaColor> randomPickColor , List<TwoValueGradientColor> randomPickGradientColor){
        ColorTheme theme = new ColorTheme(themeName, baseColor, randomPickColor, randomPickGradientColor);
        THEMES.put(themeName, theme);
        return theme;
    }
}
