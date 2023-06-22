package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class VidaSapling extends SaplingBlock {

    public VidaSapling(AbstractTreeGrower pTreeGrower, Properties pProperties) {
        super(pTreeGrower, BlockBehaviour.Properties.of().mapColor(DyeColor.GREEN).noCollission().randomTicks().instabreak().sound(SoundType.GRASS));
    }


}
