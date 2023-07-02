package teamHTBP.vidaReforged.core.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

/**
 * BlockEntity提供者
 * @author DustW
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class VidaBaseEntityBlock<I extends BlockEntity> extends Block implements EntityBlock {
    protected @NotNull Supplier<BlockEntityType<I>> entityTypeSupplier;

    public VidaBaseEntityBlock(Properties properties, Supplier<BlockEntityType<I>> supplier) {
        super(properties);
        this.entityTypeSupplier = supplier;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == entityTypeSupplier.get() ? IVidaTickableBlockEntity.getTicker() : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return entityTypeSupplier.get().create(pPos, pState);
    }
}
