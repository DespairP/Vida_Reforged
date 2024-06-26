package teamHTBP.vidaReforged.client.model.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import teamHTBP.vidaReforged.core.common.model.AbstractGeoModel;
import teamHTBP.vidaReforged.server.mobs.OrangeSpottedSparrow;

import static teamHTBP.vidaReforged.core.common.VidaConstant.ORANGE_SPOTTED_SPARROW_NAME;

public class OrangeSpottedSparrowsRenderer extends GeoEntityRenderer<OrangeSpottedSparrow> {
    public OrangeSpottedSparrowsRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AbstractGeoModel<>(ORANGE_SPOTTED_SPARROW_NAME));
    }
}
