package teamHTBP.vidaReforged.client.model.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.mobs.GlowLight;

public class GlowLightEntityRenderer extends SimpleTextureEntityRender<GlowLight>{
    protected GlowLightEntityRenderer(EntityRendererProvider.Context context, TextureSection texture) {
        super(context, texture);
    }

    @Override
    public int argbColor(GlowLight entity) {
        return entity.getColor().argb();
    }

    @Override
    public float size(GlowLight entity) {
        return entity.getSize();
    }
}
