package teamHTBP.vidaReforged.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.server.entity.SparkEntity;
import teamHTBP.vidaReforged.server.entity.TrailEntity;

public class TrailEntityRenderer extends EntityRenderer<TrailEntity> {
    protected TrailEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(TrailEntity p_114482_) {
        return null;
    }
}
