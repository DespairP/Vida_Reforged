package teamHTBP.vidaReforged.client.model.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import teamHTBP.vidaReforged.core.common.model.AbstractGeoModel;
import teamHTBP.vidaReforged.server.mobs.AncientBeliever;
import static teamHTBP.vidaReforged.core.common.VidaConstant.*;

/**
 * @author DustW
 */
public class AncientBelieverRenderer extends GeoEntityRenderer<AncientBeliever> {
    public AncientBelieverRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AbstractGeoModel<>(ANCIENT_BELIEVER_NAME));
    }
}
