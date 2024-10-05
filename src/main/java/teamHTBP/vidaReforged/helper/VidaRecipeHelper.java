package teamHTBP.vidaReforged.helper;

import com.google.common.collect.ImmutableMap;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import java.util.Map;
import java.util.function.Supplier;

public class VidaRecipeHelper {

    public static final Map<Item, Supplier<Item>> VIVID_LIQUID_RECIPE_BLOCK = ImmutableMap.of(
            Items.STONE, () -> VidaItemLoader.VIVID_STONE
    );

    public static final Map<TagKey<Item>, Supplier<Item>> VIVID_LIQUID_RECIPE_TAG = ImmutableMap.of(
            ItemTags.LOGS, () -> VidaItemLoader.VIVID_LOG
    );

    public static Supplier<Item> getResultItem(ItemStack stack){
        Item recipeItem = stack.getItem();
        if(VIVID_LIQUID_RECIPE_BLOCK.containsKey(recipeItem)){
            return VIVID_LIQUID_RECIPE_BLOCK.get(recipeItem);
        }
        for (TagKey<Item> tag : VIVID_LIQUID_RECIPE_TAG.keySet()) {
            if(stack.is(tag)){
                return VIVID_LIQUID_RECIPE_TAG.get(tag);
            }
        }
        return null;
    }
}
