package teamHTBP.vidaReforged.client.screen.screens.factions;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import teamHTBP.vidaReforged.server.menu.TradeFractionMenu;

public class TradeFactionBasedScreen extends FactionBasedScreen<TradeFractionMenu>{
    protected TradeFactionBasedScreen(TradeFractionMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected FactionSection addEntityFactionSection() {
        return null;
    }

    @Override
    protected FactionSection addDetailedFactionSection() {
        return null;
    }
}
