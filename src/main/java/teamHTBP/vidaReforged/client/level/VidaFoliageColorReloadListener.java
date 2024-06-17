package teamHTBP.vidaReforged.client.level;

import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.LegacyStuffWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.FoliageColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.common.level.VidaLeavesColor;

import java.io.FileNotFoundException;
import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class VidaFoliageColorReloadListener extends SimplePreparableReloadListener<int[]> {
    private static final ResourceLocation LOCATION = new ResourceLocation(VidaReforged.MOD_ID, "textures/colormap/vida_foliage.png");

    @Override
    protected int[] prepare(ResourceManager manager, ProfilerFiller profiler) {
        try {
            return LegacyStuffWrapper.getPixels(manager, LOCATION);
        } catch (IOException ioexception) {
            throw new IllegalStateException("Failed to load foliage color texture", ioexception);
        }
    }

    @Override
    protected void apply(int[] origin, ResourceManager manager, ProfilerFiller profiler) {
        VidaLeavesColor.init(origin);
    }
}
