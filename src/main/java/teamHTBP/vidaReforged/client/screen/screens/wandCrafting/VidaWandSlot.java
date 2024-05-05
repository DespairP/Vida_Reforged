package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.client.screen.components.common.ItemStackWidget;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaWandCraftingViewModel;
import teamHTBP.vidaReforged.core.api.hud.IVidaNodes;
import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.server.items.VidaWandEquipment;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandEquipmentSlot;

import java.util.ArrayList;
import java.util.List;
import static teamHTBP.vidaReforged.helper.VidaGuiHelper.FONT;
public class VidaWandSlot extends VidaWidget implements IVidaNodes {
    private final static int INIT_HEIGHT = 50;
    /***/
    StringWidget emptyTextWidget;
    /**空槽位文字*/
    private final Component emptyComponent = Component.literal("-- ")
            .append(Component.translatable("gui.vida_reforged.vida_wand_slot.no_item"))
            .append(Component.literal(" --")).withStyle(Style.EMPTY.withBold(true));
    /**透明度*/
    public FloatRange alpha = new FloatRange(0, 0, 1);
    /***/
    private ItemStack lastItemStack = ItemStack.EMPTY;
    private ItemStack currentItemStack = ItemStack.EMPTY;
    /***/
    private List<AbstractWidget> equipmentInfoWidgets = new ArrayList<>();
    /**/
    protected FrameLayout frameLayout;

    public ItemStack getSlotStack(){
        return currentItemStack == null ? ItemStack.EMPTY : currentItemStack;
    }

    public VidaWandSlot(int x, int y, int width, int height, ItemStack stack) {
        super(x, y, width, height == 0 ? INIT_HEIGHT : height, Component.literal(""));
    }

    public void setSlotStack(ItemStack stack){
        this.lastItemStack = currentItemStack;
        this.currentItemStack = stack;
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
        emptyTextWidget.setColor(ARGBColor.of(alpha.get(), 1.0f, 1.0f, 1.0f).fontColor());
        FrameLayout.alignInRectangle(emptyTextWidget, getX(), getY(), getWidth(), getHeight(), 0.5F, 0.5F);
        emptyTextWidget.renderWidget(graphics, mouseX, mouseY, partialTicks);
    }

    /**渲染文字*/
    public void renderTextedInfo(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        ItemStack stack = getSlotStack();
        if(!stack.is(item -> item.get() instanceof VidaWandEquipment)){
            return;
        }
        renderEquipmentInfo(graphics, mouseX, mouseY, partialTicks);
    }

    public void renderEquipmentInfo(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        this.frameLayout.setX(getX());
        this.frameLayout.setY(getY());
        this.frameLayout.arrangeElements();
        equipmentInfoWidgets.forEach(widget -> {widget.render(graphics, mouseX, mouseY, partialTicks);});
    }

    public void rearrangeComponent(){
        equipmentInfoWidgets.clear();
        ItemStack slotItemStack = this.getSlotStack();
        //物品图片
        ItemStackWidget itemWidget = new ItemStackWidget(slotItemStack);
        //物品名字
        Component itemDisplayName = slotItemStack.getDisplayName();
        int displayWidth = mc.font.width(itemDisplayName);
        if(displayWidth > getWidth() - 60){
            FormattedText splitDisplayName = mc.font.substrByWidth(itemDisplayName, getWidth() - 60);
            itemDisplayName = Component.literal(splitDisplayName.getString()).append("...");
        }
        StringWidget itemDisplayNameWidget = new StringWidget(itemDisplayName, mc.font);
        //物品介绍
        MultiLineTextWidget itemInfoWidget = new MultiLineTextWidget(Component.literal("\n123\n123143\ntest\b123\n123314415551\n131451551555512"), mc.font);
        //
        int widgetsHeight = itemWidget.getHeight() + itemInfoWidget.getHeight() + 10;
        this.setHeight(Math.max(widgetsHeight, getHeight()));

        //标题的layout
        GridLayout titleLayout = new GridLayout();
        titleLayout.spacing(12);
        titleLayout.addChild(itemWidget, 0, 0);
        titleLayout.addChild(itemDisplayNameWidget, 0, 1, titleLayout.defaultCellSetting().alignHorizontallyLeft().alignVerticallyMiddle());
        titleLayout.arrangeElements();


        //title+介绍
        GridLayout mainLayout = new GridLayout();
        mainLayout.addChild(titleLayout, 0, 0);
        mainLayout.addChild(itemInfoWidget, 1, 0);
        mainLayout.arrangeElements();

        this.frameLayout = new FrameLayout(getX(), getY(), getWidth(), getHeight());
        this.frameLayout.addChild(mainLayout, frameLayout.newChildLayoutSettings().alignHorizontallyLeft().padding(5,5));

        equipmentInfoWidgets.addAll(List.of(itemWidget, itemDisplayNameWidget, itemInfoWidget));
    }

    /**渲染背景 */
    protected void renderBackground(GuiGraphics graphics){
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        graphics.fillGradient( getX(), getY(), getX() + this.width, getY() + this.height, -1072689136, -804253680);
        poseStack.popPose();
    }

    public boolean isChange(){
        return !this.currentItemStack.equals(this.lastItemStack, true);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if(isChange()){
            this.alpha.set(0);
            this.rearrangeComponent();
        }
        this.alpha.increase(0.2f * mc.getDeltaFrameTime());

        this.renderBackground(graphics);
        this.renderInfo(graphics, mouseX, mouseY, partialTicks);

        setSlotStack(lastItemStack);
    }
}
