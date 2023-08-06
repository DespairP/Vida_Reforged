package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;

public class GlowingLightBlockEntity extends BlockEntity implements IVidaTickableBlockEntity {
    public VidaElement element = VidaElement.EMPTY;


    public GlowingLightBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.GLOWING_LIGHT.get(), pPos, pBlockState);
    }

    public void setElement(VidaElement element) {
        this.element = element;
        this.setChanged();
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

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {

    }
}
