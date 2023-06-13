package teamHTBP.vidaReforged.core.common.model;

import lombok.AllArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import teamHTBP.vidaReforged.VidaReforged;

/**
 * @author DustW
 */
@AllArgsConstructor
public class AbstractGeoModel<T extends GeoAnimatable> extends GeoModel<T> {
    final String name;

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return new ResourceLocation(VidaReforged.MOD_ID, "geo/entity/%s.geo.json".formatted(name));
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return new ResourceLocation(VidaReforged.MOD_ID, "textures/entity/%s.png".formatted(name));
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return new ResourceLocation(VidaReforged.MOD_ID, "animations/entity/%s.animation.json".formatted(name));
    }
}
