package teamHTBP.vidaReforged.server.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class AbstractFactionMenu extends VidaAbstractMenu{

    public AbstractFactionMenu(@Nullable MenuType<?> menuType, int containerId, ContainerLevelAccess access, Inventory playerInventory) {
        super(menuType, containerId, access, playerInventory, 0, 0);
    }
}
