package teamHTBP.vidaReforged.core.common.model;

import lombok.AllArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import teamHTBP.vidaReforged.VidaReforged;

/**
 * @author DustW
 */
@AllArgsConstructor
public class AbstractGeoModel<T extends IAnimatable & IAnimationTickable> extends AnimatedTickingGeoModel<T> {
    final String name;

    @Override
    public ResourceLocation getModelLocation(T object) {
        return new ResourceLocation(VidaReforged.MOD_ID, "geo/entity/%s.geo.json".formatted(name));
    }

    @Override
    public ResourceLocation getTextureLocation(T object) {
        return new ResourceLocation(VidaReforged.MOD_ID, "textures/entity/%s.png".formatted(name));
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T animatable) {
        return new ResourceLocation(VidaReforged.MOD_ID, "animations/entity/%s.animation.json".formatted(name));
    }
}
