package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class VidaSapling extends SaplingBlock {

    public VidaSapling(AbstractTreeGrower pTreeGrower, Properties pProperties) {
        super(pTreeGrower, BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS));
    }


}
