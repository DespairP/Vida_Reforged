package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import teamHTBP.vidaReforged.server.blockEntities.VaseBlockEntity;
import teamHTBP.vidaReforged.server.blocks.VaseBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class VaseBlockEntityRenderer extends VidaBlockEntityRenderer<VaseBlockEntity>{
    BlockRenderDispatcher blockRenderer;
    List<Consumer<PoseStack>> poseStackRunnable = List.of(
            VaseBlockEntityRenderer::random_1, VaseBlockEntityRenderer::random_2, VaseBlockEntityRenderer::random_3, VaseBlockEntityRenderer::random_4, VaseBlockEntityRenderer::random_5
    );

    public VaseBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(VaseBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        SimpleContainer container = blockEntity.getFlowers();
        if(container.isEmpty()){
            return;
        }
        List<Integer> placeRandom = blockEntity.getPlaceRandom();
        renderItem(container.getItem(0), partialTicks, poseStack, bufferSource,combinedLightIn, combinedOverlayIn, poseStackRunnable.get(placeRandom.get(0)));
        renderItem(container.getItem(1), partialTicks, poseStack, bufferSource,combinedLightIn, combinedOverlayIn, poseStackRunnable.get(placeRandom.get(1)));
        renderItem(container.getItem(2), partialTicks, poseStack, bufferSource,combinedLightIn, combinedOverlayIn, poseStackRunnable.get(placeRandom.get(2)));

    }

    public void renderItem(ItemStack item, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn, Consumer<PoseStack> consumer){
        if(item.isEmpty()){
            return;
        }
        poseStack.pushPose();
        List<BlockState> state = new ArrayList<>();
        if(item.getItem() instanceof BlockItem blockItem){
            Block block = blockItem.getBlock();
            state.addAll(block.getStateDefinition().getPossibleStates());
        }
        if(state.size() == 0){
            return;
        }
        consumer.accept(poseStack);
        blockRenderer.renderSingleBlock(state.get(0), poseStack, bufferSource, combinedLightIn, combinedOverlayIn, ModelData.EMPTY, RenderType.cutout());
        poseStack.popPose();
    }


    public static void random_1(PoseStack poseStack){
        poseStack.translate(0.5F,0.5F,0.5F);
        poseStack.scale(0.7F,0.7F, 0.7f);
        poseStack.translate(-0.5F,-0.5F,-0.5F);

        poseStack.translate(0.5F,0.5F,0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(12F));
        poseStack.mulPose(Axis.XP.rotationDegrees(-12F));

        poseStack.translate(-0.5F,-0.5F,-0.5F);

        poseStack.translate(0F,0.7F,0.1F);
    }


    public static void random_2(PoseStack poseStack){
        poseStack.translate(0.5F,0.5F,0.5F);
        poseStack.scale(0.7F,0.7F, 0.7f);
        poseStack.translate(-0.5F,-0.5F,-0.5F);

        poseStack.translate(0.5F,0.5F,0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-9F));
        poseStack.mulPose(Axis.XP.rotationDegrees(5F));

        poseStack.translate(-0.5F,-0.5F,-0.5F);

        poseStack.translate(0F,0.8F,0.1F);
    }

    public static void random_3(PoseStack poseStack){
        poseStack.translate(0.5F,0.5F,0.5F);
        poseStack.scale(0.7F,0.7F, 0.7f);
        poseStack.translate(-0.5F,-0.5F,-0.5F);

        poseStack.translate(0.5F,0.5F,0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(1F));
        poseStack.mulPose(Axis.XP.rotationDegrees(-4F));

        poseStack.translate(-0.5F,-0.5F,-0.5F);

        poseStack.translate(-0.1F,0.6F,-0.1F);
    }

    public static void random_4(PoseStack poseStack){
        poseStack.translate(0.5F,0.5F,0.5F);
        poseStack.scale(0.7F,0.7F, 0.7f);
        poseStack.translate(-0.5F,-0.5F,-0.5F);

        poseStack.translate(0.5F,0.5F,0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(14F));
        poseStack.mulPose(Axis.XP.rotationDegrees(-7F));
        poseStack.mulPose(Axis.YP.rotationDegrees(7F));

        poseStack.translate(-0.5F,-0.5F,-0.5F);

        poseStack.translate(0.2F,0.8F,-0.1F);
    }

    public static void random_5(PoseStack poseStack){
        poseStack.translate(0.5F,0.5F,0.5F);
        poseStack.scale(0.7F,0.7F, 0.7f);
        poseStack.translate(-0.5F,-0.5F,-0.5F);

        poseStack.translate(0.5F,0.5F,0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(2F));
        poseStack.mulPose(Axis.XP.rotationDegrees(-12F));

        poseStack.translate(-0.5F,-0.5F,-0.5F);

        poseStack.translate(0F,0.9F,0.1F);
    }
}
