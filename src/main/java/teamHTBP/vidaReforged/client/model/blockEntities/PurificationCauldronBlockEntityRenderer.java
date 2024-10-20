package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.utils.animation.Animator;
import teamHTBP.vidaReforged.core.utils.animation.DestinationAnimator;
import teamHTBP.vidaReforged.core.utils.animation.TimeInterpolator;
import teamHTBP.vidaReforged.core.utils.animation.calculator.IValueProvider;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.server.blockEntities.BasePurificationCauldronBlockEntity;

public class PurificationCauldronBlockEntityRenderer implements BlockEntityRenderer<BasePurificationCauldronBlockEntity> {
    private final static float MAX_WATER_LEVEL = 0.3F;
    private ResourceLocation fluidLocation;
    private int fluidColor;
    private final BlockEntityRendererProvider.Context context;

    private final DestinationAnimator<Float> floatingLevelAnimator;

    public PurificationCauldronBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.floatingLevelAnimator = new DestinationAnimator
                .Builder<Float>()
                .init(0.1f)
                .fromValue(0.1f)
                .toValue(0.2f)
                .mode(Animator.INFINITE)
                .interpolator(TimeInterpolator.SINE)
                .maxTick(400)
                .provider(IValueProvider.FLOAT_VALUE_PROVIDER)
                .build();
        this.floatingLevelAnimator.start();
    }

    @Override
    public void render(BasePurificationCauldronBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        this.floatingLevelAnimator.tick(Minecraft.getInstance().getDeltaFrameTime() * 3f);
        this.renderWaterLayer(entity, partialTicks, poseStack, bufferSource);
        this.renderItem(entity, partialTicks, poseStack, bufferSource, combinedLightIn, combinedOverlayIn);

    }

    /**渲染水图层*/
    protected void renderWaterLayer(BasePurificationCauldronBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource){
        if(!entity.isWaterFilled()){
            return;
        }
        //
        final float percent = (entity.totalProgress / entity.getMaxMainTaskProgress());
        final float waterLevel = MAX_WATER_LEVEL * percent;

        poseStack.pushPose();
        //获取水的贴图
        Fluid fluid = Fluids.WATER.getSource();
        this.initWaterAttribute(fluid,entity);

        //绑定贴图
        TextureAtlasSprite textureAtlasSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(this.fluidLocation);

        //获取水贴图在精灵图中的最大/小u,v位置
        float uMin = textureAtlasSprite.getU0();
        float uMax = textureAtlasSprite.getU1();
        float vMin = textureAtlasSprite.getV0();
        float vMax = textureAtlasSprite.getV1();

        //设置基础argb
        float a = 0.9F;
        ARGBColor argbColor = getColor(fluidColor, entity.mainElement, Math.min(percent + percent * 0.5f, 1));
        float r = argbColor.r() / 255.0F;
        float g = argbColor.g() / 255.0F;
        float b = argbColor.b() / 255.0F;


        final Matrix4f matrix4f = poseStack.last().pose();
        final VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucent());
        poseStack.translate(0, 0.3F + waterLevel, 0);

        //绘制
        buffer.vertex(matrix4f, 0.1F, 0.1F, 0.1F).color(r, g, b, a).uv(uMin, vMin).overlayCoords(0, 0).uv2(15728880).normal(0, 1, 0).endVertex();
        buffer.vertex(matrix4f, 0.1F, 0.1F, 0.9F).color(r, g, b, a).uv(uMin, vMax).overlayCoords(0, 0).uv2(15728880).normal(0, 1, 0).endVertex();
        buffer.vertex(matrix4f, 0.9F, 0.1F, 0.9F).color(r, g, b, a).uv(uMax, vMax).overlayCoords(0, 0).uv2(15728880).normal(0, 1, 0).endVertex();
        buffer.vertex(matrix4f, 0.9F, 0.1F, 0.1F).color(r, g, b, a).uv(uMax, vMin).overlayCoords(0, 0).uv2(15728880).normal(0, 1, 0).endVertex();


        Minecraft.getInstance().getProfiler().pop();
        poseStack.popPose();
    }

    private void renderItem(BasePurificationCauldronBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn){
        if(blockEntity.purificationItems.size() <= 0 || !blockEntity.isInProgress()){
            return;
        }
        //获取renderer
        final BlockEntityRenderDispatcher dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        final ItemStack lastItem = blockEntity.purificationItems.get(0);

        poseStack.pushPose();

        poseStack.translate(0.5f, 1.3f + floatingLevelAnimator.getValue(), 0.55f);
        poseStack.scale(0.6f, 0.6f, 0.6f);

        // 使得物品对着玩家视角旋转
        Quaternionf quaternion = dispatcher.camera.rotation();
        float rotationF = Mth.lerp(partialTicks, 0, 0);
        quaternion.mul(Axis.XP.rotation(rotationF));

        poseStack.mulPose(quaternion);

        BakedModel ibakedmodel = itemRenderer.getModel(lastItem, blockEntity.getLevel(), null, 0);
        itemRenderer.render(lastItem, ItemDisplayContext.FIXED, true, poseStack, bufferSource, combinedLightIn, combinedOverlayIn, ibakedmodel);

        poseStack.popPose();
    }



    /**获取液体颜色*/
    private static int getFluidColor(Level world, BlockPos pos, Fluid fluid) {
        if (fluid.isSame(Fluids.WATER)) {
            return BiomeColors.getAverageWaterColor(world, pos);
        }

        return IClientFluidTypeExtensions.of(fluid).getTintColor();
    }

    /**初始化液体参数*/
    private void initWaterAttribute(Fluid fluid, BlockEntity blockEntity){
        // 获取静态/流动的液体贴图
        if (IClientFluidTypeExtensions.of(fluid).getStillTexture() != null) {
            this.fluidLocation = IClientFluidTypeExtensions.of(fluid).getStillTexture();
            this.fluidColor = getFluidColor(blockEntity.getLevel(), blockEntity.getBlockPos(), fluid);
        } else if (IClientFluidTypeExtensions.of(fluid).getFlowingTexture() != null) {
            this.fluidLocation = IClientFluidTypeExtensions.of(fluid).getFlowingTexture();
            this.fluidColor = getFluidColor(blockEntity.getLevel(), blockEntity.getBlockPos(), fluid);
        } else { // In case that no texture exist
            this.fluidLocation = IClientFluidTypeExtensions.of(fluid).getStillTexture();
            this.fluidColor = getFluidColor(blockEntity.getLevel(), blockEntity.getBlockPos(), Fluids.WATER);
        }
    }


    private ARGBColor getColor(int originalColor, VidaElement element, float percent){
        // from Color
        ARGBColor fromColor = new ARGBColor(
                1,
                (originalColor >> 16 & 0xFF),
                (originalColor >> 8 & 0xFF),
                (originalColor & 0xFF)
        );
        // to Color
        ARGBColor toColor = element.baseColor.toARGB();

        return new ARGBColor(
                1,
                (int)(fromColor.r() + (toColor.r() - fromColor.r()) * percent),
                (int)(fromColor.g() + (toColor.g() - fromColor.g()) * percent),
                (int)(fromColor.b() + (toColor.b() - fromColor.b()) * percent)
        );
    }


}
