package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VaseBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    private SimpleContainer flowers = new SimpleContainer(3){
        @Override
        public int getMaxStackSize() {
            return 1;
        }
    };
    private boolean isCompleted = false;
    private List<Integer> placeRandom;

    public VaseBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.VASE.get(), pPos, pBlockState);
        ArrayList<Integer> places = new ArrayList<>(List.of(0,1,2,3,4));
        Collections.shuffle(places);
        this.placeRandom = places;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.isCompleted = tag.getBoolean("IsCompleted");
        flowers.fromTag((ListTag) tag.get("Flowers"));
        ListTag listTag = (ListTag) tag.get("places");
        placeRandom = new ArrayList<>(List.of(listTag.getInt(0), listTag.getInt(1), listTag.getInt(2), listTag.getInt(3), listTag.getInt(4)));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("IsCompleted",  this.isCompleted);
        tag.put("Flowers",  flowers.createTag());
        tag.put("places", newIntList(this.placeRandom));
    }

    protected ListTag newIntList(List<Integer> p_20066_) {
        ListTag listtag = new ListTag();

        for(int f : p_20066_) {
            listtag.add(IntTag.valueOf(f));
        }

        return listtag;
    }

    public boolean putFlower(ItemStack itemStack){
        if(isCompleted){
            return false;
        }
        for (int i = 0; i < 3; i++) {
            ItemStack container = flowers.getItem(i);
            if(container.isEmpty()){
                flowers.setItem(i, itemStack.copyWithCount(1));
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 1 | 2);
                return true;
            }
        }
        return false;
    }

    public SimpleContainer getFlowers(){
        return this.flowers;
    }

    public List<Integer> getPlaceRandom(){
        return this.placeRandom.subList(0, 3);
    }

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {
        if(pLevel.isClientSide()){
            setUpdated();
            return;
        }
    }
}
