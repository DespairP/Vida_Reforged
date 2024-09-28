package teamHTBP.vidaReforged.server.recipe.ingredient;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.item.crafting.Ingredient;

public class VidaRecipeCodecs {
    public static final Codec<Ingredient> INGREDIENT_CODEC = Codec.PASSTHROUGH.comapFlatMap(dynamic ->
            {
                try
                {
                    Ingredient ingredient = Ingredient.fromJson(dynamic.convert(JsonOps.INSTANCE).getValue());
                    return DataResult.success(ingredient);
                }
                catch(Exception e)
                {
                    return DataResult.error(() -> e.getMessage());
                }
            },
            ingredient -> new Dynamic<JsonElement>(JsonOps.INSTANCE, ingredient.toJson()));
}
