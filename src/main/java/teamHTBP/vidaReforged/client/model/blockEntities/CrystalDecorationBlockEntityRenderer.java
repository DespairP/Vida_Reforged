package teamHTBP.vidaReforged.client.model.blockEntities;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.RenderTypeHandler;
import teamHTBP.vidaReforged.core.utils.animation.Animator;
import teamHTBP.vidaReforged.core.utils.animation.DestinationAnimator;
import teamHTBP.vidaReforged.core.utils.animation.TimeInterpolator;
import teamHTBP.vidaReforged.core.utils.animation.calculator.IValueProvider;
import teamHTBP.vidaReforged.server.blockEntities.CrystalDecorationBlockEntity;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import java.util.Map;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class CrystalDecorationBlockEntityRenderer implements BlockEntityRenderer<CrystalDecorationBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    private final DestinationAnimator<Float> floatingLevelAnimator;

    public CrystalDecorationBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
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
    public boolean shouldRenderOffScreen(CrystalDecorationBlockEntity entity) {
        return true;
    }

    @Override
    public void render(CrystalDecorationBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int lightIn, int overlayIn) {
        if(!entity.hasItem()){
            return;
        }
        return;
        /*
        ResourceLocation iconLocation = icon(entity.getItemWithoutClear().getItem());

        float uMin = 0;
        float uMax = 1;
        float vMin = 0;
        float vMax = 1;

        if(iconLocation == null){
            return;
        }

        if(iconLocation == null){
            return;
        }
        // 渲染中间的宝石块
        poseStack.pushPose();
        RenderSystem.enableBlend();

        RenderType renderType = RenderTypeHandler.IMAGE.apply(iconLocation);

        VertexConsumer builder = bufferSource.getBuffer(renderType);
        poseStack.translate(0.5f, 1.2F, 0.5f);
        poseStack.mulPose(this.context.getEntityRenderer().cameraOrientation());
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(-0.5f, -0.5, -0.5f);

        Matrix4f matrixStack = poseStack.last().pose();
        float color = 1;

        builder.vertex(matrixStack, 0, 1, 0.5F).color(color, color, color, 1.0f).uv(uMin, vMin).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 0.5F).color(color, color, color, 1.0f).uv(uMax, vMin).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 0.5F).color(color, color, color, 1.0f).uv(uMax, vMax).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 0, 0.5F).color(color, color, color, 1.0f).uv(uMin, vMax).uv2(240, 240).normal(1, 0, 0).endVertex();

        RenderSystem.disableBlend();

        ((MultiBufferSource.BufferSource)bufferSource).endBatch(renderType);
        poseStack.popPose();
        */

    }

    public ResourceLocation icon(Item item){
         final Map<Item, ResourceLocation> iconMap = ImmutableMap.of(
                 VidaItemLoader.GOLD_GEM.get(), new ResourceLocation(MOD_ID, String.format("textures/icons/%slogo.png", "gold")),
                 VidaItemLoader.WOOD_GEM.get(), new ResourceLocation(MOD_ID, String.format("textures/icons/%slogo.png", "wood")),
                 VidaItemLoader.AQUA_GEM.get(), new ResourceLocation(MOD_ID, String.format("textures/icons/%slogo.png", "aqua")),
                 VidaItemLoader.FIRE_GEM.get(), new ResourceLocation(MOD_ID, String.format("textures/icons/%slogo.png", "fire")),
                 VidaItemLoader.EARTH_GEM.get(), new ResourceLocation(MOD_ID, String.format("textures/icons/%slogo.png", "earth"))
        );
         return iconMap.getOrDefault(item, new ResourceLocation(MOD_ID, String.format("textures/icons/%slogo.png", "gold")) );
    }


}
