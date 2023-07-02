package teamHTBP.vidaReforged.core.api.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public interface IVidaTickableBlockEntity {
    void doServerTick(Level pLevel, BlockPos pPos, BlockState pState,BlockEntity entity);

    static <T extends BlockEntity> BlockEntityTicker<T> getTicker() {
        return (pLevel, pPos, pState, pBlockEntity) -> ((IVidaTickableBlockEntity) pBlockEntity).doServerTick(pLevel, pPos, pState, pBlockEntity);
    }
}
