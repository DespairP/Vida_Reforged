package teamHTBP.vidaReforged.server.menu.slots;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FobiddenSlot extends Slot {
    private boolean canTake = true;

    public FobiddenSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public FobiddenSlot(Container inventoryIn, int index, int xPosition, int yPosition, boolean canTake) {
        super(inventoryIn, index, xPosition, yPosition);
        this.canTake = canTake;
    }
    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player p_40228_) {
        return canTake;
    }

    @Override
    public boolean isActive() {
        return true;
    }
}