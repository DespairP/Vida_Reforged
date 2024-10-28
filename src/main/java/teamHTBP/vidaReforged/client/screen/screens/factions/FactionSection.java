package teamHTBP.vidaReforged.client.screen.screens.factions;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import teamHTBP.vidaReforged.core.common.ui.VidaLifecycleSection;

public class FactionSection extends VidaLifecycleSection {
    EntityType<?> entityType;

    public FactionSection(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
    }


}
