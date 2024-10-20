package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.debug.IDebugObj;
import teamHTBP.vidaReforged.core.common.container.BlockEntityHelper;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.providers.ElementPotentialManager;
import teamHTBP.vidaReforged.server.providers.records.ElementPotential;

public abstract class AbstractPurificationCauldronBlockEntity extends BlockEntity implements IDebugObj {
    /**锅中的主要属性，或者是第一个被添加的属性*/
    public VidaElement mainElement = VidaElement.EMPTY;
    /**/
    public boolean isWaterFilled = false;
    /**本次提纯进度*/
    public float progress = 0.0f;
    /**步长*/
    public float step = 1.0f;
    /**本次目标提纯进度*/
    public float targetSubProgress = 0.0f;
    /**总进度*/
    public float totalProgress = 0.0f;
    /**最大进度，如果进度超过最大进度*/
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


    /**获取最前面的结果*/
    public ItemStack popResultItem(){
        return resultItems.remove(0);
    }

    /**清空所有进度*/
    public void resetAllProgress(){
        this.mainElement = VidaElement.EMPTY;
        this.progress = 0f;
        this.targetSubProgress = 0f;
        this.totalProgress = 0f;
        this.isInProgress = false;
        this.isWaterFilled = false;
        this.step = 1.0f;
    }

    /**加入物品*/
    public boolean addItem(ItemStack item){
        // 防止放入空气
        if(item.isEmpty() || item.getItem() == Items.AIR){
            return false;
        }
        if(this.purificationItems.size() > 0){
            return false;
        }
        // 如果没有放入水源，不进行提纯
        if(!isWaterFilled()){
            return false;
        }

        ElementPotential potential = ElementPotentialManager.getPotential(item);
        // 为空或者不匹配,都不加入
        if(potential != null && (mainElement == VidaElement.EMPTY || mainElement == potential.getElement())){
            this.purificationItems.add(item);
            return true;
        }
        return false;
    }

    /**填入水*/
    public boolean fillWater(){
        this.isWaterFilled = true;
        return true;
    }

    public boolean isWaterFilled(){
        return this.isWaterFilled;
    }

    /**强制终止此次提纯任务*/
    public void forceStopSubTask(){
        this.progress = 0f;
        this.targetSubProgress = 0f;
        this.isInProgress = false;
        this.step = 1.0f;
        this.shrinkPurificationItem(1);
    }

    /**一次子任务最大完成量*/
    public float getMaxSubTaskProgress(){
        return this.targetSubProgress;
    }

    /**一次总提纯需要的完成量*/
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

    /**是否正在提炼物品*/
    public boolean isInProgress(){
        return this.isInProgress;
    }

    /**是否总进度完成*/
    public boolean isMainTaskComplete(){
        return this.totalProgress >= getMaxMainTaskProgress();
    }


    /**当物品提纯完毕，完成一次提纯任务*/
    protected void completeSubTask(){
        // 清空现在的进度
        this.targetSubProgress = 0f;
        this.progress = 0f;
        this.step = 1.0f;
        this.isInProgress = false;

        //
        this.shrinkPurificationItem(1);

    }

    /**
     * 减少最前面的提纯物品
     * @param shrinkStep 减少步长
     * */
    protected void shrinkPurificationItem(int shrinkStep){
        if(this.purificationItems.size() == 0){
            return;
        }
        // 检查是否第一个物品stack数量为1
        ItemStack lastItem = this.purificationItems.get(0);
        // 如果>1减去数量,否则移除
        if(lastItem.isStackable() && lastItem.getCount() > shrinkStep){
            lastItem.shrink(1);
            return;
        }
        this.purificationItems.remove(0);
    }

    /**生成物品*/
    public void generateResult(){
        // 如果当前还有东西在提纯，停止提纯，并且生成残留物
        if(this.progress > 0 && this.targetSubProgress > 0 && this.isInProgress()){
            this.isInProgress = false;
            this.shrinkPurificationItem(1);
        }
        // 把剩下未完成提纯的物品移动到结果栏
        if(this.purificationItems.size() > 0){
            for(ItemStack puringItem : this.purificationItems){
                if(!puringItem.isEmpty() && puringItem.getItem() != Items.AIR){
                    level.addFreshEntity(new ItemEntity(level,getBlockPos().getX(),getBlockPos().getY(),getBlockPos().getZ(), puringItem.copy()));
                }
            }
        }
        this.purificationItems.clear();

        // 生成微光
        ItemStack stack = new ItemStack(VidaItemLoader.UNLOCK_MAGIC_WORD_PAPER.get(), 1);
        stack.getOrCreateTag().putString("wordId","vida_reforged:purify");
        level.addFreshEntity(new ItemEntity(level,getBlockPos().getX(),getBlockPos().getY(),getBlockPos().getZ(), stack));

    }

    /**当锅被填满，完成整个提纯任务*/
    protected void completeMainTask(){
        this.resetAllProgress();
    }

    /**开始新的提纯任务*/
    public void startNewSubTask() {
        // 检查是否已经有任务在进行了
        if(this.progress > 0){
            return;
        }
        // 如果没有任务进行，加入新的提纯物品
        ItemStack lastItem = this.purificationItems.size() > 0 ? this.purificationItems.get(0) : ItemStack.EMPTY;
        // 避免空物品，如果检测到，就进行移除
        if(lastItem.getItem() == Items.AIR || lastItem.isEmpty()){
            this.purificationItems.remove(lastItem);
            return;
        }
        // 如果不是空物品，获取最前面物品的potential
        ElementPotential potential = ElementPotentialManager.getPotential(lastItem);
        if(potential == null){
            return;
        }
        // 检查是否之前已经有元素了,如果有和现在的不匹配，不进行提纯
        if(this.mainElement != VidaElement.EMPTY && this.mainElement != potential.element){
            return;
        }
        // 提纯
        this.isInProgress = true;
        this.mainElement = potential.element;
        this.targetSubProgress = potential.energy;
        this.step = 3.0f;
    }



    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
    }

    /**保存结果*/

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putFloat("progress", progress);
        pTag.putFloat("targetSubProgress", targetSubProgress);
        pTag.putFloat("totalProgress", totalProgress);
        pTag.putFloat("step", step);
        pTag.putBoolean("isInProgress", isInProgress);
        pTag.putString("mainElement", mainElement.name());
        pTag.putBoolean("isWaterFilled", isWaterFilled);
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
        super.saveAdditional(pTag);
    }


    @Override
    public void load(CompoundTag pTag) {
        this.progress = pTag.getFloat("progress");
        this.isInProgress = pTag.getBoolean("isInProgress");
        this.targetSubProgress = pTag.getFloat("targetSubProgress");
        this.totalProgress = pTag.getFloat("totalProgress");
        this.step = pTag.getFloat("step");
        this.mainElement = VidaElement.valueOf(pTag.getString("mainElement"));
        this.isWaterFilled = pTag.getBoolean("isWaterFilled");
        // 获取结果
        this.resultItems = NonNullList.create();
        if(pTag.contains(TAG_RESULT_ITEMS)){
            BlockEntityHelper.loadAllItems(pTag.getCompound(TAG_RESULT_ITEMS), this.resultItems);
        }
        // 获取正在烧制物品
        this.purificationItems = NonNullList.create();
        if(pTag.contains(TAG_PURE_ITEMS)){
            BlockEntityHelper.loadAllItems(pTag.getCompound(TAG_PURE_ITEMS), this.purificationItems);
        }
        super.load(pTag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compound = super.getUpdateTag();
        this.saveAdditional(compound);
        return compound;
    }
}
