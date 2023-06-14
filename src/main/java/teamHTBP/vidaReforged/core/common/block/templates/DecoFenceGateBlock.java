package teamHTBP.vidaReforged.core.common.block.templates;

import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import teamHTBP.vidaReforged.core.common.block.DecoBlockProperties;

import java.util.Optional;

public class DecoFenceGateBlock extends FenceGateBlock {
    public DecoFenceGateBlock(Properties properties, DecoBlockProperties decoBlockProperties) {
        super(
            properties,
            Optional.ofNullable(decoBlockProperties.woodType()).orElse(WoodType.OAK)
        );
    }
}
