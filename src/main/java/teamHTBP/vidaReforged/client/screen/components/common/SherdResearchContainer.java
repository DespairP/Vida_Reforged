package teamHTBP.vidaReforged.client.screen.components.common;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.helper.VidaMathHelper;
import teamHTBP.vidaReforged.server.menu.slots.SherdSlot;

import java.util.ArrayList;
import java.util.List;

public class SherdResearchContainer extends VidaWidget {
    FloatRange alpha = new FloatRange(0, 0, 0.5f);
    final Slot sherdSlot;
    List<Vector2i> randomPoint = new ArrayList<>();
    ItemStack lastSherdItemStack = ItemStack.EMPTY;
    int offsetX = 180;
    int offsetY = 180;

    public SherdResearchContainer(int x, int y, int width, int height, Component component, Slot sherdSlot) {
        super(x, y, width, height, component);
        this.sherdSlot = sherdSlot;
        this.tick();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics, mouseX, mouseY, partialTicks);
        //graphics.fill(getX(), getY(), getX() + getWidth(), getHeight() + getY(), 0xffffffff);
        renderRandomPoint(graphics);
    }

    protected void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        this.alpha.change(isHovered, 0.005f);
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();

        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer buffer = graphics.bufferSource().getBuffer(RenderType.gui());

        float alpha = this.alpha.get();
        ARGBColor fromColor = ARGBColor.of(alpha, 0.3f, 0.3f, 0.3f);
        ARGBColor toColor = ARGBColor.of(alpha, 0.4f, 0.4f, 0.4f);

        graphics.fillGradient(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 10, fromColor.argb(), toColor.argb());

        poseStack.popPose();
    }

    protected void renderRandomPoint(GuiGraphics graphics){
        RenderSystem.enableBlend();
        VidaGuiHelper.renderScissor(getX(), getY(), getWidth(), getHeight());
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(this.getX() + this.width / 2, this.getY() + this.height / 2, 0);

        poseStack.pushPose();
        poseStack.translate(-this.offsetX, -this.offsetY, 0);
        Vector2i positionResearch = new Vector2i(this.offsetX, this.offsetY);
        float deltaAlpha = (float) VidaMathHelper.getCyclicalityNumber((double) ClientTickHandler.ticks, 150) * 0.3F;
        RenderSystem.setShaderColor(1, 1, 1, 1);
        for (Vector2i pos : this.randomPoint) {
            double distance = pos.distance(positionResearch);
            if(distance > 60) {
                continue;
            }
            float alpha = (float) ((distance - 50) / 50f) + deltaAlpha;
            VidaGuiHelper.fillGradient(graphics, pos.x + 0.3f, pos.y + 0.3f, pos.x + 1.3f, pos.y  + 1.3f, 1000, ARGBColor.of(alpha, 1f,0.4f,1f).argb(), ARGBColor.of(alpha, 1f,0.4f,1f).argb());
        }
        graphics.flush();

        poseStack.popPose();
        RenderSystem.disableScissor();
        poseStack.popPose();
    }


    @Override
    protected void onDrag(double p_93636_, double p_93637_, double dragX, double dragY) {
        this.offsetX -= dragX;
        this.offsetY -= dragY;
        if(this.offsetX < 0){
            this.offsetX = 0;
        }
        if(this.offsetX > 380){
            this.offsetX = 380;
        }
        if(this.offsetY < 0){
            this.offsetY = 0;
        }
        if(this.offsetY > 380){
            this.offsetY = 380;
        }
    }

    public void tick(){
        if(sherdSlot.getItem().isEmpty()){
            this.randomPoint.clear();
            return;
        }
        if(ItemStack.isSameItemSameTags(lastSherdItemStack, sherdSlot.getItem())){
            return;
        }
        lastSherdItemStack = sherdSlot.getItem();
        if(sherdSlot instanceof SherdSlot slot){
            this.randomPoint = slot.getRandomPoints();
        }
    }
}
