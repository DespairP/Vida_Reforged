package teamHTBP.vidaReforged.server.blocks;


import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;

import java.util.function.Supplier;

/**花瓶*/
public class VaseBlock extends VidaBaseEntityBlock<BlockEntity> {

    public VaseBlock(Properties properties, Supplier<BlockEntityType<BlockEntity>> supplier) {
        super(properties, supplier);
    }
}
