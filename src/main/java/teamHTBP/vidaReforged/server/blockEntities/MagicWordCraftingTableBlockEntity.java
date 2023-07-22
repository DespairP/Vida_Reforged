package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.server.recipe.VidaRecipeManager;
import teamHTBP.vidaReforged.server.recipe.ingredient.ItemStackListIngredient;
import teamHTBP.vidaReforged.server.recipe.records.VidaMagicWordRecipe;

import java.util.*;
import java.util.stream.Collectors;

import static teamHTBP.vidaReforged.helper.VidaElementHelper.getNormalElements;

public class MagicWordCraftingTableBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    Map<VidaElement, SimpleContainer> elementSlots = new HashMap<>(Map.of(
            VidaElement.GOLD, new SimpleContainer(ItemStack.EMPTY),
            VidaElement.WOOD, new SimpleContainer(ItemStack.EMPTY),
            VidaElement.AQUA, new SimpleContainer(ItemStack.EMPTY),
            VidaElement.FIRE, new SimpleContainer(ItemStack.EMPTY),
            VidaElement.EARTH, new SimpleContainer(ItemStack.EMPTY)
    ));
    Map<VidaElement,String> magicWordMap = new HashMap<>();
    public boolean isCrafting = false;

    private SimpleContainer resultItem = new SimpleContainer(ItemStack.EMPTY);

    public MagicWordCraftingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.MAGIC_WORD_CRAFTING.get(), pPos, pBlockState);
    }

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {
        if(level.isClientSide()){
            setChanged();
            return;
        }

        if(doRecipeAndUpdate()){
            level.sendBlockUpdated(this.worldPosition, entity.getBlockState(), entity.getBlockState(), 1 | 2);
        }
        setChanged();
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

        tag.putBoolean("isCrafting", isCrafting);

        ListTag listTag = new ListTag();
        // 保存选择的词条
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

        // 保存
        for(VidaElement element : elementSlots.keySet()){
            tag.put(
                    String.format("%s:%s", element.toString(), "item"),
                    this.elementSlots.get(element).getItem(0).serializeNBT()
            );
        }
        tag.put("resultItems", resultItem.getItem(0).serializeNBT());

    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        //
        this.isCrafting = tag.getBoolean("isCrafting");
        //
        ListTag magicListTag = (ListTag) tag.get("magicWords");
        if(magicListTag == null){
            return;
        }
        // 加载词条
        for(int i = 0; i < magicListTag.size(); ++i) {
            CompoundTag magicTag = magicListTag.getCompound(i);
            VidaElement element = VidaElement.of(magicTag.getString("magicWordElement"));
            String wordId = magicTag.getString("magicWordId");
            magicWordMap.put(element, wordId);
        }
        // 加载物品
        for(VidaElement element : elementSlots.keySet()){
            CompoundTag compound = tag.getCompound(String.format("%s:%s", element.toString(), "item"));
            ItemStack stack = ItemStack.of(compound);
            this.elementSlots.get(element).setItem(0, stack);
        }
        // 加载
        this.resultItem.setItem(0, ItemStack.of(tag.getCompound("resultItems")) );
    }


    public SimpleContainer getSlotFromElement(VidaElement element){
        return this.elementSlots.get(element);
    }

    public SimpleContainer getResultSlot(){
        return this.resultItem;
    }

    public List<ItemStack> getElementStacks(){
        List<ItemStack> itemStacks = new ArrayList<>();
        this.elementSlots.forEach((element, container) -> itemStacks.add(container.getItem(0)));
        return itemStacks;
    }

    public boolean doRecipeAndUpdate(){
        if(level == null || !this.isCrafting){
            return false;
        }

        VidaMagicWordRecipe recipe = VidaRecipeManager.getMagicWordRecipe(level,this);
        this.isCrafting = false;
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 1 | 2);

        if(recipe == null){
            return false;
        }
        if(this.resultItem.getItem(0).isEmpty()){
            consumeWithRecipe(recipe);
        }
        return true;
    }


    public void consumeWithRecipe(VidaMagicWordRecipe recipe){
        List<ItemStack> currentStacks = getElementStacks();
        final Map<Item, ItemStack> matchMap = currentStacks.stream().collect(Collectors.toMap(ItemStack::getItem, itemStack -> itemStack, (first, second) -> first));

        ItemStackListIngredient ingredient = recipe.ingredients;
        for(ItemStack requiredStack : ingredient.requiredItemStack){
            ItemStack currentStack = matchMap.get(requiredStack.getItem());
            currentStack.shrink(requiredStack.getCount());
        }

        this.resultItem.setItem(0,recipe.resultItem.copy());
    }
}
