package teamHTBP.vidaReforged.core.api.screen;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.IGuidebookComponent;

public interface IGuideBookSection {
    public String getType();

    @OnlyIn(Dist.CLIENT)
    public IGuidebookComponent initComponent(int x, int y, int width, int height);
}
