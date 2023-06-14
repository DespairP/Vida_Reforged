package teamHTBP.vidaReforged.core.common.block.templates;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import teamHTBP.vidaReforged.core.common.block.DecoBlockProperties;

import java.util.Optional;

public class DecoTrapDoorBlock extends TrapDoorBlock {
    public DecoTrapDoorBlock(Properties properties, DecoBlockProperties decoBlockProperties) {
        super(
            properties,
            Optional.ofNullable(decoBlockProperties.blockSetType()).orElse(BlockSetType.f_271198_)
        );
    }
}
