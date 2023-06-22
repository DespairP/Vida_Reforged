package teamHTBP.vidaReforged.core.common.block.templates;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import teamHTBP.vidaReforged.core.common.block.DecoBlockProperties;

import java.util.Optional;

public class DecoDoorBlock extends DoorBlock {

    public DecoDoorBlock(Properties builder, DecoBlockProperties properties) {
        super(
            builder,
            Optional.ofNullable(properties.blockSetType()).orElse(BlockSetType.OAK)
        );
    }
}
