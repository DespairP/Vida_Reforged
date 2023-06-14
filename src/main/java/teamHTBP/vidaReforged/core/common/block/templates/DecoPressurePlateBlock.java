package teamHTBP.vidaReforged.core.common.block.templates;

import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import teamHTBP.vidaReforged.core.common.block.DecoBlockProperties;

import java.util.Optional;

public class DecoPressurePlateBlock extends PressurePlateBlock {
    public DecoPressurePlateBlock(Properties properties, DecoBlockProperties decoBlockProperties) {
        super(
            Sensitivity.MOBS,
            properties,
            Optional.ofNullable(decoBlockProperties.blockSetType()).orElse(BlockSetType.f_271198_)
        );
    }
}
