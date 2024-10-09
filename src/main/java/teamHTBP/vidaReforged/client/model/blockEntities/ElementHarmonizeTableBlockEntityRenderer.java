package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.core.common.vertex.VidaItemRenderer;
import teamHTBP.vidaReforged.server.blockEntities.ElementHarmonizeTableBlockEntity;

import java.util.Locale;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class ElementHarmonizeTableBlockEntityRenderer extends VidaBlockEntityRenderer<ElementHarmonizeTableBlockEntity>{
    final VidaItemRenderer itemRenderer;
    BlockEntityRendererProvider.Context context;

    public ElementHarmonizeTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.context = context;
        this.itemRenderer = VidaItemRenderer.ITEM_RENDERER;
    }

    @Override
    public void render(ElementHarmonizeTableBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        renderIconInProcess(blockEntity, partialTicks, poseStack, bufferSource, combinedLightIn, combinedOverlayIn);
        renderItem(blockEntity, partialTicks, poseStack, bufferSource, combinedLightIn, combinedOverlayIn);
    }

    /**渲染物品*/
    protected void renderItem(ElementHarmonizeTableBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn){
        if(blockEntity.isProcessing()){
            return;
        }
        ItemStack realStack = blockEntity.getItemWithCopy();
        ItemStack virtualStack = blockEntity.getVirtualItem();
        if(realStack.isEmpty() && virtualStack.isEmpty()){
            return;
        }
        BakedModel bakedmodel = this.itemRenderer.getModel(realStack.isEmpty() ? virtualStack : realStack, blockEntity.getLevel(), (LivingEntity) null, 0);
        float floating = Mth.sin(((float) ClientTickHandler.ticks + partialTicks) / 10.0F) * 0.1F + 0.1F;
        float scale = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
        float alpha = realStack.isEmpty() ? 0.3f : 1f;
        RenderSystem.enableBlend();
        poseStack.pushPose();
        poseStack.translate(0.5F, floating + 0.25F * scale + 1.3F, 0.5F);
        poseStack.mulPose(Axis.YP.rotation(blockEntity.spin));
        itemRenderer.render(realStack.isEmpty() ? virtualStack : realStack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, combinedLightIn, OverlayTexture.NO_OVERLAY, bakedmodel, alpha);
        RenderSystem.setShaderColor(1,1,1, 1);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }


    protected void renderIconInProcess(ElementHarmonizeTableBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn){
        ResourceLocation location = new ResourceLocation(MOD_ID, String.format("icons/%slogo", blockEntity.getElement().getElementName().toLowerCase(Locale.ROOT)));
        poseStack.pushPose();
        RenderSystem.enableBlend();
        TextureAtlasSprite atlasTexture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(location);
        Matrix4f matrix4f = poseStack.last().pose();
        poseStack.translate(0.5F,2, 0.5F);
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS));
        float uMin = atlasTexture.getU0();
        float uMax = atlasTexture.getU1();
        float vMin = atlasTexture.getV0();
        float vMax = atlasTexture.getV1();
        float alpha = blockEntity.getProcessTick() < 100 ? 0 : Math.min((blockEntity.getProcessTick() - 100f) / 20F, 1);

        Vector3f[] points = new Vector3f[]{
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F)
        };
        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = points[i];
            vector3f.mul(0.3f);
            vector3f.rotate(context.getEntityRenderer().cameraOrientation());
        }

        buffer.vertex(matrix4f, points[0].x, points[0].y, points[0].z).color(1, 1, 1, alpha).uv(uMin, vMin).overlayCoords(1).uv2(240, 240).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix4f, points[1].x, points[1].y, points[1].z).color(1, 1, 1, alpha).uv(uMin, vMax).overlayCoords(1).uv2(240, 240).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix4f, points[2].x, points[2].y, points[2].z).color(1, 1, 1, alpha).uv(uMax, vMax).overlayCoords(1).uv2(240, 240).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix4f, points[3].x, points[3].y, points[3].z).color(1, 1, 1, alpha).uv(uMax, vMin).overlayCoords(1).uv2(240, 240).normal(1, 0, 0).endVertex();

        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    @Override
    public boolean shouldRender(ElementHarmonizeTableBlockEntity blockEntity, Vec3 viewPoint) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull ElementHarmonizeTableBlockEntity blockEntity) {
        return true;
    }
}
