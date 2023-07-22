package teamHTBP.vidaReforged.server.recipe;

import net.minecraft.world.level.Level;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.server.blockEntities.MagicWordCraftingTableBlockEntity;
import teamHTBP.vidaReforged.server.recipe.records.VidaMagicWordRecipe;

import java.util.List;


public class VidaRecipeManager {


    /**获取合成表*/
    public static VidaMagicWordRecipe getMagicWordRecipe(Level level, MagicWordCraftingTableBlockEntity entity){
        List<VidaMagicWordRecipe> recipeList = level.getRecipeManager().getAllRecipesFor(VidaRecipeLoader.MAGIC_WORD_RECIPE.get());
        return recipeList.stream().filter(
                r -> r.matches(entity)
        ).findFirst().orElse(null);
    }

}
