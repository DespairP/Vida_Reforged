package teamHTBP.vidaReforged.server.recipe.records;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import teamHTBP.vidaReforged.server.recipe.VidaRecipeLoader;
import teamHTBP.vidaReforged.server.recipe.VidaRecipeSerializerLoader;
import teamHTBP.vidaReforged.server.recipe.ingredient.VidaRecipeCodecs;

public class ElementHarmonizeRecipe extends AbstractVidaRecipe<Container> {
    public static final Codec<ElementHarmonizeRecipe> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    VidaRecipeCodecs.INGREDIENT_CODEC.fieldOf("goldItem").forGetter(ElementHarmonizeRecipe::getGoldItem),
                    VidaRecipeCodecs.INGREDIENT_CODEC.fieldOf("woodItem").forGetter(ElementHarmonizeRecipe::getWoodItem),
                    VidaRecipeCodecs.INGREDIENT_CODEC.fieldOf("aquaItem").forGetter(ElementHarmonizeRecipe::getAquaItem),
                    VidaRecipeCodecs.INGREDIENT_CODEC.fieldOf("fireItem").forGetter(ElementHarmonizeRecipe::getFireItem),
                    VidaRecipeCodecs.INGREDIENT_CODEC.fieldOf("earthItem").forGetter(ElementHarmonizeRecipe::getEarthItem),
                    ItemStack.CODEC.fieldOf("resultItem").forGetter(ElementHarmonizeRecipe::getResultItem),
                    Codec.INT.fieldOf("useTicks").forGetter(ElementHarmonizeRecipe::getUseTicks))
                    .apply(inst, ElementHarmonizeRecipe::new)
    );
    private Ingredient goldItem = Ingredient.EMPTY;
    private Ingredient woodItem = Ingredient.EMPTY;
    private Ingredient aquaItem = Ingredient.EMPTY;
    private Ingredient fireItem = Ingredient.EMPTY;
    private Ingredient earthItem = Ingredient.EMPTY;
    private ItemStack resultItem = ItemStack.EMPTY;
    private int useTicks = 0;


    public ElementHarmonizeRecipe(Ingredient goldItemStack, Ingredient woodItemStack, Ingredient aquaItemStack, Ingredient fireItemStack, Ingredient earthItemStack, ItemStack resultItem, int useTick) {
        this.goldItem = goldItemStack;
        this.woodItem = woodItemStack;
        this.aquaItem = aquaItemStack;
        this.fireItem = fireItemStack;
        this.earthItem = earthItemStack;
        this.resultItem = resultItem;
        this.useTicks = useTick;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return earthItem.test(container.getItem(0));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(goldItem, woodItem, aquaItem, fireItem, earthItem);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeType<?> getType() {
        return VidaRecipeLoader.ELEMENT_HARMONIZE_RECIPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return VidaRecipeSerializerLoader.ELEMENT_HARMONIZE_RECIPE_SERIALIZER.get();
    }

    public Ingredient getGoldItem() {
        return goldItem;
    }

    public Ingredient getWoodItem() {
        return woodItem;
    }

    public Ingredient getAquaItem() {
        return aquaItem;
    }

    public Ingredient getFireItem() {
        return fireItem;
    }

    public Ingredient getEarthItem() {
        return earthItem;
    }

    public int getUseTicks() {
        return useTicks;
    }

    public ItemStack getResultItem() {
        return resultItem;
    }


}
