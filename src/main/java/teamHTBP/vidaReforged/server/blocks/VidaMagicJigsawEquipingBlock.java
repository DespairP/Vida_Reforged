package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.blockEntities.VidaMagicJigsawEquipingBlockEntity;
import teamHTBP.vidaReforged.server.menu.MagicJigsawMenu;
import teamHTBP.vidaReforged.server.menu.TimeElementCraftingTableMenuContainer;

import java.util.function.Supplier;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.server.menu.TimeElementCraftingTableMenuContainer.MENU_NAME;

public class VidaMagicJigsawEquipingBlock extends VidaBaseEntityBlock<VidaMagicJigsawEquipingBlockEntity> {
    public VidaMagicJigsawEquipingBlock() {
        super(Properties.of(), VidaBlockEntityLoader.JIGSAW_EQUIP);
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(pLevel, pPos, pPlayer);
            return InteractionResult.CONSUME;
        }
    }

    protected void openContainer(Level pLevel, BlockPos pPos, Player pPlayer) {
        NetworkHooks.openScreen(
                (ServerPlayer) pPlayer,
                new SimpleMenuProvider(
                        (pContainerId,pPlayerInventory,pPlayerI) -> new MagicJigsawMenu(pContainerId, ContainerLevelAccess.create(pLevel, pPos), pPlayerI.getInventory(), pPos),
                        Component.translatable(String.format("%s:%s", MOD_ID, MENU_NAME))
                ),
                (packerBuffer) -> {
                    packerBuffer.writeBlockPos(pPos);
                    packerBuffer.writeBlockPos(pPos);
                });
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return super.getMenuProvider(pState, pLevel, pPos);
    }
}
