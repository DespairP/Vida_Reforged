package teamHTBP.vidaReforged.core.common.system.guidebook;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.IGuidebookComponent;
import teamHTBP.vidaReforged.core.api.screen.IGuideBookSection;

@Deprecated
public class TeaconGuideBookEmptySection implements IGuideBookSection {
    public String type = "EMPTY";

    @Override
    public String getType() {
        return "EMPTY";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IGuidebookComponent initComponent(int x, int y, int width, int height) {
        return null;
    }
}
