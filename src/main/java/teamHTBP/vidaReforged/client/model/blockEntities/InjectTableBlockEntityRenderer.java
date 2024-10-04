package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import org.lwjgl.opengl.GL11;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.events.ShadersHandler;
import teamHTBP.vidaReforged.client.events.registries.LayerRegistryHandler;
import teamHTBP.vidaReforged.client.model.blockModel.InjectTableModel;
import teamHTBP.vidaReforged.helper.VidaRenderHelper;
import teamHTBP.vidaReforged.server.blockEntities.InjectTableBlockEntity;

public class InjectTableBlockEntityRenderer implements BlockEntityRenderer<InjectTableBlockEntity> {
    private InjectTableModel model;
    public static final ResourceLocation TEXTURE = new ResourceLocation(VidaReforged.MOD_ID, "textures/block/blockmodel/inject_table_block.png");
    private ItemRenderer itemRenderer;
    private final BufferBuilder bufferBuilder = new BufferBuilder(256);

    public InjectTableBlockEntityRenderer(BlockEntityRendererProvider.Context context){
        itemRenderer = context.getItemRenderer();
    }

    public InjectTableModel getOrCreateModel(){
        if(model == null){
            model = LayerRegistryHandler.getModelSupplier(InjectTableModel.APPRENTICE_LAYER_LOCATION, InjectTableModel.class).get();
        }
        return model;
    }
    @Override
    public void render(InjectTableBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int lightOverlayIn, int packetOverlayIn) {
        long time = System.currentTimeMillis();
        renderCube(poseStack, bufferSource, lightOverlayIn, packetOverlayIn, time);
        renderItem(poseStack, blockEntity.getItem(), blockEntity.getLevel(), bufferSource, lightOverlayIn, packetOverlayIn, time);
    }

    public void renderCube(PoseStack poseStack, MultiBufferSource bufferSource, int lightOverlayIn, int packetOverlayIn, long time){
        InjectTableModel cubeModel = getOrCreateModel();
        if(cubeModel == null) {
            return;
        }
        float angle = (time / 30) % 360;
        poseStack.pushPose();
        poseStack.translate(0.5f,-0.2f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        cubeModel.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE)), lightOverlayIn, packetOverlayIn,1, 1, 1, 1);
        poseStack.popPose();

    }

    public void renderItem(PoseStack poseStack, ItemStack stack, Level level, MultiBufferSource bufferSource, int lightOverlayIn, int packetOverlayIn, long time){
        if (stack.isEmpty()){
            return;
        }
        Window window = Minecraft.getInstance().getWindow();
        ShadersHandler.spidew_bloom.resize(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height);

        // 渲染物品
        BakedModel ibakedmodel = itemRenderer.getModel(stack, level, null, 0);
        double floatingYOffset = 0.12F * Math.sin(sinWave(time));
        poseStack.translate(0.5F, 1.8F + floatingYOffset, 0.5F);
        // 武器展示角度
        if(stack.is(itemHolder -> itemHolder.get() instanceof SwordItem)){
            poseStack.mulPose(Axis.XN.rotationDegrees(180));
        } else {
            poseStack.mulPose(Axis.XN.rotationDegrees(0));
        }
        poseStack.mulPose(Axis.ZN.rotationDegrees(45));
        itemRenderer.render(stack, ItemDisplayContext.FIXED, true, poseStack, MultiBufferSource.immediate(bufferBuilder), 240, packetOverlayIn, ibakedmodel);

        RenderTarget rendertarget = ShadersHandler.spidew_bloom.getTempTarget("final");
        rendertarget.clear(Minecraft.ON_OSX);
        rendertarget.bindWrite(false);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BufferUploader.drawWithShader(this.bufferBuilder.end());
        ShadersHandler.spidew_bloom.process(Minecraft.getInstance().getPartialTick());


        // Main
        RenderTarget mainRenderTarget = Minecraft.getInstance().getMainRenderTarget();
        mainRenderTarget.bindWrite(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ZERO,
                GlStateManager.DestFactor.ONE
        );
        rendertarget.blitToScreen(window.getWidth(), window.getHeight(), false);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1,1);
        RenderSystem.resetTextureMatrix();
        RenderSystem.depthMask(true);

    }

    @Override
    public boolean shouldRenderOffScreen(InjectTableBlockEntity p_112306_) {
        return true;
    }

    private static double sinWave(float ticks) {
        return (ticks * 0.1) % (Math.PI * 2);
    }
}
