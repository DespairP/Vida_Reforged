package teamHTBP.vidaReforged.server.recipe.serializers;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.server.recipe.records.AbstractVidaRecipe;

public class BaseCodecSerializer<C extends Container, RECIPE extends AbstractVidaRecipe<C>> implements RecipeSerializer<RECIPE> {
    @NotNull final Codec<RECIPE> codec;

    public BaseCodecSerializer(@NotNull Codec<RECIPE> codec) {
        this.codec = codec;
    }

    @Override
    public RECIPE fromJson(ResourceLocation location, JsonObject json) {
        return codec.parse(JsonOps.INSTANCE, json).get().orThrow().setID(location);
    }

    @Override
    public @Nullable RECIPE fromNetwork(ResourceLocation location, FriendlyByteBuf byteBuf) {
        return byteBuf.readJsonWithCodec(codec).setID(location);
    }

    @Override
    public void toNetwork(FriendlyByteBuf byteBuf, RECIPE recipe) {
        byteBuf.writeJsonWithCodec(codec, recipe);
    }
}
