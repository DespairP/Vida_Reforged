package teamHTBP.vidaReforged.client.screen.components.common;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.screen.components.magicWords.MagicWordWidget;
import teamHTBP.vidaReforged.core.common.ui.style.Padding;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.helper.RenderHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;

/**
 * 可滚动的容器
 *
 * */
public class ScrolledContainer<T extends VidaWidget> extends VidaWidget{
    /**容器内容物*/
    List<VidaWidget> contents = new ArrayList<>();
    /**y轴滚动*/
    private AtomicInteger scrollY = new AtomicInteger(0);
    /**滚动条按钮大小*/
    private FloatRange scrollThumbAlpha = new FloatRange(0,0,0.3f);
    /**缓存*/
    private int cachedContentHeight = 0;
    /**缓存*/
    private boolean shouldUpdateCache = true;
    /**每个内容物在容器内渲染逻辑*/
    private BiConsumer<GuiGraphics, VidaWidget> renderFunc;

    public ScrolledContainer(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, Component.literal("scrollable container"));
        this.contents = new ArrayList<>();
    }

    /**设置容器内容渲染逻辑*/
    public void setContentRenderFunc(BiConsumer<GuiGraphics, VidaWidget> renderFunc) {
        this.renderFunc = renderFunc;
    }

    /**
     * 添加组件到容器中
     * @param widget 组件
     * @param containerInX 组件在容器内的相对位置X
     * @param containerInY 组件在容器内的相对位置Y
     */
    public void add(VidaWidget widget, int containerInX,int containerInY){
        add(widget);
        //
        widget.setX(getX() + containerInX);
        widget.setY(getY() + containerInY);
    }

    /**
     * 绝对位置加入组件
     * @param widget 组件
     */
    public void add(VidaWidget widget){
        if(widget == null){
            return;
        }
        //设置初始
        widget.setOffsetX(0);
        widget.setOffsetY(0);

        //
        shouldUpdateCache = true;
    }

    /**清空所有容器内组件*/
    public void clearAllComponent(){
        this.contents.clear();
        this.resetScrollTo(0, 0);
        shouldUpdateCache = true;
    }

    /**获取滚动高度*/
    public int getScrollY() {
        return scrollY.get();
    }

    /**设置滚动高度*/
    public void resetScrollTo(int scrollX, int scrollY){
        this.scrollY.set(scrollY);
        notifyComponentToFresh();
    }

    /**通知所有组件修改位置*/
    public void notifyComponentToFresh(){
        this.contents.forEach(widget -> widget.setOffset(0,  scrollY.get()));
    }


    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        scrollThumbAlpha.change(isHovered, 0.02f);

        renderBackground(graphics);


        renderThumbIcon(graphics, mouseX, mouseY);
    }

    /**渲染背景 */
    protected void renderBackground(GuiGraphics graphics){
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        graphics.fillGradient( getX(), getY(), getX() + this.width, getY() + this.height, -1072689136, -804253680);
        poseStack.popPose();
    }

    protected void renderContents(GuiGraphics graphics, float mouseX, float mouseY){
        PoseStack poseStack = graphics.pose();


        // 画子组件
        poseStack.pushPose();
        RenderSystem.enableBlend();
        poseStack.pushPose();
        RenderHelper.renderScissor(getX(),getY(), width, height);



        poseStack.popPose();
        RenderSystem.disableScissor();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }


    protected void renderThumbIcon(GuiGraphics graphics, float mouseX, float mouseY){
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());

        final float iconHeight = getSliderThumbHeight();

        Matrix4f matrix4f = poseStack.last().pose();
        matrix4f.translate(getX() + getWidth(), getY() - (scrollY.get() * height * 1.0f / getAllContentsHeight()), 0);
        consumer.vertex(matrix4f, 0, 0, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 0, iconHeight, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 6, iconHeight, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 6, 0, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        poseStack.popPose();
    }

    public float getSliderThumbHeight(){
        return (float) (Math.pow(height, 2) / getAllContentsHeight());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        int nextScroll = this.scrollY.get() + (int)scroll * 10;
        int maxScroll = getMaxScrollHeight();

        this.scrollY.set(nextScroll);
        if(nextScroll * -1 > maxScroll){
            this.scrollY.set(maxScroll * -1);
        }
        if(nextScroll > 0){
            this.scrollY.set(0);
        }

        notifyComponentToFresh();
        return true;
    }

    /**最大可以滚动的高度*/
    public int getMaxScrollHeight(){
        return Math.max(0, (int)( getAllContentsHeight() - height ));
    }

    /**整个页面的高度*/
    public int getAllContentsHeight(){
        if(!shouldUpdateCache){
            return cachedContentHeight;
        }
        final VidaWidget minYWidget = this.contents.stream().min(Comparator.comparing(VidaWidget::getY)).get();
        final VidaWidget maxYWidget = this.contents.stream().max(Comparator.comparing(VidaWidget::getY)).get();
        cachedContentHeight = maxYWidget.getY() + maxYWidget.getHeight() - minYWidget.getY();
        shouldUpdateCache = false;
        return cachedContentHeight;
    }
}
