package teamHTBP.vidaReforged.server.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.server.blockEntities.PrismBlockEntity;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.items.ElementGem;
import teamHTBP.vidaReforged.server.menu.slots.ItemPredicateSlot;
import teamHTBP.vidaReforged.server.menu.slots.FobiddenSlot;

public class PrismMenu extends AbstractContainerMenu {
    private static final int INVENTORY_ROW_AMOUNT = 3;
    private static final int INVENTORY_COL_AMOUNT = 9;
    private final ContainerLevelAccess access;
    private final Inventory playerInventory;
    public static final String MENU_NAME = "prism";
    private final BlockPos pos;

    public PrismMenu(int pContainerId, ContainerLevelAccess access, Inventory inventory, BlockPos pos) {
        super(VidaMenuContainerTypeLoader.PRISM.get(),pContainerId);
        this.access = access;
        this.playerInventory = inventory;
        this.pos = pos;



        for(int row = 0; row < INVENTORY_ROW_AMOUNT; ++row) {
            for(int col = 0; col < INVENTORY_COL_AMOUNT; ++col) {
                int slotNumber = col + row * 9 + 9;
                this.addSlot(new Slot(inventory ,slotNumber,  col * 18, row * 18));
            }
        }

        for(int col = 0; col < INVENTORY_COL_AMOUNT; ++col) {
            int slotNumber = col;
            this.addSlot(new Slot(inventory, slotNumber, col * 18 , 3 * 18 + 4));
        }

        this.access.execute((level, pPos) -> {
            PrismBlockEntity entity = (PrismBlockEntity)level.getBlockEntity(pPos);

            this.addSlot(new ItemPredicateSlot(entity.getInputAndResult(),0,50,-30, stack -> stack.is(itemHolder -> itemHolder.get() instanceof ElementGem)));
            this.addSlot(new FobiddenSlot(entity.getInputAndResult(),1,110,-30));
        });
    }

    @Override
    public ItemStack quickMoveStack(Player player, int number) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, VidaBlockLoader.PRISM.get());
    }

    public BlockPos getPos() {
        return pos;
    }
}
