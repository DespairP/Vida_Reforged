package teamHTBP.vidaReforged.client.screen.components.common;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.core.common.ui.style.Padding;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.GuiHelper;

public class IconButton extends VidaWidget{
    /**材质路径*/
    protected TextureSection section;
    /**文字*/
    protected Component text;
    /**透明度变化速度*/
    protected float speed = 0.02f;
    private FloatRange alpha = new FloatRange(0,0.0f,1f);
    private GridLayout gridLayout;
    private Padding padding = Padding.empty();
    private ImageWidget imageWidget;
    private StringWidget textWidget;
    private boolean shouldUpdate = true;


    public IconButton(int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal("button"));
        gridLayout = new GridLayout(getX(), getY());
    }

    public IconButton setPadding(int top, int left){
        this.padding = new Padding(top, left, top, left);
        this.shouldUpdate = true;
        return this;
    }

    public IconButton setIcon(TextureSection section) {
        this.section = section;
        this.imageWidget = section == null ? null : new ImageWidget(0, 0, section.w(), section.h(), section.location());
        this.shouldUpdate = true;
        return this;
    }

    public IconButton setText(Component text){
        this.text = text;
        this.textWidget = text == null ? null : new StringWidget(0, 0, text, Minecraft.getInstance().font);

        this.shouldUpdate = true;
        return this;
    }

    protected void init(){
        if(!shouldUpdate){
            return;
        }

        this.gridLayout.setX(getX() + padding.left());
        this.gridLayout.setY(getY() + padding.top());

        int remainingWidth = getWidth() - padding.left() - padding.right();

        // 组件放进左边内部盒中
        if(this.imageWidget != null) {
            int imageSectionWidget = this.textWidget == null ? remainingWidth : section.width();
            FrameLayout leftInnerBox = new FrameLayout(0, 0, imageSectionWidget, getHeight() - padding.top() - padding.bottom());
            leftInnerBox.addChild(this.imageWidget, leftInnerBox.newChildLayoutSettings().alignHorizontallyCenter());
            this.gridLayout.addChild(leftInnerBox, 0, 0);
            remainingWidth -= imageSectionWidget;
        }

        // 组件放进左边内部盒中
        if(this.textWidget != null) {
            FrameLayout innerBox = new FrameLayout(0, 0, remainingWidth, getHeight() - padding.top() - padding.bottom());
            innerBox.addChild(this.textWidget, innerBox.newChildLayoutSettings().alignHorizontallyCenter().alignVerticallyMiddle());
            this.gridLayout.addChild(innerBox, 0, 1, innerBox.newChildLayoutSettings().alignHorizontallyCenter().alignVerticallyMiddle());
        }

        this.gridLayout.arrangeElements();
        this.shouldUpdate = false;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        final float alpha = this.alpha.get();
        this.alpha.change(isHovered && alpha <= 0.5f, speed);

        PoseStack poseStack = graphics.pose();

        init();

        poseStack.pushPose();
        RenderSystem.enableBlend();
        GuiHelper.renderScissor(getX(), getY(), width, height);

        renderWidgetBackground(graphics);
        renderHoverBackground(graphics);
        renderChildren(graphics, mouseX, mouseY, partialTicks);

        RenderSystem.disableScissor();
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    /**渲染图标*/
    public void renderChildren(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if(this.imageWidget != null) {
            imageWidget.renderWidget(graphics, mouseX, mouseY, partialTicks);
        }
        if(this.textWidget != null){
            textWidget.renderWidget(graphics, mouseX, mouseY, partialTicks);
        }
    }

    protected void renderHoverBackground(GuiGraphics graphics){
        ARGBColor color = ARGBColor.of(alpha.get(), 0.6f, 0.6f, 0.6f);

        graphics.fillGradient(getX(), getY(), getX() + width, getY() + height, color.argb(), color.argb());
    }

    /**渲染背景*/
    protected void renderWidgetBackground(GuiGraphics graphics){
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        graphics.fillGradient(getX(), getY(), getX() + width, getY() + height, 0xA0101010, 0xB0101010);
        poseStack.popPose();
    }

    @Override
    public void onClick(double p_93634_, double p_93635_) {
        super.onClick(p_93634_, p_93635_);
        this.alpha.set(0.8f);
    }

    public interface ClickListener{
        public void onClick(String id, VidaWidget widget);
    }
}
