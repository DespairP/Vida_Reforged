package teamHTBP.vidaReforged.client.level;

import net.minecraft.client.resources.LegacyStuffWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.common.level.VidaBlueLeavesColor;
import teamHTBP.vidaReforged.core.common.level.VidaLeavesColor;

import java.io.IOException;

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
