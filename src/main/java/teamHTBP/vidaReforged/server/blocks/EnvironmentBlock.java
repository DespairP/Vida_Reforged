package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;

import java.util.function.Supplier;

public class EnvironmentBlock extends VidaBaseEntityBlock<BlockEntity> {
    public EnvironmentBlock(Properties properties, Supplier<BlockEntityType<BlockEntity>> supplier) {
        super(properties, supplier);
    }
}
