package teamHTBP.vidaReforged.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

public class NonTextureEntityRenderer<T extends Entity> extends SimpleTextureEntityRender<T>{
    public NonTextureEntityRenderer(EntityRendererProvider.Context context) {
        super(context, null);
    }

    @Override
    public void render(T entity, float p1, float p2, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        // Do nothing
    }

    @Override
    public int argbColor(T entity) {
        return 0;
    }

    @Override
    public float size(T entity) {
        return 0;
    }
}
