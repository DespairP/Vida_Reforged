package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.core.utils.animation.Animator;
import teamHTBP.vidaReforged.core.utils.animation.DestinationAnimator;
import teamHTBP.vidaReforged.core.utils.animation.TimeInterpolator;
import teamHTBP.vidaReforged.core.utils.animation.calculator.IValueProvider;

public class FloatingCrystalBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {

    public long floatingHeight = 0;

    public VidaElement element = VidaElement.EMPTY;

    public FloatingCrystalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.FLOATING_CRYSTAL.get(), pPos, pBlockState);
    }

    public void setElement(VidaElement element) {
        this.element = element;
        this.setChanged();
    }

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {
        if(!pLevel.isClientSide){
            return;
        }
        this.floatingHeight += 1;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        if(element != null){
            tag.putString("element", this.element.toString());
        }
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        if(tag.contains("element")){
            this.element = VidaElement.of(tag.getString("element"));
        }
        super.load(tag);
    }
}
