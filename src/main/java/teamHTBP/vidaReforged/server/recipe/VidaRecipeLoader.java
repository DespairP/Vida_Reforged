package teamHTBP.vidaReforged.server.recipe;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.server.recipe.records.AbstractVidaRecipe;
import teamHTBP.vidaReforged.server.recipe.records.VidaMagicWordRecipe;

public class VidaRecipeLoader {
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, VidaReforged.MOD_ID);

    public static final RegistryObject<RecipeType<VidaMagicWordRecipe>> MAGIC_WORD_RECIPE = register("magic_word");

    private static <C extends Container, TYPE extends AbstractVidaRecipe<C>> RegistryObject<RecipeType<TYPE>> register(String name) {
        return TYPES.register(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return new ResourceLocation(VidaReforged.MOD_ID, name).toString();
            }
        });
    }

}
