package teamHTBP.vidaReforged.client.screen.screens.factions;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.client.screen.components.VidaLifecycleSection;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.client.screen.components.common.ItemStackWidget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeLevelSection extends VidaLifecycleSection {
    private Map<Integer, List<ItemStack>> tradeItems = new HashMap<>();
    private Map<ResourceLocation, VidaWidget> buttons = new HashMap<>();

    public TradeLevelSection(int x, int y, int width, int height, Component component, ResourceLocation componentId) {
        super(x, y, width, height, component, componentId);
    }
}
