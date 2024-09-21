package teamHTBP.vidaReforged.server.menu.slots;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ItemPredicateSlot extends Slot {
    final Predicate<ItemStack> tester;

    public ItemPredicateSlot(Container inventoryIn, int index, int xPosition, int yPosition, Predicate<ItemStack> tester) {
        super(inventoryIn, index, xPosition, yPosition);
        this.tester = tester;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return tester.test(stack);
    }
}
