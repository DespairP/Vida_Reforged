package teamHTBP.vidaReforged.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.server.entity.SparkEntity;
import teamHTBP.vidaReforged.server.entity.projectile.MagicParticleProjectile;

public class SparkEntityRenderer extends EntityRenderer<SparkEntity> {
    protected SparkEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SparkEntity p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) {

    }

    @Override
    public ResourceLocation getTextureLocation(SparkEntity entity) {
        return null;
    }
}
