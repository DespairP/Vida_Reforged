package teamHTBP.vidaReforged.server.menu.slots;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.server.items.VidaWandEquipment;

/**指定饰品的饰品槽*/
public class VidaWandEquipmentSlot extends Slot {
    private Position position;

    public VidaWandEquipmentSlot(Container container, int index, int xPosition, int yPosition, Position slotPosition) {
        super(container, index, xPosition, yPosition);
        this.position = slotPosition;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        //
        ItemStack wandItemStack = getVidaWandItemStack();
        if(wandItemStack == null || wandItemStack.isEmpty()){
            return false;
        }
        return itemStack.is(itemHolder->{
            if(itemHolder.get() instanceof VidaWandEquipment ep){
                return ep.getAttribute().getPosition() == position;
            }
            return false;
        });
    }

    public ItemStack getVidaWandItemStack(){
        return this.container.getItem(this.container.getContainerSize() - 1);
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean isHighlightable() {
        return false;
    }
}
