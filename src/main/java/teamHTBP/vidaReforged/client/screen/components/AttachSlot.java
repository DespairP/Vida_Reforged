package teamHTBP.vidaReforged.client.screen.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;

public class AttachSlot implements Renderable {
    float posX = 0;
    float posY = 0;

    public AttachSlot setPos(float x,float y){
        this.posX = x;
        this.posY = y;

        return this;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float patialTicks) {
        PoseStack poseStack = graphics.pose();
        RenderSystem.enableBlend();
        RenderSystem.disableCull();

        poseStack.pushPose();
        poseStack.translate(posX, posY,0);
        int w = 20;
        int h = 20;
        float s = 0.5f;
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer buffer = graphics.bufferSource().getBuffer(RenderType.gui());

        //上
        buffer.vertex( matrix4f, 0, 0, 0).color(1,1,1,1).endVertex();
        buffer.vertex( matrix4f, s, s, 0).color(1,1,1,0.8f).endVertex();
        buffer.vertex( matrix4f, w - s, s, 0).color(1,1,1,0.8f).endVertex();
        buffer.vertex( matrix4f, w , 0, 0).color(1,1,1,1).endVertex();

        //左
        buffer.vertex( matrix4f, 0, 0, 0).color(1,1,1,1).endVertex();
        buffer.vertex( matrix4f, 0, h, 0 ).color(1,1,1,0.5f).endVertex();
        buffer.vertex( matrix4f, s, h - s, 0 ).color(1,1,1,0.5f).endVertex();
        buffer.vertex( matrix4f, s, s, 0).color(1,1,1,1).endVertex();

        //下
        buffer.vertex( matrix4f, s, h - s, 0 ).color(1,1,1, 1).endVertex();
        buffer.vertex( matrix4f, 0, h, 0 ).color(1,1,1, 0.5f).endVertex();
        buffer.vertex( matrix4f, w, h, 0 ).color(1,1,1, 0.5f).endVertex();
        buffer.vertex( matrix4f, w - s, h -s, 0 ).color(1,1,1, 1).endVertex();

        //右
        buffer.vertex( matrix4f, w - s, s, 0 ).color(1,1,1,1).endVertex();
        buffer.vertex( matrix4f, w - s, h - s, 0 ).color(1,1,1,0.5f).endVertex();
        buffer.vertex( matrix4f, w, h, 0 ).color(1,1,1,0.5f).endVertex();
        buffer.vertex( matrix4f, w, 0, 0 ).color(1,1,1,1).endVertex();

        RenderSystem.disableBlend();
        poseStack.popPose();
    }
}
