package teamHTBP.vidaReforged.server.menu.slots;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.core.common.container.BlockEntityHelper;
import teamHTBP.vidaReforged.core.common.item.Position;

import java.util.function.Predicate;

public class VidaWandSlot extends Slot {
    private final Predicate<ItemStack> tester;

    public VidaWandSlot(Container inventoryIn, int index, int xPosition, int yPosition, Predicate<ItemStack> tester) {
        super(inventoryIn, index, xPosition, yPosition);
        this.tester = tester;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player p_40228_) {
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
        super.onTake(player, itemStack);
        if(itemStack.isEmpty()){
            return;
        }

        CompoundTag tag = itemStack.getOrCreateTag();
        ListTag listtag = new ListTag();

        for(int i = 0; i < Position.values().length; i++){
            ItemStack equipment = container.removeItem(i, 1);
            CompoundTag compoundtag = new CompoundTag();
            compoundtag.putByte("Slot", (byte)i);
            equipment.save(compoundtag);
            listtag.add(compoundtag);
        }
        tag.put("equipments", listtag);
    }

    @Override
    public void set(ItemStack itemStack) {
        super.set(itemStack);
        if(itemStack.isEmpty()){
            return;
        }

        CompoundTag tag = itemStack.getOrCreateTag();
        ListTag equipmentsTag = (ListTag) tag.get("equipments");
        NonNullList<ItemStack> equipmentItems = NonNullList.withSize(4, ItemStack.EMPTY);

        if(equipmentsTag != null && !equipmentsTag.isEmpty()){
            for(int i = 0; i < equipmentsTag.size(); ++i) {
                CompoundTag compoundtag = equipmentsTag.getCompound(i);
                int j = compoundtag.getByte("Slot") & 255;
                equipmentItems.set(j, ItemStack.of(compoundtag));
            }
        }

        for(int i = 0; i < equipmentItems.size(); i++){
            container.setItem(i , equipmentItems.get(i));
        }
    }
}
