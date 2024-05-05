package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.client.model.itemModel.VidaWandModel;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.helper.VidaModelHelper;
import teamHTBP.vidaReforged.server.blockEntities.TeaconGuideBookBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaWandCraftingTableBlockEntity;

public class VidaWandCraftingTableBlockEntityRenderer extends VidaBlockEntityRenderer<VidaWandCraftingTableBlockEntity> {
    private FloatRange rotation = new FloatRange(0, 0, 360);
    private VidaWandModel model;

    public VidaWandCraftingTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.model = VidaModelHelper.getModel(VidaWandModel.LAYER_LOCATION, VidaWandModel.class);
    }


    @Override
    public void render(VidaWandCraftingTableBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        poseStack.translate(0.5, 2.5, 0.5);
        renderWandModel(poseStack, bufferSource, combinedLightIn, combinedOverlayIn, partialTicks);

        poseStack.popPose();
    }


    public void renderWandModel(PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn, float partialTicks){
        float rot = rotation.increase(mc.getDeltaFrameTime() * 0.8f);
        rotation.set(rot >= 360.0f ? rot % 360.0f : rot);
        poseStack.pushPose();
        poseStack.translate(0, -0.2, 0);
        poseStack.translate(0, Math.sin(ClientTickHandler.ticks * 0.01f) * 0.1F,  0);
        poseStack.scale(0.8f, 0.8f, 0.8f);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation.get()));
        poseStack.mulPose(Axis.XN.rotationDegrees(180));


        model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucentCull(VidaWandModel.DEFAULT_TEXTURE)), combinedLightIn, combinedOverlayIn, 1f, 1f, 1f, 0.5f);

        poseStack.popPose();

    }


}
