package teamHTBP.vidaReforged.helper;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class VidaRecipeHelper {

    public static final Map<Item, Supplier<Item>> VIVID_LIQUID_RECIPE = ImmutableMap.of(
            Items.STONE, VidaItemLoader.AQUA_ELEMENT_CORE
    );

}
