package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.screen.components.magicWords.MagicWordWidget;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaTeaconGuidebookViewModel;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.RenderHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GuiBookPagination extends AbstractWidget {
    public static final int WIDTH = 70;
    public static final int HEIGHT = 20;
    final VidaTeaconGuidebookViewModel viewModel;

    final Button leftButton;

    final Button rightButton;


    public GuiBookPagination(VidaTeaconGuidebookViewModel viewModel, int x, int y) {
        super(x, y, WIDTH, HEIGHT, Component.translatable("GuiBookPagination"));
        this.viewModel = viewModel;
        this.leftButton = new Button(this.viewModel::decreasePage, getX() + 5, getY() + 5, true);
        this.rightButton = new Button(this.viewModel::increasePage, getX() + getWidth() - 5, getY() + 5);
        this.init();
    }

    public void init(){
        this.viewModel.page.observe(page -> {
            this.leftButton.setEnabled(false);
            this.rightButton.setEnabled(false);
            if (page > 1){
                this.leftButton.setEnabled(true);
            }
            if (page < viewModel.maxPage){
                this.rightButton.setEnabled(true);
            }
        });

        this.leftButton.setEnabled(false);
        this.rightButton.setEnabled(false);
        if (viewModel.page.getValue() > 1){
            this.leftButton.setEnabled(true);
        }
        if (viewModel.page.getValue() < viewModel.maxPage){
            this.rightButton.setEnabled(true);
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBg(graphics, mouseX, mouseY, partialTicks);
        renderButton(graphics, mouseX, mouseY, partialTicks);
    }

    /**渲染*/
    protected void renderBg(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        graphics.fillGradient(
                getX(),
                getY(),
                getX() + this.width,
                getY() + this.height,
                0x50000000,
                0x20000000
        );
        poseStack.popPose();
    }

    protected void renderButton(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        this.rightButton.render(graphics, mouseX, mouseY, partialTicks);
        this.leftButton.render(graphics, mouseX, mouseY, partialTicks);

        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        RenderSystem.enableBlend();

        poseStack.translate(26, 6 , 0);
        graphics.drawString(
                Minecraft.getInstance().font,
                Component.translatable("%s / %s",viewModel.page.getValue(), viewModel.maxPage),
                getX(),
                getY(),
                0xFFFFFFFF
        );

        RenderSystem.disableScissor();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }


    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    public Collection<? extends GuiEventListener> getChildren(){
        List<GuiEventListener> listeners = new ArrayList<>();
        listeners.add(leftButton);
        listeners.add(rightButton);
        return listeners;
    }

    public class Button extends AbstractWidget{
        Runnable clickFunc;

        private boolean isEnabled = false;

        private boolean isLeft = false;

        private FloatRange alpha = new FloatRange(0.2f, 0.2f, 0.6f);

        public Button(Runnable clickFunc,int x, int y) {
            super(x, y, 10, 10, Component.translatable("page"));
            this.clickFunc = clickFunc;
        }

        public Button(Runnable clickFunc,int x, int y,boolean isLeft) {
            super(x, y, 10, 10, Component.translatable("page"));
            this.clickFunc = clickFunc;
            this.isLeft = isLeft;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            float alpha = this.alpha.change(isHovered,0.02f);
            PoseStack poseStack = graphics.pose();

            poseStack.pushPose();
            Matrix4f matrix4f = poseStack.last().pose();
            VertexConsumer buffer = graphics.bufferSource().getBuffer(RenderType.gui());
            matrix4f.translate(getX(), getY(), 0);
            ARGBColor fromColor = ARGBColor.of(255, 255, 255);
            float fromR = fromColor.r() / 255.0f;
            float fromG = fromColor.g() / 255.0f;
            float fromB = fromColor.b() / 255.0f;

            ARGBColor toColor = ARGBColor.of(64, 64, 64);
            float toR = toColor.r() / 255.0f;
            float toG = toColor.g() / 255.0f;
            float toB = toColor.b() / 255.0f;

            if(isLeft) {
                buffer.vertex(matrix4f, 0, getHeight() / 2, 0).color(fromR, fromG, fromB, alpha).endVertex();
                buffer.vertex(matrix4f, 0, getHeight() / 2, 0).color(toR, toG, toB, alpha).endVertex();
                buffer.vertex(matrix4f, getWidth(), getHeight(), 0).color(fromR, fromR, fromR, alpha).endVertex();
                buffer.vertex(matrix4f, getWidth(), 0, 0).color(toR, toG, toB, alpha).endVertex();
                poseStack.popPose();
                return;
            }

            buffer.vertex(matrix4f, 0, 0, 0).color(fromR, fromG, fromB, alpha).endVertex();
            buffer.vertex(matrix4f, 0, getHeight(), 0).color(toR, toG, toB, alpha).endVertex();
            buffer.vertex(matrix4f, getWidth() / 2 + 4, getHeight() / 2, 0).color(fromR, fromR, fromR, alpha).endVertex();
            buffer.vertex(matrix4f, getWidth() / 2 + 4, getHeight() / 2, 0).color(toR, toG, toB, alpha).endVertex();


            poseStack.popPose();
        }

        public void setEnabled(boolean enabled) {
            isEnabled = enabled;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

        }

        @Override
        public void onClick(double p_93634_, double p_93635_) {
            if(!isEnabled){
                return;
            }
            this.clickFunc.run();
        }
    }
}
