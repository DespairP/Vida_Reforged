package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MagicWordCraftingTableBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    Map<VidaElement, SimpleContainer> elementSlots = new HashMap<>(Map.of(
            VidaElement.GOLD, new SimpleContainer(ItemStack.EMPTY),
            VidaElement.WOOD, new SimpleContainer(ItemStack.EMPTY),
            VidaElement.AQUA, new SimpleContainer(ItemStack.EMPTY),
            VidaElement.FIRE, new SimpleContainer(ItemStack.EMPTY),
            VidaElement.EARTH, new SimpleContainer(ItemStack.EMPTY)
    ));
    Map<VidaElement,String> magicWordMap = new HashMap<>();

    public MagicWordCraftingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.MAGIC_WORD_CRAFTING.get(), pPos, pBlockState);
    }

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {
        if(level.isClientSide()){
            setChanged();
            return;
        }
    }

    public void setMagicWordMap(Map<VidaElement, String> magicWordMap) {
        this.magicWordMap = magicWordMap;
    }

    public Map<VidaElement, String> getMagicWordMap() {
        return magicWordMap;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag listTag = new ListTag();

        for(VidaElement element : VidaElement.values()){
            CompoundTag magicWordTag = new CompoundTag();
            magicWordTag.putString("magicWordElement", element.toString());
            if(magicWordMap.get(element) == null){
                continue;
            }
            magicWordTag.putString("magicWordId", magicWordMap.get(element));
            listTag.add(magicWordTag);
        }

        tag.put("magicWords", listTag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ListTag magicListTag = (ListTag) tag.get("magicWords");
        if(magicListTag == null){
            return;
        }
        for(int i = 0; i < magicListTag.size(); ++i) {
            CompoundTag magicTag = magicListTag.getCompound(i);
            VidaElement element = VidaElement.of(magicTag.getString("magicWordElement"));
            String wordId = magicTag.getString("magicWordId");
            magicWordMap.put(element, wordId);
        }
    }


    public SimpleContainer getSlotFromElement(VidaElement element){
        return this.elementSlots.get(element);
    }
}
