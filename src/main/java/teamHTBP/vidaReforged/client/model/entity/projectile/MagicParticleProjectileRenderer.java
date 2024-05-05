package teamHTBP.vidaReforged.client.model.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import teamHTBP.vidaReforged.server.entity.projectile.MagicParticleProjectile;

/**
 * @author TT432
 */
public class MagicParticleProjectileRenderer extends EntityRenderer<MagicParticleProjectile> {
    public MagicParticleProjectileRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    protected int getBlockLightLevel(MagicParticleProjectile p_114496_, BlockPos p_114497_) {
        return 15;
    }

    @Override
    public void render(MagicParticleProjectile p_116085_, float p_116086_, float p_116087_, PoseStack p_116088_, MultiBufferSource p_116089_, int p_116090_) {
        if (p_116085_.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(p_116085_) < 12.25D)) {
            p_116088_.pushPose();
            p_116088_.mulPose(this.entityRenderDispatcher.cameraOrientation());
            p_116088_.mulPose(Axis.YP.rotationDegrees(180.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(Items.SLIME_BALL.getDefaultInstance(),
                    ItemDisplayContext.GROUND, p_116090_, OverlayTexture.NO_OVERLAY,
                    p_116088_, p_116089_, p_116085_.level(), p_116085_.getId());
            p_116088_.popPose();
            super.render(p_116085_, p_116086_, p_116087_, p_116088_, p_116089_, p_116090_);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(MagicParticleProjectile p_114482_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
