package teamHTBP.vidaReforged.client.screen.components.magicWords;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.RenderHelper;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MagicWordListWidget extends AbstractWidget {
    public final static int WIDTH = 180;
    public final Map<VidaElement, List<MagicWordWidget>> widgetMap;
    public VidaElement currentSelectedElement = VidaElement.GOLD;
    private AtomicInteger scroll = new AtomicInteger(0);
    private FloatRange scrollBarAlpha = new FloatRange(0,0,0.3f);
    VidaMagicWordViewModel model;

    public MagicWordListWidget(VidaMagicWordViewModel model, int x, int y, int width, int height,double factor) {
        super(x, y, (int)(WIDTH), height, Component.translatable("Magic Word List"));
        this.widgetMap = new LinkedHashMap<>();
        this.model = model;
        this.initWidget();
    }

    public void initWidget(){
        for(VidaElement element : VidaElement.values()){
            widgetMap.put(element,new LinkedList<>());
        }
        Map<VidaElement,AtomicInteger> offset = ImmutableMap.of(
                VidaElement.GOLD, new AtomicInteger(0),
                VidaElement.WOOD, new AtomicInteger(0),
                VidaElement.AQUA, new AtomicInteger(0),
                VidaElement.FIRE, new AtomicInteger(0),
                VidaElement.EARTH, new AtomicInteger(0)
        );

        for(MagicWord word : MagicWordManager.getAllMagicWords()){
            int i = offset.getOrDefault(word.element(), new AtomicInteger(0)).getAndIncrement();
            int x = (i % 2) * MagicWordWidget.WIDTH;
            int y = (int)Math.floor(i / 2.0f) * MagicWordWidget.HEIGHT;
            int offsetX = (i % 2) * 5;
            int offsetY = (int)Math.floor(i / 2.0f) * 10;
            MagicWordWidget magicWordWidget = new MagicWordWidget(model, this, getX() + x + offsetX, getY() + y + offsetY, word);
            widgetMap.get(word.element()).add(magicWordWidget);
        }

        this.currentSelectedElement = this.model.selectedFilterElement.getValue();
        this.model.selectedFilterElement.observe(newValue -> {
            this.currentSelectedElement = newValue;
            this.scroll.set(0);
        });
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
        RenderHelper.renderScissor(getX(),getY(), width, height);
        getCurrentWidgetList().forEach((magicWordWidget -> magicWordWidget.render(graphics, mouseX, mouseY, partialTicks)));
        RenderSystem.disableScissor();
        RenderSystem.disableBlend();
        poseStack.popPose();

        // 画滚动条
        drawScrollBar(graphics, mouseX, mouseY, partialTicks);
    }

    private void drawScrollBar(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        float alpha = scrollBarAlpha.change(isHovered, 0.02f);

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());
        int iconHeight = getHeight() - getMaxScrollHeight();
        Matrix4f matrix4f = poseStack.last().pose();
        matrix4f.translate(getX() + getWidth(), getY() - scroll.get(), 0);
        consumer.vertex(matrix4f, 0, 0, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 0, iconHeight, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 6, iconHeight, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 6, 0, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        poseStack.popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        int nextScroll = this.scroll.get() + (int)scroll * 10;
        int maxScroll = getMaxScrollHeight();
        this.scroll.set(nextScroll);
        if(nextScroll * -1 > maxScroll){
            this.scroll.set(maxScroll * -1);
        }
        if(nextScroll > 0){
            this.scroll.set(0);
        }
        return true;
    }

    public int getScroll() {
        return scroll.get();
    }

    public List<MagicWordWidget> getCurrentWidgetList(){
        return widgetMap.get(currentSelectedElement);
    }

    public int getMaxScrollHeight(){
        int size = getCurrentWidgetList().size();
        int allWordHeight = (int)Math.floor(size / 2.0f) * (MagicWordWidget.HEIGHT + 10);
        return Math.max(0, allWordHeight - getHeight());
    }

    @Override
    public void onClick(double x, double y) {
        widgetMap.get(currentSelectedElement).forEach(widget -> widget.mouseClicked(x, y, 0));
    }

    public Collection<? extends GuiEventListener> getChildren(){
        List<MagicWordWidget> listeners = new ArrayList<>();
        this.widgetMap.forEach((key,value)->listeners.addAll(value));
        return listeners;
    }
}
