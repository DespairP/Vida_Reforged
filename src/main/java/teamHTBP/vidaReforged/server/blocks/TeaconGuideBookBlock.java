package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.TeaconGuideBookBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;

import java.util.function.Supplier;

/**
 *
 * */
public class TeaconGuideBookBlock extends VidaBaseEntityBlock<TeaconGuideBookBlockEntity> {
    public TeaconGuideBookBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion().noCollission(), VidaBlockEntityLoader.TEACON_GUIDEBOOK);
    }


    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
