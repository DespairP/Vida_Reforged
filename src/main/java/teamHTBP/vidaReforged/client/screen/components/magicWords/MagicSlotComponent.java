package teamHTBP.vidaReforged.client.screen.components.magicWords;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;

public class MagicSlotComponent extends VidaWidget {
    Slot slot;
    FloatRange hoverAlpha = new FloatRange(0.0F,0.0F, 0.15F);
    FloatRange itemAlpha = new FloatRange(0.1F,0.1F,0.85F);
    Minecraft mc;

    public MagicSlotComponent(Slot slot, int x, int y) {
        super(x, y, 20, 20, Component.translatable("slot"));
        this.mc = Minecraft.getInstance();
        this.slot = slot;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack poseStack = graphics.pose();
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        float a = 0;
        a += hoverAlpha.change(isHovered, 0.1F * mc.getDeltaFrameTime());
        a += itemAlpha.change(slot != null && slot.hasItem(), 0.08F * mc.getDeltaFrameTime());

        poseStack.pushPose();
        poseStack.translate(getX() - 2, getY() - 2, 0);
        int w = getWidth();
        int h = getHeight();
        float s = 1.1f;
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer buffer = graphics.bufferSource().getBuffer(RenderType.gui());

        //上
        buffer.vertex( matrix4f, 0, 0, 0).color(1,1,1,a).endVertex();
        buffer.vertex( matrix4f, s, s, 0).color(1,1,1,a).endVertex();
        buffer.vertex( matrix4f, w - s, s, 0).color(1,1,1, Math.min(a,0.5f)).endVertex();
        buffer.vertex( matrix4f, w , 0, 0).color(1,1,1, Math.min(a,0.5f)).endVertex();

        //左
        buffer.vertex( matrix4f, 0, 0, 0).color(1,1,1,a).endVertex();
        buffer.vertex( matrix4f, 0, h, 0 ).color(1,1,1,a).endVertex();
        buffer.vertex( matrix4f, s, h - s, 0 ).color(1,1,1,Math.min(a,0.5f)).endVertex();
        buffer.vertex( matrix4f, s, s, 0).color(1,1,1,Math.min(a,0.5f)).endVertex();

        //下
        buffer.vertex( matrix4f, s, h - s, 0 ).color(1,1,1, a).endVertex();
        buffer.vertex( matrix4f, 0, h, 0 ).color(1,1,1, a).endVertex();
        buffer.vertex( matrix4f, w, h, 0 ).color(1,1,1, Math.min(a,0.5f)).endVertex();
        buffer.vertex( matrix4f, w - s, h -s, 0 ).color(1,1,1, Math.min(a,0.5f)).endVertex();

        //右
        buffer.vertex( matrix4f, w - s, s, 0 ).color(1,1,1, a).endVertex();
        buffer.vertex( matrix4f, w - s, h - s, 0 ).color(1,1,1, a).endVertex();
        buffer.vertex( matrix4f, w, h, 0 ).color(1,1,1,Math.min(a,0.5f)).endVertex();
        buffer.vertex( matrix4f, w, 0, 0 ).color(1,1,1,Math.min(a,0.5f)).endVertex();

        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }
}
