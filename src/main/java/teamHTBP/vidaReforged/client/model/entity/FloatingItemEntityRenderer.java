package teamHTBP.vidaReforged.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.server.entity.FloatingItemEntity;

public class FloatingItemEntityRenderer extends EntityRenderer<FloatingItemEntity> {
    RandomSource random;
    ItemRenderer itemRenderer;

    public FloatingItemEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        random = RandomSource.create();
        itemRenderer = context.getItemRenderer();
    }


    public void render(FloatingItemEntity entity, float p_115037_, float p_115038_, PoseStack poseStack, MultiBufferSource bufferSource, int lightOverlay) {
        poseStack.pushPose();
        ItemStack itemstack = entity.getItem();
        int i = itemstack.isEmpty() ? 187 : Item.getId(itemstack.getItem()) + itemstack.getDamageValue();
        this.random.setSeed((long) i);
        BakedModel bakedmodel = this.itemRenderer.getModel(itemstack, entity.level(), (LivingEntity) null, entity.getId());
        boolean flag = bakedmodel.isGui3d();
        int itemAmount = 1;
        float f = 0.25F;
        float f1 = Mth.sin(((float) entity.getAge() + p_115038_) / 10.0F + entity.bobOffs) * 0.1F + 0.1F;
        float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
        poseStack.translate(0.0F, f1 + 0.25F * f2, 0.0F);
        float f3 = entity.getSpin(p_115038_);
        poseStack.mulPose(Axis.YP.rotation(f3));
        if (!flag) {
            float f7 = -0.0F * (float) (itemAmount - 1) * 0.5F;
            float f8 = -0.0F * (float) (itemAmount - 1) * 0.5F;
            float f9 = -0.09375F * (float) (itemAmount - 1) * 0.5F;
            poseStack.translate(f7, f8, f9);
        }

        for (int k = 0; k < itemAmount; ++k) {
            poseStack.pushPose();
            if (k > 0) {
                if (flag) {
                    float f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    poseStack.translate(shouldSpreadItems() ? f11 : 0, shouldSpreadItems() ? f13 : 0, shouldSpreadItems() ? f10 : 0);
                } else {
                    float f12 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float f14 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    poseStack.translate(shouldSpreadItems() ? f12 : 0, shouldSpreadItems() ? f14 : 0, 0.0D);
                }
            }

            this.itemRenderer.render(itemstack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, lightOverlay, OverlayTexture.NO_OVERLAY, bakedmodel);
            poseStack.popPose();
            if (!flag) {
                poseStack.translate(0.0, 0.0, 0.09375F);
            }
        }

        poseStack.popPose();
        super.render(entity, p_115037_, p_115038_, poseStack, bufferSource, lightOverlay);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(FloatingItemEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    public boolean shouldSpreadItems() {
        return true;
    }


}
