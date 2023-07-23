package teamHTBP.vidaReforged.client.screen.components.jigsaw;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.screen.components.AbstractVidaWidget;
import teamHTBP.vidaReforged.core.utils.animation.DestinationAnimator;

public class AttachSlot extends AbstractVidaWidget implements Renderable {
    /***/
    private float offsetPosX = 0;
    /***/
    private float offsetPosY = 0;
    /***/
    private boolean isInSlotContainer = false;
    public static final int SLOT_WIDTH = 20;
    public static final int SLOT_HEIGHT = 20;
    private float alpha = 0;
    private float strokeWidth = 1;
    private int containerRowNum = 0;
    private int containerColNum = 0;



    /***/
    public AttachSlot(float x,float y) {
        this.x = x;
        this.y = y;
        this.width = SLOT_WIDTH;
        this.height = SLOT_HEIGHT;
    }

    public AttachSlot(float containerPosX,float containerPosY,float offsetX,float offsetY){
        this(containerPosX + offsetX, containerPosY + offsetY);
        this.isInSlotContainer = true;
        this.offsetPosX = offsetX;
        this.offsetPosY = offsetY;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float patialTicks) {
        PoseStack poseStack = graphics.pose();
        RenderSystem.enableBlend();
        RenderSystem.disableCull();

        float a = getAlpha(mouseX,mouseY);

        poseStack.pushPose();
        poseStack.translate(offsetPosX, offsetPosY, 0);
        int w = SLOT_WIDTH;
        int h = SLOT_HEIGHT;
        float s = strokeWidth;
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


    private float getAlpha(int mouseX,int mouseY){
        if(this.isMouseOver(mouseX,mouseY)){
            alpha = Math.min(1, alpha + 0.03f);
        }else {
            alpha = Math.max(0.05F,alpha - 0.03f);
        }
        return this.alpha;
    }
}
