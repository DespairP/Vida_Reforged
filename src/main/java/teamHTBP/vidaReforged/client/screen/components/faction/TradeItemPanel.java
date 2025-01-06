package teamHTBP.vidaReforged.client.screen.components.faction;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.renderer.RenderTypeHandler;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;

/**/
public class TradeItemPanel extends VidaWidget {
    private static final ResourceLocation PANEL = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/slot_panel01.png");
    private static final Vector3f SELECTED_ALPHA = new Vector3f(1);
    private static final Vector3f DEFAULT_ALPHA = new Vector3f(0.3f);
    private final ItemRenderer itemRenderer;
    private SecondOrderDynamics alpha = new SecondOrderDynamics(1, 1, 0.8F, new Vector3f());
    private ItemStack seletedItemStack = ItemStack.EMPTY;
    private TextureSection panelTexture;
    private boolean isSelected = false;
    private float frames = 0;
    private int size = 16;


    public TradeItemPanel(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component, new ResourceLocation(VidaReforged.MOD_ID, "trade_item_section"));
        panelTexture = new TextureSection(PANEL, 0, 0, width, height, width, height);
        itemRenderer = mc.getItemRenderer();
    }

    @Override
    protected void onResized(int width, int height) {
        panelTexture = new TextureSection(PANEL, 0, 0, width, height, width, height);
    }

    protected void renderBackground(GuiGraphics graphics){
        RenderTypeHandler.TRANSLUCENT_TRANSPARENCY_GUI.setupRenderState();
        graphics.setColor(1f, 1f, 1f, alpha.currentPos().x);
        RenderSystem.depthMask(false);
        VidaGuiHelper.blitWithTexture(graphics, getX(), getY(), 100, panelTexture);
        graphics.setColor(1f, 1f, 1f, 1f);
        RenderTypeHandler.TRANSLUCENT_TRANSPARENCY_GUI.clearRenderState();
    }

    protected void renderItemstack(GuiGraphics graphics){
        PoseStack poseStack = graphics.pose();
        BakedModel model = itemRenderer.getModel(seletedItemStack, mc.level, mc.player, 0);
        MultiBufferSource.BufferSource source = graphics.bufferSource();
        VertexConsumer buffer = source.getBuffer(RenderType.translucent());

        seletedItemStack = new ItemStack(Items.GLASS_BOTTLE);
        frames = (frames + mc.getDeltaFrameTime() * 3F) % 360;
        //
        poseStack.pushPose();
        poseStack.translate(getX() + (width - 32) / 2, model.isGui3d() ? size + getY() + 40.0f : getY() + 16.0f + 64.0F, size);

        poseStack.scale(32.0F, 32.0F, 32.0F);
        poseStack.pushPose();
        poseStack.mulPose(model.isGui3d() ? Axis.XN.rotationDegrees(-20) : Axis.XN.rotationDegrees(-180));
        poseStack.translate(0.5, 0.5,0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(frames));
        poseStack.translate(-0.5, -0.5,-0.5);
        itemRenderer.renderModelLists(model, seletedItemStack, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, poseStack, buffer);

        poseStack.popPose();
        poseStack.popPose();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        alpha.update(partialTicks * 0.1f, isHovered ? SELECTED_ALPHA : DEFAULT_ALPHA, null);
        renderBackground(graphics);

        renderItemstack(graphics);
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setSeletedItemStack(ItemStack seletedItemStack) {
        this.seletedItemStack = seletedItemStack;
    }
}
