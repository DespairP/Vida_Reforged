package teamHTBP.vidaReforged.core.common.system.guidebook;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.GuideBookScrollTextArea;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.IGuidebookComponent;
import teamHTBP.vidaReforged.core.api.screen.IGuideBookSection;

public class TeaconGuideBookTextSection implements IGuideBookSection {
    public String text;
    public String type = "TEXT";
    @Override
    public String getType() {
        return "TEXT";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IGuidebookComponent initComponent(int x, int y, int width, int height) {
        return new GuideBookScrollTextArea(text, x, y, width, height);
    }

    public String getText() {
        return text;
    }
}
