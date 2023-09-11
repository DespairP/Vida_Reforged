package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.blockEntities.VidaWandCraftingTableBlockEntity;

import java.util.function.Supplier;

public class VidaWandCraftingTable extends VidaBaseEntityBlock<VidaWandCraftingTableBlockEntity> {
    public VidaWandCraftingTable() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), VidaBlockEntityLoader.VIDA_WAND_CRAFTING_TABLE);
    }
}
