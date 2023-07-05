package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;

public class VidaOreBlock extends DropExperienceBlock {
    public VidaOreBlock(Properties p_221083_) {
        super(p_221083_, UniformInt.of(0, 2));
    }
}
