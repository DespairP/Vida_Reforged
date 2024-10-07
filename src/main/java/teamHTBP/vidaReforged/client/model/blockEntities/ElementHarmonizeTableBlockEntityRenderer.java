package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.systems.RenderSystem;
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
import teamHTBP.vidaReforged.core.common.vertex.VidaItemRenderer;
import teamHTBP.vidaReforged.server.blockEntities.ElementHarmonizeTableBlockEntity;

public class ElementHarmonizeTableBlockEntityRenderer extends VidaBlockEntityRenderer<ElementHarmonizeTableBlockEntity>{
    final VidaItemRenderer itemRenderer;
    public ElementHarmonizeTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = VidaItemRenderer.ITEM_RENDERER;
    }

    @Override
    public void render(ElementHarmonizeTableBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        ItemStack realStack = blockEntity.getItemWithCopy();
        ItemStack virtualStack = blockEntity.getVirtualItem();
        if(realStack.isEmpty() && virtualStack.isEmpty()){
            return;
        }
        BakedModel bakedmodel = this.itemRenderer.getModel(realStack.isEmpty() ? virtualStack : realStack, blockEntity.getLevel(), (LivingEntity) null, 0);
        float floating = Mth.sin(((float) ClientTickHandler.ticks + partialTicks) / 10.0F) * 0.1F + 0.1F;
        float scale = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
        float alpha = realStack.isEmpty() ? 0.3f : 1f;
        RenderSystem.enableBlend();
        poseStack.pushPose();
        poseStack.translate(0.5F, floating + 0.25F * scale + 1.3F, 0.5F);
        poseStack.mulPose(Axis.YP.rotation(blockEntity.spin));
        itemRenderer.render(realStack.isEmpty() ? virtualStack : realStack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, combinedLightIn, OverlayTexture.NO_OVERLAY, bakedmodel, alpha);
        RenderSystem.setShaderColor(1,1,1, 1);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }
}
