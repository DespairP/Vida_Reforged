package teamHTBP.vidaReforged.core.common.system.guidebook;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.GuideBookBlockListModel;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.IGuidebookComponent;
import teamHTBP.vidaReforged.core.api.screen.IGuideBookSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeaconGuideBookBlockModelList implements IGuideBookSection {
    public ResourceLocation[] blocklist = new ResourceLocation[]{new ResourceLocation("minecraft:air")};
    public String type = "BLOCKLIST";

    @Override
    public String getType() {
        return "BLOCKLIST";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IGuidebookComponent initComponent(int x, int y, int width, int height) {
        return new GuideBookBlockListModel(x,y,width,height, Arrays.stream(blocklist).toList());
    }
}
