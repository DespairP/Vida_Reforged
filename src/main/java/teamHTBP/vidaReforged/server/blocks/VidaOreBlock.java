package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import teamHTBP.vidaReforged.core.api.VidaElement;

public class VidaOreBlock extends DropExperienceBlock {
    public VidaOreBlock(VidaElement element) {
        super(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE), UniformInt.of(0, 2));
    }
}
