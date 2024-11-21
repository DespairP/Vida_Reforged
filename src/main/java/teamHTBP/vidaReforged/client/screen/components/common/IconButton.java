package teamHTBP.vidaReforged.client.screen.components.common;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Data;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.common.ui.style.Padding;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

public class IconButton extends VidaWidget {
    /**文字*/
    protected Component text;
    /**透明度变化速度*/
    protected float speed = 0.02f;
    private FloatRange alpha = new FloatRange(0,0.0f,0.5f);
    private GridLayout gridLayout;
    private Padding padding = Padding.empty();
    private ImageMutableWidget imageWidget;
    private StringWidget textWidget;
    private boolean isBackground = false;
    TextureSection normalSection;
    /***/
    TextureSection activeSection;
    private int iconRev;
    private ClickListener listener;

    @Data
    @Accessors(chain = true, fluent = true)
    public static class Builder{
        private Component message;
        private TextureSection imageTex;
        private TextureSection imageActiveTex;
        private int width;
        private int height;
        private boolean isBackground = false;
        private Padding padding = new Padding(0, 0, 0, 0);
        private int rev;
        private ClickListener listener;

        public Builder(){}

        public IconButton build(int x, int y){
            IconButton button = new IconButton(x, y, width, height, message);
            button.setIcon(imageTex, rev)
                    .setActiveIcon(imageActiveTex)
                    .setPadding(padding)
                    .setBackground(isBackground)
                    .setListener(listener);
            return button;
        }
    }


    public IconButton(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        this.gridLayout = new GridLayout(getX(), getY());
        this.imageWidget = new ImageMutableWidget(0, 0, 0, 0, Component.literal("image"), null);
        this.textWidget = new StringWidget(component, mc.font);
        init();
    }

    public IconButton setPadding(Padding padding) {
        this.padding = padding;
        return this;
    }

    public IconButton setBackground(boolean visible){
        this.isBackground = visible;
        return this;
    }

    public IconButton setIcon(TextureSection section, int iconRev) {
        this.normalSection = section;
        this.iconRev = iconRev;
        this.imageWidget.setSection(section);
        this.imageWidget.setRev(iconRev);
        return this;
    }

    public IconButton setActiveIcon(TextureSection section) {
        this.activeSection = section;
        return this;
    }

    public IconButton setText(Component text){
        this.text = text;
        this.textWidget.setMessage(text);
        return this;
    }

    public IconButton setListener(ClickListener listener) {
        this.listener = listener;
        return this;
    }

    public void refresh(){
        this.init();
    }

    protected void init(){
        this.gridLayout = new GridLayout(getX(), getY());
        this.gridLayout.setX(getX() + padding.left());
        this.gridLayout.setY(getY() + padding.top());

        int remainingWidth = getWidth() - padding.left() - padding.right();

        // 组件放进左边内部盒中
        int imageSectionWidget = imageWidget.getWidth();
        FrameLayout leftInnerBox = new FrameLayout(0, 0, imageSectionWidget, getHeight() - padding.top() - padding.bottom());
        leftInnerBox.addChild(this.imageWidget, leftInnerBox.newChildLayoutSettings().alignHorizontallyCenter());
        this.gridLayout.addChild(leftInnerBox, 0, 0);
        remainingWidth -= imageSectionWidget;

        // 组件放进左边内部盒中
        FrameLayout innerBox = new FrameLayout(0, 0, remainingWidth, getHeight() - padding.top() - padding.bottom());
        innerBox.addChild(this.textWidget, innerBox.newChildLayoutSettings().alignHorizontallyCenter().alignVerticallyMiddle());
        this.gridLayout.addChild(innerBox, 0, 1, innerBox.newChildLayoutSettings().alignHorizontallyCenter().alignVerticallyMiddle());
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        final float alpha = this.alpha.get();
        this.alpha.change(isHovered, Minecraft.getInstance().getDeltaFrameTime() * 0.2f);

        PoseStack poseStack = graphics.pose();

        this.gridLayout.setX(getX());
        this.gridLayout.setY(getY());
        this.gridLayout.arrangeElements();

        poseStack.pushPose();
        RenderSystem.enableBlend();

        if(isBackground) {
            renderWidgetBackground(graphics);
            renderHoverBackground(graphics);
        }
        renderChildren(graphics, mouseX, mouseY, partialTicks);

        RenderSystem.disableScissor();
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    /**渲染图标*/
    public void renderChildren(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if(this.imageWidget != null) {
            imageWidget.setSection(isHovered ? activeSection : normalSection);
            imageWidget.renderWidget(graphics, mouseX, mouseY, partialTicks);
        }
        if(this.textWidget != null){
            textWidget.renderWidget(graphics, mouseX, mouseY, partialTicks);
        }
    }

    protected void renderHoverBackground(GuiGraphics graphics){
        // 渲染背景
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
    public void onClick(double mouseX, double mouseY) {
        if(listener != null){
            this.listener.onClick();
        }
    }


    public interface ClickListener{
        public void onClick();
    }
}
