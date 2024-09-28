package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.server.blockEntities.ElementHarmonizeTableBlockEntity;

public class ElementHarmonizeTableBlockEntityRenderer extends VidaBlockEntityRenderer<ElementHarmonizeTableBlockEntity>{
    final ItemRenderer itemRenderer;
    public ElementHarmonizeTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ElementHarmonizeTableBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        ItemStack stack = blockEntity.getItemWithCopy();
        if(stack.isEmpty()){
            return;
        }
        BakedModel bakedmodel = this.itemRenderer.getModel(stack, blockEntity.getLevel(), (LivingEntity) null, 0);
        float floating = Mth.sin(((float) ClientTickHandler.ticks + partialTicks) / 10.0F) * 0.1F + 0.1F;
        float scale = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
        poseStack.pushPose();
        poseStack.translate(0.5F, floating + 0.25F * scale + 1.3F, 0.5F);
        poseStack.mulPose(Axis.YP.rotation(blockEntity.spin));
        itemRenderer.render(stack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, combinedLightIn, OverlayTexture.NO_OVERLAY, bakedmodel);
        poseStack.popPose();
    }
}
