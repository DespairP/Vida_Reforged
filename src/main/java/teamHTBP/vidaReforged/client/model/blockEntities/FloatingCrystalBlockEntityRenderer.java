package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.server.blockEntities.FloatingCrystalBlockEntity;

import java.util.Locale;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class FloatingCrystalBlockEntityRenderer implements BlockEntityRenderer<FloatingCrystalBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    public FloatingCrystalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(FloatingCrystalBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int lightIn, int overlayIn) {
        if(entity.element == null || entity.element == VidaElement.EMPTY){
            return;
        }

        poseStack.pushPose();

        ResourceLocation location = new ResourceLocation(MOD_ID, String.format("block/crystal/%s_crystal_animate", entity.element.toString().toLowerCase(Locale.ROOT)));

        //
        TextureAtlasSprite atlasTexture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(location);
        VertexConsumer builder = bufferSource.getBuffer(RenderType.cutout());
        Matrix4f matrixStack = poseStack.last().pose();


        float floatWave = (float) (Math.sin(entity.floatingHeight * 0.1F) * 0.4F);

        long time = System.currentTimeMillis();
        float angle = (time / 50) % 360;

        float color = (float) (0.7f - 0.3 * floatWave);

        poseStack.translate(0.6f, 0.6f + 0.03 * floatWave, 0.6f);
        poseStack.scale(0.4f, 0.4f, 0.4f);
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        poseStack.translate(-0.5, -0.5f, -0.5);

        float minU = atlasTexture.getU0();
        float maxU = atlasTexture.getU1();
        float minV = atlasTexture.getV0();
        float maxV = atlasTexture.getV1();

        // 绑定材质
        builder.vertex(matrixStack, 0, 0, 0).color(color, color, color, 1.0f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 0).color(color, color, color, 1.0f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 0).color(color, color, color, 1.0f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 1, 0).color(color, color, color, 1.0f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();

        builder.vertex(matrixStack, 0, 1, 0).color(color, color, color, 1.0f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 0).color(color, color, color, 1.0f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 0).color(color, color, color, 1.0f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 0, 0).color(color, color, color, 1.0f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();


        builder.vertex(matrixStack, 0, 1, 1).color(color, color, color, 1.0f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 1).color(color, color, color, 1.0f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 0).color(color, color, color, 1.0f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 1, 0).color(color, color, color, 1.0f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();

        builder.vertex(matrixStack, 0, 1, 0).color(color, color, color, 1.0f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 0).color(color, color, color, 1.0f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 1).color(color, color, color, 1.0f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 1, 1).color(color, color, color, 1.0f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();


        builder.vertex(matrixStack, 0, 1, 1).color(color, color, color, 1.0f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 1).color(color, color, color, 1.0f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 1).color(color, color, color, 1.0f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 0, 1).color(color, color, color, 1.0f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();

        builder.vertex(matrixStack, 0, 0, 1).color(color, color, color, 1.0f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 1).color(color, color, color, 1.0f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 1).color(color, color, color, 1.0f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 1, 1).color(color, color, color, 1.0f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();

        builder.vertex(matrixStack, 0, 0, 0).color(color, color, color, 1.0f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 0).color(color, color, color, 1.0f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 1).color(color, color, color, 1.0f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 0, 1).color(color, color, color, 1.0f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();

        builder.vertex(matrixStack, 0, 0, 1).color(color, color, color, 1.0f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 1).color(color, color, color, 1.0f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 0).color(color, color, color, 1.0f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 0, 0).color(color, color, color, 1.0f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();

        builder.vertex(matrixStack, 0, 1, 1).color(color, color, color, 1.0f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 1, 0).color(color, color, color, 1.0f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 0, 0).color(color, color, color, 1.0f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 0, 1).color(color, color, color, 1.0f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();

        builder.vertex(matrixStack, 0, 0, 1).color(color, color, color, 1.0f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 0, 0).color(color, color, color, 1.0f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 1, 0).color(color, color, color, 1.0f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 1, 1).color(color, color, color, 1.0f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();

        builder.vertex(matrixStack, 1, 0, 1).color(color, color, color, 1.0f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 0).color(color, color, color, 1.0f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 0).color(color, color, color, 1.0f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 1).color(color, color, color, 1.0f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();

        builder.vertex(matrixStack, 1, 1, 1).color(color, color, color, 1.0f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 0).color(color, color, color, 1.0f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 0).color(color, color, color, 1.0f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 1).color(color, color, color, 1.0f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();


        poseStack.popPose();
    }
}
