package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.PrismBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;

import java.util.function.Supplier;

public class PrismBlock extends VidaBaseEntityBlock<PrismBlockEntity> {
    public PrismBlock() {
        super(Properties.copy(Blocks.CAULDRON).noOcclusion(), VidaBlockEntityLoader.PRISM);
    }


}
