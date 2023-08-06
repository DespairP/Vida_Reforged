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
import teamHTBP.vidaReforged.server.blockEntities.CrystalLanternBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.FloatingCrystalBlockEntity;

import java.util.Locale;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class CrystalLanternRenderer implements BlockEntityRenderer<CrystalLanternBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public CrystalLanternRenderer(BlockEntityRendererProvider.Context context){
        this.context = context;
    }
    
    @Override
    public void render(CrystalLanternBlockEntity block, float p_112308_, PoseStack poseStack, MultiBufferSource bufferSource, int lightIn, int layoutIn) {
        ResourceLocation location = new ResourceLocation(MOD_ID, "block/crystal/gold_crystal_animate");


        long time = System.currentTimeMillis();
        float angle = (time / 50) % 360;
        float floatWave = (float) (Math.sin(Math.toRadians(angle)) * 0.4F);
        final float size = 0.3f;
        VertexConsumer builder = bufferSource.getBuffer(RenderType.translucent());

        poseStack.pushPose();
        poseStack.translate(0.50f, 0.6f + 0.2 * floatWave - 0.12f, 0.50f);
        poseStack.scale(0.3f, 0.3f, 0.3f);
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));

        TextureAtlasSprite atlasTexture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(location);
        Matrix4f matrixStack = poseStack.last().pose();

        float minU = atlasTexture.getU0();
        float maxU = atlasTexture.getU1();
        float minV = atlasTexture.getV0();
        float maxV = atlasTexture.getV1();

        //上面
        //正面
        builder.vertex(matrixStack, 0, 1, 0).color(1, 1, 1, 0.6f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, -size, 0, size).color(1, 1, 1, 0.6f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, size, 0, size).color(1, 1, 1, 0.6f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, size, 0, size).color(1, 1, 1, 0.6f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();

        //左边
        builder.vertex(matrixStack, 0, 1, 0).color(1, 1, 1, 0.6f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, -size, 0, -size).color(1, 1, 1, 0.6f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, -size, 0, size).color(1, 1, 1, 0.6f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, -size, 0, size).color(1, 1, 1, 0.6f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();

        //背后
        builder.vertex(matrixStack, 0, 1, 0).color(1, 1, 1, 0.6f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, size, 0, -size).color(1, 1, 1, 0.6f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, -size, 0, -size).color(1, 1, 1, 0.6f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, -size, 0, -size).color(1, 1, 1, 0.6f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();

        //背后
        builder.vertex(matrixStack, 0, 1, 0).color(1, 1, 1, 0.6f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, size, 0, size).color(1, 1, 1, 0.6f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, size, 0, -size).color(1, 1, 1, 0.6f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, size, 0, -size).color(1, 1, 1, 0.6f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();

        //下面
        //正面
        builder.vertex(matrixStack, size, 0, size).color(1, 1, 1, 0.6f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, size, 0, size).color(1, 1, 1, 0.6f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, -size, 0, size).color(1, 1, 1, 0.6f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, -1, 0).color(1, 1, 1, 0.6f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();

        //左边
        builder.vertex(matrixStack, -size, 0, size).color(1, 1, 1, 0.6f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, -size, 0, size).color(1, 1, 1, 0.6f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, -size, 0, -size).color(1, 1, 1, 0.6f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, -1, 0).color(1, 1, 1, 0.6f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();

        //背后
        builder.vertex(matrixStack, -size, 0, -size).color(1, 1, 1, 0.6f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, -size, 0, -size).color(1, 1, 1, 0.6f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, size, 0, -size).color(1, 1, 1, 0.6f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, -1, 0).color(1, 1, 1, 0.6f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();

        //背后
        builder.vertex(matrixStack, size, 0, -size).color(1, 1, 1, 0.6f).uv(minU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, size, 0, -size).color(1, 1, 1, 0.6f).uv(maxU, maxV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, size, 0, size).color(1, 1, 1, 0.6f).uv(maxU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, -1, 0).color(1, 1, 1, 0.6f).uv(minU, minV).uv2(240, 240).normal(1, 0, 0).endVertex();


        poseStack.popPose();
    }
}
