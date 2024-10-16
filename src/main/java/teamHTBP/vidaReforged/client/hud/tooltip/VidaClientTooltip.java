package teamHTBP.vidaReforged.client.hud.tooltip;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.core.common.vertex.VidaItemRenderer;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.server.components.VidaTooltipComponent;

import java.awt.*;

public class VidaClientTooltip implements ClientTooltipComponent {
    private VidaTooltipComponent component;
    private final VidaItemRenderer itemRenderer;
    private final Level level;
    private final int packedLight = 14 << 4 | 14 << 20;
    private SecondOrderDynamics scale = new SecondOrderDynamics(1f, 0.5f, 0.5f, new Vector3f());
    private FloatRange alpha = new FloatRange(0, 1, 1);
    private Minecraft mc;
    private float frames = 0;

    public VidaClientTooltip(VidaTooltipComponent component){
        this.component = component;
        this.mc = Minecraft.getInstance();
        this.itemRenderer = VidaItemRenderer.ITEM_RENDERER;
        this.level = Minecraft.getInstance().level;
    }

    public void setComponent(VidaTooltipComponent component){
        if(this.component != null && !this.component.getItem().equals(component.getItem(), false)){
            scale = new SecondOrderDynamics(1f, 0.5f, 0.5f, new Vector3f());
            alpha.set(0);
            frames = 0;
            this.component = component;
        }
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
        Component countComponent = Component.literal(String.valueOf(component.getItem().getCount() == 1 ? "" : component.getItem().getCount())).withStyle(Style.EMPTY.withColor(ChatFormatting.BLUE));
        matrix4f.translate(0, 0, 1000);
        font.drawInBatch(countComponent, x + 14, y, 150, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        frames = (frames + mc.getDeltaFrameTime() * 3F) % 360;
        float itemAlpha = alpha.increase(mc.getDeltaFrameTime() * 0.02f);

        BakedModel bakedmodel = this.itemRenderer.getModel(component.getItem(), level, (LivingEntity) null, 0);

        poseStack.translate((float)(x + 8), (float)(y - 2), (float)(150));
        poseStack.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        Vector3f scale = this.scale.update(mc.getDeltaFrameTime() * 0.2F, new Vector3f(16), null);
        poseStack.scale(scale.x, scale.y, scale.z);

        if(bakedmodel.isGui3d() && component.getItem().getItem() instanceof BlockItem){
            poseStack.mulPose(Axis.XP.rotationDegrees(30));
            poseStack.mulPose(Axis.YP.rotationDegrees(frames));
            poseStack.mulPose(Axis.XP.rotationDegrees(-30));
        }else{
            poseStack.mulPose(Axis.YN.rotationDegrees(frames));
        }

        this.itemRenderer.render(component.getItem(), ItemDisplayContext.GUI, false, poseStack, graphics.bufferSource(), packedLight, OverlayTexture.NO_OVERLAY, bakedmodel, itemAlpha);
        poseStack.popPose();

        graphics.flush();
    }

    @Override
    public int getHeight() {
        return 9;
    }

    @Override
    public int getWidth(Font font) {
        return font.width(component.getItem().getDisplayName()) + 8;
    }
}
