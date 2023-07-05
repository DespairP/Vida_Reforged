package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import teamHTBP.vidaReforged.core.utils.animation.Animator;
import teamHTBP.vidaReforged.core.utils.animation.DestinationAnimator;
import teamHTBP.vidaReforged.core.utils.animation.TimeInterpolator;
import teamHTBP.vidaReforged.core.utils.animation.calculator.IValueProvider;
import teamHTBP.vidaReforged.server.blockEntities.BasePurificationCauldronBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.CollectorBlockEntity;

public class CollectorBlockEntityRenderer implements BlockEntityRenderer<CollectorBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    public CollectorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }
    @Override
    public void render(CollectorBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        renderItem(blockEntity, partialTicks, poseStack, bufferSource, combinedLightIn, combinedOverlayIn);
    }

    private void renderItem(CollectorBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn){
        if(blockEntity.collectItem.isEmpty()){
            return;
        }
        //获取renderer
        final BlockEntityRenderDispatcher dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        final ItemStack lastItem = blockEntity.collectItem;

        poseStack.pushPose();

        poseStack.translate(0.5f, 1.2f, 0.55f);
        poseStack.scale(0.6f,0.6f,0.6f);

        // 使得物品对着玩家视角旋转
        Quaternionf quaternion = dispatcher.camera.rotation();
        float rotationF = Mth.lerp(partialTicks, 0, 0);
        quaternion.mul(Axis.XP.rotation(rotationF));

        poseStack.mulPose(quaternion);

        BakedModel ibakedmodel = itemRenderer.getModel(lastItem, blockEntity.getLevel(), null, 0);
        itemRenderer.render(lastItem, ItemDisplayContext.FIXED, true, poseStack, bufferSource, combinedLightIn, combinedOverlayIn, ibakedmodel);

        poseStack.popPose();
    }
}
