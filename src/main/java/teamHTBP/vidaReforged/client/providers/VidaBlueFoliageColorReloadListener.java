package teamHTBP.vidaReforged.client.providers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.common.level.VidaBlueLeavesColor;

@OnlyIn(Dist.CLIENT)
public class VidaBlueFoliageColorReloadListener extends VidaFoliageColorReloadListener {
    private static final ResourceLocation LOCATION = new ResourceLocation(VidaReforged.MOD_ID, "textures/colormap/vida_blue_foliage.png");

    @Override
    protected void apply(int[] origin, ResourceManager manager, ProfilerFiller profiler) {
        VidaBlueLeavesColor.init(origin);
    }

    @Override
    protected ResourceLocation getLocation() {
        return LOCATION;
    }
}
