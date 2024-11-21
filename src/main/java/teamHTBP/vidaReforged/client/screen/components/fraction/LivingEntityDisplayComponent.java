package teamHTBP.vidaReforged.client.screen.components.fraction;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;

public class LivingEntityDisplayComponent extends VidaWidget {
    LivingEntity entity;
    public static final int DEFAULT_WIDTH = 49;
    public static final int DEFAULT_HEIGHT = 70;
    public SecondOrderDynamics scale = new SecondOrderDynamics(1, 1, 0.5f, new Vector3f(0));

    public LivingEntityDisplayComponent(int x, int y, int width, int height, ResourceLocation id) {
        super(x, y, width, height, Component.literal(""), id);
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //graphics.fill(getX(), getY(), getX() + width, getY() + height, 0xffffffff);
        if(entity != null) {
            double scales = mc.getWindow().getGuiScaledHeight() * 0.3F / getHeight();
            Vector3f currentScale = scale.update(partialTicks * 0.05f, new Vector3f((float) scales), null);
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            poseStack.translate((getX() + getWidth() / 2) , (getY() + getHeight() / 2) , 80F);
            poseStack.scale((float) (currentScale.x), (float) (currentScale.y), (float) (currentScale.z));
            poseStack.translate(-(getX() + getWidth() / 2), -(getY() + getHeight() / 2), 0);
            InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, getX() + getWidth() / 2, getY() + getHeight() - 10,30, getX() - mouseX + 30, getY() - mouseY, entity);
            poseStack.popPose();
        }
    }
}
