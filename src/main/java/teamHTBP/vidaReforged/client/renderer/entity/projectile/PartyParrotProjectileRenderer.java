package teamHTBP.vidaReforged.client.renderer.entity.projectile;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.server.entity.projectile.MagicParticleProjectile;
import teamHTBP.vidaReforged.server.entity.projectile.PartyParrotProjecttile;

import java.util.List;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

@OnlyIn(Dist.CLIENT)
public class PartyParrotProjectileRenderer extends EntityRenderer<PartyParrotProjecttile> {
    private EntityRendererProvider.Context context;

    private static List<ResourceLocation> locations = ImmutableList.of(
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_coffee"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_deal"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_deployee"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_disco"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_github"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_merged_twins"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_mongodb"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_opensource"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_python"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_react"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_rythm"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_shuffle"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_stable"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_too_fast"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_two"),
            new ResourceLocation(MOD_ID, "block/parrots/partyparrot_vue")
    );

    public PartyParrotProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public ResourceLocation getTextureLocation(PartyParrotProjecttile p_114482_) {
        return new ResourceLocation(MOD_ID,"textures/item/parrots/partyparrot.png");
    }

    @Override
    public void render(PartyParrotProjecttile tile, float p1, float p2, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
        //super.render(tile, p1, p2, poseStack, bufferSource, packedLightIn);

        poseStack.pushPose();
        RenderSystem.enableBlend();

        TextureAtlasSprite textureAtlasSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(locations.get(tile.getTypeOfParrot() % locations.size()));

        VertexConsumer builder = bufferSource.getBuffer(RenderType.cutoutMipped());
        poseStack.translate(0.5f, 1.2F, 0.5f);
        poseStack.mulPose(this.context.getEntityRenderDispatcher().cameraOrientation());
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(-0.5f, -0.5, -0.5f);

        Matrix4f matrixStack = poseStack.last().pose();
        float color = 1;

        float uMin = textureAtlasSprite.getU0();
        float uMax = textureAtlasSprite.getU1();
        float vMin = textureAtlasSprite.getV0();
        float vMax = textureAtlasSprite.getV1();

        builder.vertex(matrixStack, 0, 1, 0.5F).color(color, color, color, 1.0f).uv(uMin, vMin).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 1, 0.5F).color(color, color, color, 1.0f).uv(uMax, vMin).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 1, 0, 0.5F).color(color, color, color, 1.0f).uv(uMax, vMax).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrixStack, 0, 0, 0.5F).color(color, color, color, 1.0f).uv(uMin, vMax).uv2(240, 240).normal(1, 0, 0).endVertex();

        RenderSystem.disableBlend();

        poseStack.popPose();
    }
}
