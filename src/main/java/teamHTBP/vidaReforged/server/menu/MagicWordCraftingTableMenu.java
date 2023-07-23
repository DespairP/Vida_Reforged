package teamHTBP.vidaReforged.server.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.helper.VidaElementHelper;
import teamHTBP.vidaReforged.server.blockEntities.MagicWordCraftingTableBlockEntity;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.menu.slots.FobiddenSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagicWordCraftingTableMenu extends AbstractContainerMenu {
    public static final String MENU_NAME = "magic_word_crafting_table";
    private final ContainerLevelAccess access;
    private final Inventory playerInventory;
    private final BlockPos blockPos;
    private final static int COL_SIZE = 18;
    /**物品栏列数*/
    private final static int INVENTORY_COL_AMOUNT = 9;
    /**物品栏里面的行数*/
    private final static int INVENTORY_ROW_AMOUNT = 3;
    /***/
    private List<String> playerMagicWords = new ArrayList<>();

    private Map<VidaElement, Slot> elementSlotMap = new HashMap<>();

    private Slot resultSlot;


    public MagicWordCraftingTableMenu(int menuId, ContainerLevelAccess access, Inventory inventory, BlockPos pos, List<String> playerMagicWords) {
        super(VidaMenuContainerTypeLoader.MAGIC_WORD_CRAFTING.get(), menuId);
        this.access = access;
        this.playerInventory = inventory;
        this.blockPos = pos;
        this.playerMagicWords = playerMagicWords;
        int xOffset = 0;
        int yOffset = 0;


        for(int row = 0; row < INVENTORY_ROW_AMOUNT; ++row) {
            for(int col = 0; col < INVENTORY_COL_AMOUNT; ++col) {
                int slotNumber = col + row * 9 + 9;
                this.addSlot(
                        new Slot(inventory, slotNumber,8 + col * COL_SIZE + xOffset,row * COL_SIZE + yOffset)
                );
            }
        }

        for(int col = 0; col < INVENTORY_COL_AMOUNT; ++col) {
            int slotNumber = col;
            this.addSlot(new Slot(inventory, slotNumber, 8 + col * 18 + xOffset, 161 - 103 + yOffset));
        }

        final List<VidaElement> normalElements = VidaElementHelper.getNormalElements();

        // 添加其他物品栏
        access.execute(((level, pos$1) -> {
            MagicWordCraftingTableBlockEntity entity = (MagicWordCraftingTableBlockEntity)level.getBlockEntity(pos$1);
            int index = 0;
            for(VidaElement element : normalElements){
                Slot elementSlot = new Slot(entity.getSlotFromElement(element), 0, (index++) * 20 + COL_SIZE * 2 + 4, -30);
                this.elementSlotMap.put(element, elementSlot);
                this.addSlot(elementSlot);
            }
            resultSlot = new FobiddenSlot( entity.getResultSlot(), 0, 80, -120);
            this.addSlot(resultSlot);
        }));

    }

    @Override
    public ItemStack quickMoveStack(Player player, int number) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, VidaBlockLoader.MAGIC_WORD_CRAFTING.get());
    }

    public BlockPos getBlockPos(){
        return blockPos;
    }

    public Inventory getPlayerInventory() {
        return playerInventory;
    }

    public List<String> getPlayerMagicWords() {
        return playerMagicWords;
    }

    public Slot getSlotFromElement(VidaElement element){
        return this.elementSlotMap.get(element);
    }

    public List<Slot> getAllElementSlot(){
        return this.elementSlotMap.values().stream().toList();
    }

    public Slot getResultSlot(){
        return resultSlot;
    }
}
