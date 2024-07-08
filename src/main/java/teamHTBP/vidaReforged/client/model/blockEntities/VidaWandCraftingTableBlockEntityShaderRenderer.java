package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.client.events.ShadersHandler;
import teamHTBP.vidaReforged.client.model.itemModel.VidaWandModel;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.helper.VidaModelHelper;
import teamHTBP.vidaReforged.server.blockEntities.VidaWandCraftingTableBlockEntity;

import static net.minecraft.client.Minecraft.ON_OSX;

public class VidaWandCraftingTableBlockEntityShaderRenderer extends VidaBlockEntityRenderer<VidaWandCraftingTableBlockEntity> {
    private FloatRange rotation = new FloatRange(0, 0, 360);
    private VidaWandModel model;

    public VidaWandCraftingTableBlockEntityShaderRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.model = VidaModelHelper.getModel(VidaWandModel.LAYER_LOCATION, VidaWandModel.class);
    }


    private final BufferBuilder bufferBuilder = new BufferBuilder(256);

    @Override
    public void render(VidaWandCraftingTableBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        RenderSystem.enableBlend();
        int width = mc.getMainRenderTarget().width, height = mc.getMainRenderTarget().height;
        ShadersHandler.spidew_bloom.resize(mc.getMainRenderTarget().width, mc.getMainRenderTarget().height);

        renderWandModel(poseStack, bufferSource, combinedLightIn, combinedOverlayIn, partialTicks);

        RenderTarget rendertarget1 = ShadersHandler.spidew_bloom.getTempTarget("translucent");
        RenderTarget rendertarget2 = ShadersHandler.spidew_bloom.getTempTarget("itemEntity");
        RenderTarget rendertarget3 = ShadersHandler.spidew_bloom.getTempTarget("particles");
        RenderTarget rendertarget4 = ShadersHandler.spidew_bloom.getTempTarget("weather");
        RenderTarget rendertarget = ShadersHandler.spidew_bloom.getTempTarget("clouds");

        rendertarget.clear(ON_OSX);
        rendertarget.bindWrite(false);
        BufferUploader.draw(bufferBuilder.end());
        ShadersHandler.spidew_bloom.process(partialTicks);

        mc.getMainRenderTarget().bindWrite(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        rendertarget.blitToScreen(width, height, false);
        rendertarget1.blitToScreen(width, height, false);
        rendertarget2.blitToScreen(width, height, false);
        rendertarget3.blitToScreen(width, height, false);
        rendertarget4.blitToScreen(width, height, false);
        RenderSystem.disableBlend();

        RenderSystem.resetTextureMatrix();
        RenderSystem.depthMask(/*flag*/true);
    }


    public void renderWandModel(PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn, float partialTicks){
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        float rot = rotation.increase(mc.getDeltaFrameTime() * 0.8f);
        rotation.set(rot >= 360.0f ? rot % 360.0f : rot);

        poseStack.pushPose();
        poseStack.translate(0, 3, 0);
        poseStack.translate(0, Math.sin(ClientTickHandler.ticks * 0.01f) * 0.1F,  0);
        poseStack.scale(0.8f, 0.8f, 0.8f);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation.get()));
        poseStack.mulPose(Axis.XN.rotationDegrees(180));

        model.renderToBuffer(poseStack,  bufferBuilder, combinedLightIn, combinedOverlayIn, 1f, 1f, 1f, 1f);

        poseStack.popPose();
    }


}
