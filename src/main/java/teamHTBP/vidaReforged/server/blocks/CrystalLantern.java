package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.CrystalLanternBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;

import java.util.function.Supplier;

public class CrystalLantern extends VidaBaseEntityBlock<CrystalLanternBlockEntity> {
    public CrystalLantern() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().lightLevel((state) -> 10), VidaBlockEntityLoader.CRYSTAL_LANTERN);
    }
}
