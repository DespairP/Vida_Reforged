package teamHTBP.vidaReforged.client.screen.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.component.IDataObserver;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class MagicWordFilter extends AbstractWidget {
    public static final int PIXEL = 20;
    private final VidaElement element;
    private FloatRange alphaRange = new FloatRange(0.1f,0.1f,0.3f);
    VidaMagicWordViewModel model;
    private Boolean isSelected = false;

    private static final Map<VidaElement, List<ARGBColor>> colorMap = ImmutableMap.of(
            VidaElement.GOLD, ImmutableList.of(ARGBColor.of(246, 211, 101), ARGBColor.of(253, 160, 133)),
            VidaElement.WOOD, ImmutableList.of(ARGBColor.of(11, 163, 96), ARGBColor.of(60, 186, 146)),
            VidaElement.AQUA, ImmutableList.of(ARGBColor.of(72, 198, 239), ARGBColor.of(111, 134, 214)),
            VidaElement.FIRE, ImmutableList.of(ARGBColor.of(255, 88, 88), ARGBColor.of(240, 152, 25)),
            VidaElement.EARTH, ImmutableList.of(ARGBColor.of(230, 185, 128), ARGBColor.of(234, 205, 163))
    );

    public MagicWordFilter(VidaMagicWordViewModel model, int x, int y, VidaElement element) {
        super(x, y, PIXEL, PIXEL, Component.translatable("magic_word_filter"));
        this.element = element;
        this.model = model;
        IDataObserver<VidaElement> elementObserver = (value) -> {
            this.setFocused(value == this.element);
            this.setSelected(value == this.element);
        };
        this.model.selectedFilterElement.observe(elementObserver);
        this.setFocused(model.selectedFilterElement.getValue() == element);
        this.setSelected(model.selectedFilterElement.getValue() == element);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        float alpha = alphaRange.change(isHovered,0.02f);

        // 渲染图标
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        TextureSection section = new TextureSection(getElemenetIcon(),0,0,16,16);
        graphics.blit(
                section.location(),
                getX() + 2 , getY() + 2, 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                16, 16
        );
        poseStack.popPose();

        // 渲染
        ARGBColor fromColor = ARGBColor.of(207, 217, 223);
        float fromR = fromColor.r() / 255.0f;
        float fromG = fromColor.g() / 255.0f;
        float fromB = fromColor.b() / 255.0f;

        ARGBColor toColor = ARGBColor.of(226, 235, 240);
        float toR = toColor.r() / 255.0f;
        float toG = toColor.g() / 255.0f;
        float toB = toColor.b() / 255.0f;

        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());
        matrix4f.translate(getX(), getY(), 0);
        consumer.vertex(matrix4f, 0, 0, 0).color(fromR, fromG, fromB, alpha).endVertex();
        consumer.vertex(matrix4f, 0, PIXEL, 0).color(fromR, fromG, fromB, alpha).endVertex();
        consumer.vertex(matrix4f, PIXEL, PIXEL, 0).color(toR, toG, toB, alpha).endVertex();
        consumer.vertex(matrix4f, PIXEL, 0, 0).color(toR, toG, toB, alpha).endVertex();
        poseStack.popPose();

        this.renderFocused(graphics, mouseX, mouseY, partialTicks);
    }

    public void renderFocused(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if(!isSelected){
            return;
        }

        PoseStack poseStack = graphics.pose();

        ARGBColor fromColor = colorMap.get(element).get(0);
        float fromR = fromColor.r() / 255.0f;
        float fromG = fromColor.g() / 255.0f;
        float fromB = fromColor.b() / 255.0f;

        ARGBColor toColor = colorMap.get(element).get(1);
        float toR = toColor.r() / 255.0f;
        float toG = toColor.g() / 255.0f;
        float toB = toColor.b() / 255.0f;

        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());

        matrix4f.translate(getX() + PIXEL - 2, getY(), 0);
        consumer.vertex(matrix4f, 0, 0, 0).color(toR, toG, toB, 1).endVertex();
        consumer.vertex(matrix4f, 0, PIXEL, 0).color(fromR, fromG, fromB, 1).endVertex();
        consumer.vertex(matrix4f, 2, PIXEL, 0).color(fromR, fromG, fromB, 1).endVertex();
        consumer.vertex(matrix4f, 2, 0, 0).color(toR, toG, toB, 1).endVertex();
        poseStack.popPose();
    }

    public ResourceLocation getElemenetIcon(){
        return new ResourceLocation(MOD_ID, String.format("textures/icons/%slogo.png", element.toString().toLowerCase(Locale.ROOT)));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    @Override
    public boolean isFocused() {
        return super.isFocused();
    }

    @Override
    public void onClick(double x, double y) {
        this.model.selectedFilterElement.setValue(this.element);
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
