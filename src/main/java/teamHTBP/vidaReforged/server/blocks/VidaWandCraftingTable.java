package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.blockEntities.VidaWandCraftingTableBlockEntity;
import teamHTBP.vidaReforged.server.menu.VidaWandCraftingTableMenu;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.server.menu.VidaWandCraftingTableMenu.MENU_NAME;

public class VidaWandCraftingTable extends VidaBaseEntityBlock<VidaWandCraftingTableBlockEntity> {
    public VidaWandCraftingTable() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), VidaBlockEntityLoader.VIDA_WAND_CRAFTING_TABLE);
    }


    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            this.openContainer(pLevel, pPos, pPlayer);
        }
        return InteractionResult.SUCCESS;
    }

    protected void openContainer(Level pLevel, BlockPos pPos, Player pPlayer) {
        NetworkHooks.openScreen(
                (ServerPlayer) pPlayer,
                new SimpleMenuProvider(
                        (pContainerId, pPlayerInventory, pPlayerI) ->
                                new VidaWandCraftingTableMenu(pContainerId, pPlayerI.getInventory(), pPos),
                        Component.translatable(String.format("%s:%s", MOD_ID, MENU_NAME))
                ),
                (packerBuffer) -> {
                    packerBuffer.writeBlockPos(pPos);
                });
    }

}
