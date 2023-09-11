package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;

public class VidaWandCraftingTableBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    public VidaWandCraftingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.VIDA_WAND_CRAFTING_TABLE.get(), pPos, pBlockState);
    }

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {

    }
}
