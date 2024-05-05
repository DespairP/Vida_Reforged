package teamHTBP.vidaReforged.server.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.server.blockEntities.MagicWordCraftingTableBlockEntity;
import teamHTBP.vidaReforged.server.recipe.records.VidaMagicWordRecipe;

import java.util.List;
import java.util.stream.Collectors;


public class VidaRecipeManager {

    /**获取合成表*/
    public static VidaMagicWordRecipe getMagicWordRecipe(Level level, MagicWordCraftingTableBlockEntity entity){
        List<VidaMagicWordRecipe> recipeList = level.getRecipeManager().getAllRecipesFor(VidaRecipeLoader.MAGIC_WORD_RECIPE.get());
        return recipeList.stream().filter(
                r -> r.matches(entity)
        ).findFirst().orElse(null);
    }

    /**获取普通合成表*/
    public static List<CraftingRecipe> getRecipeByResult(Level level, ItemStack result){
        List<CraftingRecipe> recipeList = level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
        return recipeList.stream().filter(
                r -> r.getResultItem(null).is(result.getItem()) && ( r instanceof ShapedRecipe || r instanceof ShapelessRecipe)
        ).collect(Collectors.toList());
    }

}
