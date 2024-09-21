package teamHTBP.vidaReforged.server.menu;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2i;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.menu.slots.FobiddenSlot;
import teamHTBP.vidaReforged.server.menu.slots.SherdSlot;

import java.util.ArrayList;
import java.util.List;

public class SherdResearchTableMenu extends VidaAbstractMenu {
    private SimpleContainer sherdContainer = new SimpleContainer(3){
        @Override
        public void setChanged() {
            super.setChanged();
            SherdResearchTableMenu.this.slotsChanged(this);
        }
    };
    public final static String MENU_NAME = "sherd_research_table";
    final Slot unlockSlot;
    final SherdSlot sherdSlot;
    final FobiddenSlot resultSlot;

    public SherdResearchTableMenu(int containerId, ContainerLevelAccess access, Inventory inventory) {
        super(VidaMenuContainerTypeLoader.SHERD_RESEARCH_TABLE.get(), containerId, access, inventory, 0, 0);
        unlockSlot = new Slot(sherdContainer, 0, -32, -128);
        sherdSlot = new SherdSlot(sherdContainer, 1, -32,-96, itemstack -> itemstack.is(ItemTags.DECORATED_POT_SHERDS));
        resultSlot = new FobiddenSlot(sherdContainer, 2, 60, -32);
        addSlot(unlockSlot);
        addSlot(sherdSlot);
        addSlot(resultSlot);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int container) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, VidaBlockLoader.SHERD_RESEARCH_TABLE.get());
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
    }

    public Slot getUnlockSlot() {
        return unlockSlot;
    }

    public SherdSlot getSherdSlot() {
        return sherdSlot;
    }

    public FobiddenSlot getResultSlot() {
        return resultSlot;
    }
}
