package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.VidaElement;

public abstract class AbstractPurificationCauldronBlockEntity extends BlockEntity{
    /**锅中的主要属性，或者是第一个被添加的属性*/
    public VidaElement mainElement = VidaElement.EMPTY;
    /**本次提纯进度*/
    public float progress = 0.0f;
    /**步长*/
    public float step = 1.0f;
    /**本次目标提纯进度*/
    public float targetProgress = 0.0f;
    /**总进度*/
    public float totalProgress = 0.0f;
    /***/
    public final static float MAX_TOTAL_PROGRESS = 2000.0f;
    /**是否正在提纯*/
    public boolean isInProgress = false;
    /**正在提纯的物品*/
    public NonNullList<ItemStack> purificationItems = NonNullList.create();
    /**产物*/
    public NonNullList<ItemStack> resultItems = NonNullList.create();
    /**标识*/
    public final static String TAG_RESULT_ITEMS = "resultTags";
    public final static String TAG_PURE_ITEMS = "purificationTags";

    public AbstractPurificationCauldronBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    /**清空所有进度*/
    public void resetAllProgress(){
        this.mainElement = VidaElement.EMPTY;
        this.progress = 0f;
        this.targetProgress = 0f;
        this.totalProgress = 0f;
        this.isInProgress = false;
        this.purificationItems.clear();
        this.resultItems.clear();
    }

    /**获取最前面的结果*/
    public ItemStack popResultItem(){
        return resultItems.remove(0);
    }

    /**一次子任务最大完成量*/
    public float getMaxSubTaskProgress(){
        return this.targetProgress;
    }

    public float getMaxMainTaskProgress(){
        return MAX_TOTAL_PROGRESS;
    }

    /**继续提纯物品*/
    public void continuePurify(){
        this.totalProgress += step;
        this.progress += step;
        if(this.progress >= getMaxSubTaskProgress()){
            completeSubTask();
        }
    }


    /**完成*/
    public boolean checkComplete(){
        if(this.totalProgress >= getMaxMainTaskProgress()){
            return true;
        }
        return false;
    }


    /**当物品提纯完毕，完成一次提纯任务*/
    public void completeSubTask(){
        this.purificationItems.remove(0);
        this.targetProgress = 0f;
        this.progress = 0f;
        this.step = 1.0f;
        this.isInProgress = false;
    }

    /**生成物品*/
    public void generateResult(){

    }

    /**当锅被填满，完成整个提纯任务*/
    public void completeMainTask(){

    }



    /**保存结果*/
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putFloat("progress", progress);
        pTag.putFloat("targetProgress", targetProgress);
        pTag.putFloat("totalProgress", totalProgress);
        pTag.putFloat("step", step);
        pTag.putBoolean("isInProgress", isInProgress);
        pTag.putString("mainElement", mainElement.name());
        // 保存结果物品
        CompoundTag resultTags = new CompoundTag();
        if(resultItems.size() > 0){
            ContainerHelper.saveAllItems(resultTags, resultItems);
        }
        pTag.put(TAG_RESULT_ITEMS, resultTags);
        // 保存正在烧制物品
        CompoundTag purificationTags = new CompoundTag();
        if(purificationItems.size() > 0){
            ContainerHelper.saveAllItems(purificationTags, purificationItems);
        }
        pTag.put(TAG_PURE_ITEMS, purificationTags);
    }


    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.progress = pTag.getFloat("progress");
        this.isInProgress = pTag.getBoolean("isInProgress");
        this.targetProgress = pTag.getFloat("targetProgress");
        this.totalProgress = pTag.getFloat("totalProgress");
        this.step = pTag.getFloat("step");
        this.mainElement = VidaElement.valueOf(pTag.getString("mainElement"));
        // 获取结果
        this.resultItems = NonNullList.create();
        if(pTag.contains(TAG_RESULT_ITEMS)){
            ContainerHelper.loadAllItems(pTag.getCompound(TAG_RESULT_ITEMS), this.resultItems);
        }
        // 获取正在烧制物品
        this.purificationItems = NonNullList.create();
        if(pTag.contains(TAG_PURE_ITEMS)){
            ContainerHelper.loadAllItems(pTag.getCompound(TAG_PURE_ITEMS), this.purificationItems);
        }
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, AbstractPurificationCauldronBlockEntity pBlockEntity){
        if(pBlockEntity.isInProgress){
            pBlockEntity.continuePurify();
        }

        if(pBlockEntity.checkComplete()){
            pBlockEntity.generateResult();
            pBlockEntity.completeMainTask();
        }
    }


}
