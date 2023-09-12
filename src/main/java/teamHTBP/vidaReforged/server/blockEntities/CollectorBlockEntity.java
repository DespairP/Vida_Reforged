package teamHTBP.vidaReforged.server.blockEntities;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.core.api.debug.IDebugObj;
import teamHTBP.vidaReforged.server.items.BreathCatcher;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import java.util.Map;

public class CollectorBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity, IDebugObj {
    private VidaElement collectElement = VidaElement.EMPTY;

    private float progress = 0.0f;

    private boolean isInProgress = false;

    private float step = 0.0f;

    public ItemStack collectItem = ItemStack.EMPTY;

    private final float MAX_PROGRESS = 1000.0f;


    public CollectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.COLLECTOR.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.collectElement = VidaElement.valueOf(tag.getString("collectElement"));
        this.progress = tag.getFloat("progress");
        this.isInProgress = tag.getBoolean("isInProgress");
        this.step = tag.getFloat("step");
        this.collectItem = ItemStack.of(tag.getCompound("collectItem"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("collectElement", this.collectElement.toString());
        tag.putFloat("progress", this.progress);
        tag.putBoolean("isInProgress", this.isInProgress);
        tag.putFloat("step", this.step);
        tag.put("collectItem", this.collectItem.serializeNBT());
    }

    public boolean putItem(ItemStack stack){
        boolean result = !stack.isEmpty() && stack.is(VidaItemLoader.BREATH_CATCHER.get()) && collectItem.isEmpty();
        if(result){
            this.collectItem = new ItemStack(VidaItemLoader.BREATH_CATCHER.get());
            stack.shrink(1);
        }
        return result;
    }

    public ItemStack getItem(){
        ItemStack stack = this.collectItem.copyAndClear();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 1 | 2);
        return stack;
    }

    public boolean canGetItem(){
        return !this.collectItem.isEmpty();
    }


    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {
        if(pLevel.isClientSide){
            setChanged();
            return;
        }

        if(!isInProgress){
            start(pLevel);
            return;
        }
        continueTask();
        validate();
        setChanged();
        pLevel.sendBlockUpdated(pPos, pState, pState, 1 | 2);
    }

    public float getMaxProgress(){
        return MAX_PROGRESS;
    }

    /**开始收集*/
    public void start(Level pLevel){
        if(!this.collectItem.is(VidaItemLoader.BREATH_CATCHER.get())){
            return;
        }
        if(!this.isInProgress){
            this.isInProgress = true;
            this.step = 1f;
            this.collectElement = pLevel.dimension() == Level.NETHER ? VidaElement.FIRE : VidaElement.EARTH;
        }
    }


    /**检查物品是否还在*/
    public void validate(){
        if(this.collectItem.isEmpty()){
            reset();
        }
    }

    /**重置所有进度*/
    public void reset(){
        this.step = 0;
        //this.collectItem = ItemStack.EMPTY;
        this.progress = 0;
        this.collectElement = VidaElement.EMPTY;
        ItemStack stack = new ItemStack(VidaItemLoader.UNLOCK_MAGIC_WORD_PAPER.get(), 1);
        stack.getOrCreateTag().putString("wordId","vida_reforged:energy");
        this.collectItem = stack;
        this.isInProgress = false;
    }

    public void continueTask(){
        if(!isInProgress){
            return;
        }
        this.progress += step;
        if(progress >= MAX_PROGRESS){
            //generate
            reset();
            this.step = 0;
        }
    }


    @Override
    public Map<String, String> getDebugAttributes() {
        return ImmutableMap.of(
                "collectElement", this.collectElement.toString(),
                "progress", String.valueOf(this.progress),
                "isInProgress", String.valueOf(this.isInProgress) ,
                "step", String.valueOf(this.step),
                "collectItem", this.collectItem.toString()
        );
    }

    public float getProgress() {
        return progress;
    }


}
