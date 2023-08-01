package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import org.joml.Quaternionf;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.events.LayerRegistryHandler;
import teamHTBP.vidaReforged.client.model.blockModel.InjectTableModel;
import teamHTBP.vidaReforged.server.blockEntities.GlowingLightBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.InjectTableBlockEntity;

public class InjectTableBlockEntityRenderer implements BlockEntityRenderer<InjectTableBlockEntity> {
    InjectTableModel model;
    private BlockEntityRendererProvider.Context context;
    public static final ResourceLocation TEXTURE = new ResourceLocation(VidaReforged.MOD_ID, "textures/block/blockmodel/inject_table_block.png");


    public InjectTableBlockEntityRenderer(BlockEntityRendererProvider.Context context){
        this.context = context;
    }

    public InjectTableModel getOrCreateModel(){
        if(model == null){
            model = LayerRegistryHandler.getModelSupplier(InjectTableModel.APPRENTICE_LAYER_LOCATION, InjectTableModel.class).get();
        }
        return model;
    }

    @Override
    public void render(InjectTableBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int lightOverlayIn, int packetOverlayIn) {
        InjectTableModel cubeModel = getOrCreateModel();
        long time = System.currentTimeMillis();
        float angle = (time / 30) % 360;
        if(cubeModel != null){
            poseStack.pushPose();
            poseStack.translate(0.5f,-0.2f, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(angle));
            cubeModel.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE)), lightOverlayIn,packetOverlayIn,1, 1, 1, 1);
            poseStack.popPose();
        }

        if(!blockEntity.hasItem()){
            return;
        }
        poseStack.pushPose();
        double floating = 0.12 * Math.sin(sinWave(blockEntity.time));
        poseStack.translate(0.5f, 1.8f + floating, 0.5f);
        ItemStack stack = blockEntity.getItemForDisplay();

        //选择角度
        if(stack.is(itemHolder -> itemHolder.get() instanceof SwordItem)){
            poseStack.mulPose(Axis.XN.rotationDegrees(180));
        }else {
            poseStack.mulPose(Axis.XN.rotationDegrees(0));
        }

        poseStack.mulPose(Axis.ZN.rotationDegrees(45));
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel ibakedmodel = itemRenderer.getModel(stack, blockEntity.getLevel(), null, 0);
        itemRenderer.render(blockEntity.getItemForDisplay(), ItemDisplayContext.FIXED, true, poseStack, bufferSource, 240, packetOverlayIn, ibakedmodel);
        poseStack.popPose();
    }

    double sinWave(float ticks) {
        return (ticks * 0.1) % (Math.PI * 2);
    }
}
