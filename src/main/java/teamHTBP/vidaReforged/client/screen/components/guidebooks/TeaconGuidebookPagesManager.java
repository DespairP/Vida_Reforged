package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaTeaconGuidebookViewModel;
import teamHTBP.vidaReforged.core.utils.animation.Animator;
import teamHTBP.vidaReforged.core.utils.animation.DestinationAnimator;
import teamHTBP.vidaReforged.core.utils.animation.TimeInterpolator;
import teamHTBP.vidaReforged.core.utils.animation.calculator.IValueProvider;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class TeaconGuidebookPagesManager implements IGuidebookComponent{
    private final VidaTeaconGuidebookViewModel viewModel;
    private int width;
    private int height;
    private IGuidebookComponent rightComponent = null;
    private Component title;
    private Component subTitle;
    private Font font;
    /**丁卯字体*/
    public static ResourceLocation DINKFONT = new ResourceLocation(MOD_ID, "dinkie");
    private FloatRange subTitleAlpha = new FloatRange(0, 0, 1.0f);

    private FloatRange horizonLineLength = new FloatRange(0f,0f,390f);

    public TeaconGuidebookPagesManager(VidaTeaconGuidebookViewModel viewModel) {
        this.viewModel = viewModel;
        this.font = Minecraft.getInstance().font;
    }

    public TeaconGuidebookPagesManager setHeight(int height) {
        this.height = height;
        return this;
    }

    public TeaconGuidebookPagesManager setWidth(int width) {
        this.width = width;
        return this;
    }

    public void init(){
        final int textWidth = (int)(this.width * 1.9f / 3.0f);
        final int textHeight = (int)(this.height * 2.0f / 3.0f);
        final int x = (int)(this.width * 1.0f / 3.0f);
        final int y = (int)(this.height - textHeight) * 2 / 3;
        int currentIndex = viewModel.page.getValue() - 1;
        this.rightComponent = viewModel.getGuidebook()
                .pages()
                .get(currentIndex)
                .right()
                .initComponent(x, y, textWidth, textHeight);
        this.title = viewModel.getGuidebook().getTitle();
        this.subTitle = viewModel.getGuidebook().pages().get(currentIndex).getSubTitle();
        this.viewModel.page.observe(page -> {
            this.rightComponent = viewModel.getGuidebook()
                    .pages()
                    .get(page - 1)
                    .right()
                    .initComponent(x, y, textWidth, textHeight);
            this.subTitle = viewModel.getGuidebook().pages().get(page - 1).getSubTitle();
            if(this.subTitleAlpha != null){
                this.subTitleAlpha.set(0);
            }
        });
    }


    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if(this.rightComponent != null){
            this.rightComponent.render(graphics, mouseX, mouseY, partialTicks);
        }
        if(this.title != null){
            this.renderTitle(graphics, mouseX, mouseY, partialTicks);
        }
        renderHorizonLineAndSubTilte(graphics, mouseX, mouseY, partialTicks);
    }

    public void renderTitle(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        final int x = (int)(this.width * 1.0 / 10);
        final int y = (int)(this.height * 1.0 / 10);

        PoseStack poseStack = graphics.pose();
        float scale = 1.3f;
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        graphics.drawString(font, title, (int)(x * 1.0 / scale) ,(int)(y * 1.0 / scale) ,0xFFFFFFFF);
        poseStack.popPose();
    }

    public void renderHorizonLineAndSubTilte(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        this.horizonLineLength.increase(5);
        this.subTitleAlpha.increase(0.02f);

        PoseStack poseStack = graphics.pose();
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        int x = (int)(this.width * 1.0 / 10);
        int y = (int)(this.height * 1.0 / 10);
        x = (int)(x * 1.0 / 1.3F);
        y = (int)(y * 1.0 / 1.3F + font.lineHeight * 2.1f) + 2;
        float alpha = 0.6f;
        float length = horizonLineLength.get();

        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        matrix4f.translate(
                x,
                y,
                0
        );

        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());
        consumer.vertex(matrix4f, 0, 0, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 0, 1.2f, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, length, 1.2f, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, length, 0, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        poseStack.popPose();

        float scale = 0.9f;


        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        int color = ARGBColor.of(subTitleAlpha.get(), 1, 1, 1).argb();
        graphics.drawString(font, Component.empty().append(subTitle).withStyle(style -> style.withFont(DINKFONT)) ,(int)((x + 100) * 1.0f / scale),(int)(y * 1.0f / scale) + 8, color);
        poseStack.popPose();
    }

    @Override
    public void setFocused(boolean p_265728_) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    public Collection<? extends GuiEventListener> getChildren(){
        List<GuiEventListener> listeners = new ArrayList<>();
        if(this.rightComponent != null){
            listeners.add((GuiEventListener) this.rightComponent);
        }
        return listeners;
    }
}
