package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;

public class InjectTableBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    public long time = 0;

    ItemStack itemStack = ItemStack.EMPTY;

    public InjectTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.INJECT_TABLE.get(), pPos, pBlockState);
    }

    public ItemStack getItemForDisplay(){
        return itemStack.copy();
    }

    public ItemStack getItem(){
        ItemStack stack = new ItemStack(Items.NETHERITE_SWORD, 1);
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
        this.itemStack = ItemStack.of(tag.getCompound("showItem"));
        super.load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("showItem", itemStack.serializeNBT());
        super.saveAdditional(tag);
    }


    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {
        if(pLevel.isClientSide){
            time ++;
            return;
        }
    }
}
