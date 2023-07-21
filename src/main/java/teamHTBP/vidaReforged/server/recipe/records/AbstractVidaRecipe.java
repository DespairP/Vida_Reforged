package teamHTBP.vidaReforged.server.recipe.records;

import com.google.gson.annotations.Expose;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public abstract class AbstractVidaRecipe<T extends Container> implements Recipe<T> {
    /**合成表id*/
    @Expose(deserialize = false, serialize = false)
    ResourceLocation recipeId;
    /**合成表类型*/
    @Expose
    public String type;
    @Expose
    public ItemStack resultItem = ItemStack.EMPTY;
    @Expose(deserialize = false, serialize = false)
    private RecipeSerializer<?> serializer;

    public <C extends Container, R extends AbstractVidaRecipe<C>> R setID(ResourceLocation id) {
        this.recipeId = id;
        return (R) this;
    }

    @Override
    public abstract boolean matches(T container, Level level);

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public ItemStack assemble(T container, RegistryAccess access) {
        return this.resultItem.copy();
    }

    public abstract boolean canCraftInDimensions(int width, int height);

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return this.resultItem.copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    public abstract RecipeType<?> getType();

    /**比较合成*/
    @Override
    public boolean equals(Object obj) {
        return obj instanceof AbstractVidaRecipe && this.recipeId.equals(((AbstractVidaRecipe) obj).recipeId);
    }

    public <C extends Container, R extends AbstractVidaRecipe<C>> R setSerializer(RecipeSerializer<?> serializer) {
        this.serializer = serializer;
        return (R) this;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }
}
