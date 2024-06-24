package teamHTBP.vidaReforged.client.model.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import teamHTBP.vidaReforged.core.common.VidaConstant;
import teamHTBP.vidaReforged.core.common.model.AbstractGeoModel;
import teamHTBP.vidaReforged.server.mobs.LightSheep;

public class LightSheepRenderer extends GeoEntityRenderer<LightSheep> {
    public LightSheepRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AbstractGeoModel<>(VidaConstant.LIGHT_SHEEP_NAME));
    }

}
