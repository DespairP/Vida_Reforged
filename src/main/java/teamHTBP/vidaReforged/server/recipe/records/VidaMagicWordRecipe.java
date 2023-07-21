package teamHTBP.vidaReforged.server.recipe.records;

import com.google.gson.annotations.Expose;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.server.recipe.VidaRecipeLoader;
import teamHTBP.vidaReforged.server.recipe.ingredient.ItemStackListIngredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static teamHTBP.vidaReforged.core.utils.math.StringUtils.compareString;
import static teamHTBP.vidaReforged.server.recipe.VidaRecipeSerializerLoader.MAGIC_WORD_RECIPE_SERIALIZER;

public class VidaMagicWordRecipe extends AbstractVidaRecipe<Container>{
    @Expose
    public Map<VidaElement, String> requiredWords = new HashMap<>(Map.of(
            VidaElement.GOLD, "",
            VidaElement.WOOD, "",
            VidaElement.AQUA, "",
            VidaElement.FIRE, "",
            VidaElement.EARTH, ""
    ));

    @Expose
    public ItemStackListIngredient ingredients = new ItemStackListIngredient();

    public boolean matchWords(Map<VidaElement, String> containerWordMap){
        boolean isMatch = true;
        for(VidaElement element : requiredWords.keySet()){
            String containerWord = containerWordMap.get(element);
            String recipeWord = requiredWords.get(element);
            if(!compareString(containerWord, recipeWord)){
                isMatch = false;
                break;
            }
        }
        return isMatch;
    }

    public boolean matchItemStack(List<ItemStack> containerStackList){
        return ingredients.test(containerStackList);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MAGIC_WORD_RECIPE_SERIALIZER.get();
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeType<?> getType() {
        return VidaRecipeLoader.MAGIC_WORD_RECIPE.get();
    }
}
