package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.recipe.VidaRecipeManager;

import java.util.ArrayList;
import java.util.List;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaGuidebookCraftingRecipe extends VidaWidget implements IVidaGuidebookComponent{
    public static final ResourceLocation GUIDEBOOK_RESOURCE = new ResourceLocation(MOD_ID, "textures/gui/book.png");
    public static final TextureSection RECIPE_SECTION = new TextureSection(GUIDEBOOK_RESOURCE, 224, 240, 96, 96, 512, 512);
    private ItemStack resultItem = ItemStack.EMPTY;
    private List<CraftingRecipe> recipes = new ArrayList<>();
    private int step = 0;
    FloatRange alphaRange = new FloatRange(0,0,1f);
    private static final int ICON_PIXEL = 30;

    public VidaGuidebookCraftingRecipe(int x, int y, int width, int height, ItemStack resultItem, Component component) {
        super(x, y, width, height, component);
        this.resultItem = resultItem;
        this.recipes = VidaRecipeManager.getRecipeByResult(Minecraft.getInstance().level, resultItem);
    }

    protected void renderRecipe(GuiGraphics graphics, CraftingRecipe recipe, int mouseX, int mouseY){
        if(recipe instanceof ShapedRecipe shapedRecipe){
            int recipeHeight = shapedRecipe.getRecipeHeight();
            int recipeWidth = shapedRecipe.getRecipeWidth();

            NonNullList<Ingredient> ingredients = shapedRecipe.getIngredients();
            for(int i = 0; i < ingredients.size(); i++){
                int x = (i % recipeWidth) * ICON_PIXEL + 10;
                int y = (i / recipeWidth) * ICON_PIXEL + 10;
                Ingredient ingredient = ingredients.get(i);
                int length = ingredient.getItems().length;
                if(length == 0){
                    continue;
                }
                ItemStack item = ingredient.getItems()[step % length];
                graphics.renderFakeItem(item, x, y);
            }
        }
        // 合成后物品
        if(this.resultItem != null && !this.resultItem.isEmpty()){
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            graphics.renderItem(this.resultItem, RECIPE_SECTION.w() + 15,  RECIPE_SECTION.h() / 2 - 8);
            graphics.drawCenteredString(mc.font, this.resultItem.getDisplayName(), RECIPE_SECTION.w() + 23, RECIPE_SECTION.h() / 2 + 20, ARGBColor.of(255,0,0,0).argb());
            poseStack.popPose();
        }
    }


    protected void renderBackground(GuiGraphics graphics){
        VidaGuiHelper.blitWithTexture(graphics, 0, 0, 0, RECIPE_SECTION);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        alphaRange.increase(mc.getDeltaFrameTime() * 0.2f);
        this.step = ClientTickHandler.ticks % 30 == 0 ? (step + 1) % 100 : step;

        PoseStack poseStack =  graphics.pose();

        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        poseStack.pushPose();
        poseStack.translate(getX(), getY(), 0);
        if(recipes.size() > 0) {
            renderRecipe(graphics, recipes.get(0), mouseX, mouseY);
        }
        renderBackground(graphics);
        poseStack.popPose();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);

    }

    @Override
    public void init() {

    }
}
