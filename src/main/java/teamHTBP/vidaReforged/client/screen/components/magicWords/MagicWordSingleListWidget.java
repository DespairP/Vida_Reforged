package teamHTBP.vidaReforged.client.screen.components.magicWords;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaViewMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.helper.RenderHelper;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MagicWordSingleListWidget extends AbstractWidget {
    VidaViewMagicWordViewModel viewModel;
    public final Map<VidaElement, List<MagicWordSingleWidget>> widgetMap;
    private AtomicReference<Float> scroll = new AtomicReference<>(0F);
    private FloatRange scrollBarAlpha = new FloatRange(0,0,0.3f);

    public MagicWordSingleListWidget(VidaViewMagicWordViewModel model,int x, int y, int width, int height) {
        super(x, y, width, height, Component.translatable("magic word single list"));
        this.viewModel = model;
        this.widgetMap = new LinkedHashMap<>();
        initWidget();
    }

    public void initWidget(){
        for(VidaElement element : VidaElement.values()){
            widgetMap.put(element,new LinkedList<>());
        }

        int offset = 0;

        for(MagicWord word : MagicWordManager.getAllMagicWords()){
            int i = offset ++;
            int x = 0;
            int y = i * MagicWordWidget.HEIGHT;
            int offsetX = 5;
            int offsetY = i  * 10;
            MagicWordSingleWidget magicWordWidget = new MagicWordSingleWidget(viewModel, getX() + x + offsetX, getY() + y + offsetY, width - 10, word);
            widgetMap.get(word.element()).add(magicWordWidget);
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack poseStack = graphics.pose();
        // 背景
        poseStack.pushPose();
        graphics.fillGradient( getX(), getY(), getX() + this.width, getY() + this.height, -1072689136, -804253680);
        poseStack.popPose();

        // 画子组件
        poseStack.pushPose();
        RenderSystem.enableBlend();
        poseStack.translate(0,5,0);
        poseStack.pushPose();
        RenderHelper.renderScissor(getX(),getY(), width, height);
        getCurrentWidgetList().forEach((magicWordWidget -> magicWordWidget.render(graphics, mouseX, mouseY, partialTicks)));
        poseStack.popPose();
        RenderSystem.disableScissor();
        RenderSystem.disableBlend();
        poseStack.popPose();

        // 画滚动条
        drawScrollBar(graphics, mouseX, mouseY, partialTicks);
    }

    private List<MagicWordSingleWidget> getCurrentWidgetList() {
        List<MagicWordSingleWidget> widgets = new ArrayList<>();
        for(List<MagicWordSingleWidget> elementWidget : widgetMap.values()){
            widgets.addAll(elementWidget);
        }
        return widgets;
    }

    private void drawScrollBar(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        float alpha = scrollBarAlpha.change(isHovered, 0.02f);

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());

        final float iconHeight = getSliderThumbHeight();

        Matrix4f matrix4f = poseStack.last().pose();
        matrix4f.translate(getX() + getWidth(), getY() - (scroll.get() * height / getAllWordHeight()), 0);
        consumer.vertex(matrix4f, 0, 0, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 0, iconHeight, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 6, iconHeight, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 6, 0, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        poseStack.popPose();
    }

    public float getSliderThumbHeight(){
        return (float) (Math.pow(height, 2) / getAllWordHeight());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        float nextScroll = (float) (this.scroll.get() + scroll * 30);
        int maxScroll = getMaxScrollHeight();
        this.scroll.set(nextScroll);
        if(nextScroll * -1 > maxScroll){
            this.scroll.set(maxScroll * -1.0f);
        }
        if(nextScroll > 0){
            this.scroll.set(0f);
        }
        freshWidgets();
        return true;
    }

    private void freshWidgets() {
        getCurrentWidgetList().forEach(widget->{
            widget.setScroll(this.scroll.get());
        });
    }

    /**最大可以滚动的高度*/
    public int getMaxScrollHeight(){
        return Math.max(0, (int)( getAllWordHeight() - height ));
    }

    /**整个页面的高度*/
    public int getAllWordHeight(){
        int size = getCurrentWidgetList().size();
        return size * (MagicWordWidget.HEIGHT + 10);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    @Override
    public void onClick(double x, double y) {
        getCurrentWidgetList().forEach(widget -> widget.mouseClicked(x, y, 0));
    }

    public Collection<? extends GuiEventListener> getChildren(){
        List<MagicWordSingleWidget> listeners = new ArrayList<>();
        this.widgetMap.forEach((key,value)-> listeners.addAll(getCurrentWidgetList()));
        return listeners;
    }

    @Override
    public void playDownSound(SoundManager p_93665_) {

    }
}
