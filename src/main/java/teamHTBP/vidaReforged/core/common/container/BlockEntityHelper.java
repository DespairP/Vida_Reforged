package teamHTBP.vidaReforged.core.common.container;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class BlockEntityHelper {
    public static void loadAllItems(CompoundTag p_18981_, NonNullList<ItemStack> p_18982_) {
        ListTag listtag = p_18981_.getList("Items", 10);

        for(int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            if (j >= 0) {
                p_18982_.add(j, ItemStack.of(compoundtag));
            }
        }

    }
}
