package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Random;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.core.utils.color.ColorTheme;
import teamHTBP.vidaReforged.server.blocks.ElementHarmonizeTable;

import java.util.ArrayList;
import java.util.List;

public class ElementHarmonizeTableBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    /** 旋转 */
    @OnlyIn(Dist.CLIENT)
    public float spin = 0;
    /** 祭坛元素 */
    private final VidaElement element;
    /** 正在使用的物品 */
    protected SimpleContainer container = new SimpleContainer(1);
    /** 提示物品 */
    protected SimpleContainer tipContainer = new SimpleContainer(1);
    /** 随机函数 */
    protected final RandomSource random;
    /** 是否正在合成 */
    private boolean isProcessing = false;
    /**合成时刻*/
    private int processTick = 0;
    private List<BlockPos> otherTables = new ArrayList<>();

    public ElementHarmonizeTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.ELEMENT_HARMONIZE_TABLE.get(), pPos, pBlockState);
        this.element = ((ElementHarmonizeTable) pBlockState.getBlock()).getElement();
        this.random = RandomSource.create();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        container.fromTag((ListTag) tag.get("container"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("container", container.createTag());
    }

    @Override
    public void doServerTick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
        if(level.isClientSide){
            spawnParticle(level);
            this.spin = (this.spin + 0.05f) % 360;
        }
        if(!level.isClientSide){
            boolean isStructureComplete = false;
            if(element == VidaElement.EARTH){
                isStructureComplete = checkStructureComplete();
            }
            // 如果结构完整，根据实际来开始
            if(element == VidaElement.EARTH && isStructureComplete){
                //
            }
            // 如果结构不完整，停止任何逻辑
            if(element == VidaElement.EARTH && !isStructureComplete){
                abort();
            }

        }
    }

    private boolean checkStructureComplete(){
        otherTables.clear();

        return false;
    }

    private void abort(){
        // 重置
        if(this.isProcessing){
            this.setProcessing(false);
            this.processTick = 0;
        }
    }

    private boolean shouldSpawnParticle(){
        return !getItemWithCopy().isEmpty();
    }

    public boolean isProcessing(){
        return isProcessing;
    }

    public void setProcessing(boolean isProcessing){
        this.isProcessing = isProcessing;
    }

    public ItemStack getItemWithCopy(){
        return this.container.getItem(0).copy();
    }

    public ItemStack getItem(){
        return this.container.getItem(0);
    }

    public boolean setItem(ItemStack stack){
        if(!stack.isEmpty() && getItem().isEmpty() && stack.getCount() >= 1){
            this.container.setItem(0, stack.copyWithCount(1));
            return true;
        }
        return false;
    }

    public void spawnParticle(Level level){
        if(!shouldSpawnParticle()){
            return;
        }
        double x = getBlockPos().getX() + (random.nextFloat() * 0.001F) + 0.5F;
        double y = getBlockPos().getY() + (random.nextFloat() * 0.001F) + 1.3F;
        double z = getBlockPos().getZ() + (random.nextFloat() * 0.001F) + 0.5F;
        level.addParticle(
                getParticle(),
                x,
                y,
                z,
                0.015F * (random.nextFloat() - 0.5f),
                0.015F * (random.nextFloat() - 0.5f),
                0.015F * (random.nextFloat() - 0.5f));
    }

    public ParticleOptions getParticle() {
        return new BaseParticleType(VidaParticleTypeLoader.ORB_PARTICLE.get(), ColorTheme.getColorThemeByElement(element).baseColor().toARGB(), new Vector3f(), 0.2F, 30);
    }
}
