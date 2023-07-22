package teamHTBP.vidaReforged.server.recipe.serializers;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;
import teamHTBP.vidaReforged.server.recipe.records.AbstractVidaRecipe;

public class BaseSerializer <C extends Container, RECIPE extends AbstractVidaRecipe<C>> implements RecipeSerializer<RECIPE>{

    Class<RECIPE> recipeClass;

    public BaseSerializer(Class<RECIPE> recipeClass) {
        this.recipeClass = recipeClass;
    }


    public RECIPE fromString(ResourceLocation resourceLocation,String jsonString){
        return JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).fromJson(jsonString, recipeClass).setID(resourceLocation).setSerializer(this);
    }

    @Override
    public RECIPE fromJson(ResourceLocation resourceLocation, JsonObject object) {
        return JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).fromJson(object, recipeClass).setID(resourceLocation).setSerializer(this);
    }

    @Override
    public @Nullable RECIPE fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
        return this.fromString(resourceLocation, buf.readUtf(32767));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, RECIPE recipe) {
        String recipeStr = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).toJson(recipe,recipeClass);
        buf.writeUtf(recipeStr, 32767);
    }
}
