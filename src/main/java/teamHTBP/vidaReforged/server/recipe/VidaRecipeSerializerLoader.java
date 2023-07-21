package teamHTBP.vidaReforged.server.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.server.recipe.records.VidaMagicWordRecipe;
import teamHTBP.vidaReforged.server.recipe.serializers.BaseSerializer;

public class VidaRecipeSerializerLoader {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, VidaReforged.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> MAGIC_WORD_RECIPE_SERIALIZER =
            SERIALIZER.register("magic_word", () -> new BaseSerializer<>(VidaMagicWordRecipe.class));

}
