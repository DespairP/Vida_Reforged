package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaWandCraftingViewModel;
import teamHTBP.vidaReforged.core.api.hud.IVidaNodes;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.items.VidaWandEquipment;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandEquipmentSlot;

import java.util.List;
import static teamHTBP.vidaReforged.helper.GuiHelper.FONT;
public class VidaWandSlot extends VidaWidget implements IVidaNodes {
    /**显示信息的槽位*/
    private VidaWandEquipmentSlot slot;

    private MultiLineTextWidget info;

    StringWidget emptyTextWidget;

    private VidaWandCraftingViewModel viewModel;

    /**空槽位文字*/
    private final Component emptyComponent = Component.literal("-- ")
            .append(Component.translatable("gui.vida_reforged.vida_wand_slot.no_item"))
            .append(Component.literal(" --")).withStyle(Style.EMPTY.withBold(true));

    public FloatRange alpha = new FloatRange(0, 0, 1);

    private ItemStack lastItemStack = ItemStack.EMPTY;

    public ItemStack getSlotStack(){
        return slot == null ? ItemStack.EMPTY : slot.getItem();
    }

    public VidaWandSlot(int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal(""));
        this.init();
    }

    /***/
    public Component empty(){
        return this.emptyComponent;
    }

    public void renderInfo(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if(getSlotStack().isEmpty()){
            this.renderEmptyInfo(graphics, mouseX, mouseY, partialTicks);
        }
        this.renderTextedInfo(graphics, mouseX, mouseY, partialTicks);
    }

    /**渲染空白槽位*/
    public void renderEmptyInfo(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if(emptyTextWidget == null){
            emptyTextWidget = new StringWidget(this.emptyComponent, FONT);
        }
        FrameLayout.alignInRectangle(emptyTextWidget, getX(), getY(), getWidth(), getHeight(), 0.5F, 0.5F);
        emptyTextWidget.renderWidget(graphics, mouseX, mouseY, partialTicks);
    }

    /**渲染文字*/
    public void renderTextedInfo(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        ItemStack stack = getSlotStack();
        if(!stack.is(item -> item instanceof VidaWandEquipment)){
            return;
        }


    }

    public void init(){

    }

    /**渲染背景 */
    protected void renderBackground(GuiGraphics graphics){
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        graphics.fillGradient( getX(), getY(), getX() + this.width, getY() + this.height, -1072689136, -804253680);
        poseStack.popPose();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if(this.lastItemStack != getSlotStack()){
            this.alpha.set(0);
        }
        this.alpha.increase(0.02f);

        this.renderBackground(graphics);
        this.renderInfo(graphics, mouseX, mouseY, partialTicks);

        this.lastItemStack = getSlotStack();
    }
}
