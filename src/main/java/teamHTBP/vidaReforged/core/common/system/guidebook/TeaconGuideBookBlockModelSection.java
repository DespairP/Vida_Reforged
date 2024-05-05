package teamHTBP.vidaReforged.core.common.system.guidebook;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.GuideBookBlockModel;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.IGuidebookComponent;
import teamHTBP.vidaReforged.core.api.screen.IGuideBookSection;

@Deprecated
public class TeaconGuideBookBlockModelSection implements IGuideBookSection {
    public ResourceLocation block;
    public String type = "BLOCK";

    @Override
    public String getType() {
        return "BLOCK";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IGuidebookComponent initComponent(int x, int y, int width, int height) {
        return new GuideBookBlockModel(x, y, width, height, block);
    }
}
