package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;

public class MagicWordCraftingTableBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    public MagicWordCraftingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.MAGIC_WORD_CRAFTING.get(), pPos, pBlockState);
    }

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {

    }
}
