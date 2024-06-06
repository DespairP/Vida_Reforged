package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import teamHTBP.vidaReforged.client.events.ShadersHandler;
import teamHTBP.vidaReforged.core.utils.animation.Animator;
import teamHTBP.vidaReforged.core.utils.animation.DestinationAnimator;
import teamHTBP.vidaReforged.core.utils.animation.TimeInterpolator;
import teamHTBP.vidaReforged.core.utils.animation.calculator.IValueProvider;
import teamHTBP.vidaReforged.server.blockEntities.GlowingLightBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.TeaconGuideBookBlockEntity;

public class TeaconGuideBookBlockRenderer implements BlockEntityRenderer<TeaconGuideBookBlockEntity> {
    public static final Material BOOK_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/enchanting_table_book"));
    private final BookModel bookModel;

    private final DestinationAnimator<Float> floatingLevelAnimator;

    public TeaconGuideBookBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
        this.floatingLevelAnimator = new DestinationAnimator
                .Builder<Float>()
                .init(0.1f)
                .fromValue(0.1f)
                .toValue(0.2f)
                .mode(Animator.INFINITE)
                .interpolator(TimeInterpolator.SINE)
                .maxTick(600)
                .provider(IValueProvider.FLOAT_VALUE_PROVIDER)
                .build();
        this.floatingLevelAnimator.start();
    }
    
    public void render(TeaconGuideBookBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        floatingLevelAnimator.tick(partialTicks);
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.15F + floatingLevelAnimator.getValue(), 0.5F);
        float f = (float)blockEntity.time + partialTicks;
        poseStack.translate(0.0F, 0.1F + Mth.sin(f * 0.1F) * 0.01F, 0.0F);

        float f1;
        for(f1 = blockEntity.rot - blockEntity.oRot; f1 >= (float)Math.PI; f1 -= ((float)Math.PI * 2F)) {
        }

        while(f1 < -(float)Math.PI) {
            f1 += ((float)Math.PI * 2F);
        }

        float f2 = blockEntity.oRot + f1 * partialTicks;
        poseStack.mulPose(Axis.YP.rotation(-f2));
        poseStack.mulPose(Axis.ZP.rotationDegrees(80.0F));
        float f3 = Mth.lerp(partialTicks, blockEntity.oFlip, blockEntity.flip);
        float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
        float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
        float f6 = Mth.lerp(partialTicks, blockEntity.oOpen, blockEntity.open);
        this.bookModel.setupAnim(f, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6);
        VertexConsumer vertexconsumer = BOOK_LOCATION.buffer(bufferSource, RenderType::entitySolid);
        this.bookModel.render(poseStack, vertexconsumer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}
