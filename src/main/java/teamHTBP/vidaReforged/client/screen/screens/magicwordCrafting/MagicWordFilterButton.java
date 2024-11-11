package teamHTBP.vidaReforged.client.screen.screens.magicwordCrafting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.core.api.screen.StyleSheet;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.TwoValueGradientColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import java.util.Locale;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class MagicWordFilterButton<T> extends VidaWidget {
    /**按钮大小*/
    public static final int BTN_SIZE = 20;
    /**过滤的id*/
    private final T filterId;
    /**按钮渐变动画*/
    private FloatRange alphaRange = new FloatRange(0.1f,0.1f,0.3f);
    /**按钮是否被选中*/
    private Boolean isSelected = false;
    /**监听器*/
    private ClickListener<T> clickListener = (id) -> {};
    /**Thumb颜色*/
    @StyleSheet
    private ARGBColor iconFromColor = ARGBColor.BLACK;
    /**Thumb颜色*/
    @StyleSheet
    private ARGBColor iconToColor = ARGBColor.BLACK;
    /**背景颜色*/
    @StyleSheet
    private TwoValueGradientColor backgroundColor = new TwoValueGradientColor(ARGBColor.of(207, 217, 223), ARGBColor.of(226, 235, 240));


    public MagicWordFilterButton<T> setIconFromColor(ARGBColor iconFromColor){
        this.iconFromColor = iconFromColor;
        return this;
    }

    public MagicWordFilterButton<T> setIconToColor(ARGBColor iconToColor) {
        this.iconToColor = iconToColor;
        return this;
    }

    public MagicWordFilterButton(int x, int y, int width, int height, T id) {
        super(x, y, width, height, Component.literal("filter_button:" + id.toString()), new ResourceLocation(MOD_ID, "filter_button_" + id.toString().toLowerCase(Locale.US)));
        this.filterId = id;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        float alpha = alphaRange.change(isHovered,0.02f);

        // 渲染图标
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        TextureSection section = new TextureSection(getElemenetIcon(),0,0,16,16, 16, 16);
        graphics.blit(
                section.location(),
                getX() + 2 , getY() + 2, 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                16, 16
        );
        poseStack.popPose();

        // 渲染
        float fromR = backgroundColor.getFromColor().r() / 255.0f;
        float fromG = backgroundColor.getFromColor().g() / 255.0f;
        float fromB = backgroundColor.getFromColor().b() / 255.0f;

        float toR = backgroundColor.getToColor().r() / 255.0f;
        float toG = backgroundColor.getToColor().g() / 255.0f;
        float toB = backgroundColor.getToColor().b() / 255.0f;

        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());
        matrix4f.translate(getX(), getY(), 0);
        consumer.vertex(matrix4f, 0, 0, 0).color(fromR, fromG, fromB, alpha).endVertex();
        consumer.vertex(matrix4f, 0, getHeight(), 0).color(fromR, fromG, fromB, alpha).endVertex();
        consumer.vertex(matrix4f, getWidth(), getHeight(), 0).color(toR, toG, toB, alpha).endVertex();
        consumer.vertex(matrix4f, getWidth(), 0, 0).color(toR, toG, toB, alpha).endVertex();
        poseStack.popPose();

        this.renderFocused(graphics, mouseX, mouseY, partialTicks);
    }

    /**渲染Thumb*/
    public void renderFocused(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if(!isSelected){
            return;
        }

        PoseStack poseStack = graphics.pose();

        float fromR = iconFromColor.r() / 255.0f;
        float fromG = iconFromColor.g() / 255.0f;
        float fromB = iconFromColor.b() / 255.0f;

        float toR = iconToColor.r() / 255.0f;
        float toG = iconToColor.g() / 255.0f;
        float toB = iconToColor.b() / 255.0f;

        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());

        matrix4f.translate(getX() + getWidth() - 2, getY(), 0);
        consumer.vertex(matrix4f, 0, 0, 0).color(toR, toG, toB, 1).endVertex();
        consumer.vertex(matrix4f, 0, getHeight(), 0).color(fromR, fromG, fromB, 1).endVertex();
        consumer.vertex(matrix4f, 2, getHeight(), 0).color(fromR, fromG, fromB, 1).endVertex();
        consumer.vertex(matrix4f, 2, 0, 0).color(toR, toG, toB, 1).endVertex();
        poseStack.popPose();
    }

    public ResourceLocation getElemenetIcon(){
        return new ResourceLocation(MOD_ID, String.format("textures/icons/%slogo.png", filterId.toString().toLowerCase(Locale.ROOT)));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.HINT, getMessage());
    }

    public void setSelected(Boolean selected) {
        this.isSelected = selected;
    }

    public void setClickListener(ClickListener<T> clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if(this.clickListener != null){
            clickListener.onClick(filterId);
        }
    }

    public interface ClickListener<T> {
        public void onClick(T id);
    }
}
