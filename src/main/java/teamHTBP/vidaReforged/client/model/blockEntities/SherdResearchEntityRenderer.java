package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import teamHTBP.vidaReforged.server.blockEntities.SherdResearchTableBlockEntity;

public class SherdResearchEntityRenderer extends VidaBlockEntityRenderer<SherdResearchTableBlockEntity>{
    public SherdResearchEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SherdResearchTableBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        ItemStack stack = new ItemStack(Items.ARCHER_POTTERY_SHERD, 1);
        // 第一块陶片
        poseStack.pushPose();
        poseStack.translate(0.5f,0.5f,0.5f);
        poseStack.scale(0.5f,0.5f, 0.5f);
        poseStack.translate(-0.5f, -0.5f, -0.5f);
        poseStack.translate(0.3, 1.3, 0.9);
        poseStack.pushPose();
        poseStack.mulPose(Axis.XN.rotationDegrees(79));
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel ibakedmodel = itemRenderer.getModel(stack, blockEntity.getLevel(), null, 0);
        itemRenderer.render(stack, ItemDisplayContext.FIXED, true, poseStack, bufferSource, combinedLightIn, combinedOverlayIn, ibakedmodel);
        poseStack.popPose();
        poseStack.popPose();
        // 第二块陶片
        poseStack.pushPose();
        poseStack.translate(0.5f,0.5f,0.5f);
        poseStack.scale(0.5f,0.5f, 0.5f);
        poseStack.translate(-0.5f, -0.5f, -0.5f);
        poseStack.translate(0.2, 1.2, 0.2);
        poseStack.pushPose();
        poseStack.mulPose(Axis.XN.rotationDegrees(90));
        poseStack.mulPose(Axis.ZN.rotationDegrees(67));
        itemRenderer.render(stack, ItemDisplayContext.FIXED, true, poseStack, bufferSource, combinedLightIn, combinedOverlayIn, ibakedmodel);
        poseStack.popPose();
        poseStack.popPose();
        // 第三块陶片
        poseStack.pushPose();
        poseStack.translate(0.5f,0.5f,0.5f);
        poseStack.scale(0.5f,0.5f, 0.5f);
        poseStack.translate(-0.5f, -0.5f, -0.5f);
        poseStack.translate(0.7, 1.2, 0.5);
        poseStack.pushPose();
        poseStack.mulPose(Axis.XN.rotationDegrees(-80));
        poseStack.mulPose(Axis.ZN.rotationDegrees(67));
        itemRenderer.render(stack, ItemDisplayContext.FIXED, true, poseStack, bufferSource, combinedLightIn, combinedOverlayIn, ibakedmodel);
        poseStack.popPose();
        poseStack.popPose();
    }
}
