package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;

public class CrystalDecorationBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    ItemStack itemStack = ItemStack.EMPTY;

    public CrystalDecorationBlockEntity(BlockPos pos, BlockState state) {
        super(VidaBlockEntityLoader.GEM_SHELF.get(), pos, state);
    }

    public ItemStack getItemWithoutClear(){
        return itemStack.copy();
    }

    public ItemStack getItemStack(){
        ItemStack stack = itemStack.copy();
        this.itemStack = ItemStack.EMPTY;
        return stack;
    }

    public boolean hasItem(){
        return !this.itemStack.isEmpty();
    }

    public void putItem(ItemStack stack){
        this.itemStack = stack.copy();
    }

    public void setUpdated(){
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 1 | 2);
    }

    @Override
    public void load(CompoundTag tag) {
        this.itemStack = ItemStack.of(tag.getCompound("item"));

        super.load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("item", itemStack.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {

    }
}
