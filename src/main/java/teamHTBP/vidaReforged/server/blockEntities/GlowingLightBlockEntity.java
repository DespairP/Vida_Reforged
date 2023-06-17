package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GlowingLightBlockEntity extends BlockEntity {
    public GlowingLightBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.GLOWING_LIGHT.get(), pPos, pBlockState);
    }


}
