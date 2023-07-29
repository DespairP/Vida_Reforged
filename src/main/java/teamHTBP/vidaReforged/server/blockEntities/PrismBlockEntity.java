package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

public class PrismBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    private SimpleContainer inputAndResult = new SimpleContainer(ItemStack.EMPTY, ItemStack.EMPTY);
    private RandomSource random = RandomSource.create();
    private int rotateRad$0 = 0;
    private int rotateRad$1 = 0;
    private boolean isGenerateFire = false;
    private boolean isProcess = false;
    private int clientRad$0 = 0;
    private int clientRad$1 = 0;

    public PrismBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.PRISM.get(), pPos, pBlockState);
    }

    /**随机生成位置*/
    public void generateFire(){
        this.rotateRad$0 = random.nextInt(360);
        this.rotateRad$1 = random.nextInt(360);
        this.isGenerateFire = true;
    }

    public int getRotateRad1() {
        return rotateRad$1;
    }

    public int getRotateRad0() {
        return rotateRad$0;
    }

    public boolean hasItemInside(){
        return !this.inputAndResult.getItem(0).isEmpty();
    }

    public boolean hasResultItem(){
        return !this.inputAndResult.getItem(1).isEmpty();
    }

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {
        if(pLevel.isClientSide){
            return;
        }
        if(!hasItemInside()){
            this.isGenerateFire = false;
            setChanged();
            level.sendBlockUpdated(this.worldPosition, entity.getBlockState(), entity.getBlockState(), 1 | 2);
        }
        if(hasItemInside() && !isGenerateFire){
            generateFire();
            setChanged();
            level.sendBlockUpdated(this.worldPosition, entity.getBlockState(), entity.getBlockState(), 1 | 2);
        }
        if(isProcess){
            this.check();
            setChanged();
            level.sendBlockUpdated(this.worldPosition, entity.getBlockState(), entity.getBlockState(), 1 | 2);
        }
    }

    public void check(){
        int offset1 = (int) Math.abs(this.clientRad$0 - this.rotateRad$0);
        int offset2 = (int) Math.abs(this.clientRad$1 - this.rotateRad$1);
        if(offset1 + offset2 >= 30 || hasResultItem()){
            this.isProcess = false;
            return;
        }
        this.inputAndResult.getItem(0).shrink(1);
        this.generateItem();
        this.isProcess = false;
        this.generateFire();
    }

    public void generateItem(){
        CompoundTag tag = new CompoundTag();
        tag.putString("wordId", "vida_reforged:position");
        ItemStack stack = new ItemStack(VidaItemLoader.UNLOCK_MAGIC_WORD_PAPER.get(), 1, tag);
        stack.getOrCreateTag().putString("wordId", "vida_reforged:position");
        this.inputAndResult.setItem(1, stack);
    }

    @Override
    public void load(CompoundTag tag) {
        rotateRad$0 = tag.getInt("rotateRad$0");
        rotateRad$1 = tag.getInt("rotateRad$1");
        isGenerateFire = tag.getBoolean("isGenerateFire");
        this.inputAndResult.setItem(0,ItemStack.of(tag.getCompound("inputAndResult$0")));
        this.inputAndResult.setItem(1,ItemStack.of(tag.getCompound("inputAndResult$1")));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("rotateRad$0", rotateRad$0);
        tag.putInt("rotateRad$1", rotateRad$1);
        tag.putBoolean("isGenerateFire", isGenerateFire);
        tag.put("inputAndResult$0", this.inputAndResult.getItem(0).serializeNBT());
        tag.put("inputAndResult$1", this.inputAndResult.getItem(1).serializeNBT());
    }

    public boolean isGenerateFire() {
        return isGenerateFire;
    }

    public SimpleContainer getInputAndResult(){
        return this.inputAndResult;
    }

    public void setProcess(boolean process) {
        isProcess = process;
        setChanged();
    }

    public void setClientRad$0(int clientRad$0) {
        this.clientRad$0 = clientRad$0;
    }

    public void setClientRad$1(int clientRad$1) {
        this.clientRad$1 = clientRad$1;
    }
}
