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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.PrismBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.menu.PrismMenu;
import teamHTBP.vidaReforged.server.menu.TimeElementCraftingTableMenuContainer;

import java.util.function.Supplier;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.server.menu.PrismMenu.MENU_NAME;

public class PrismBlock extends VidaBaseEntityBlock<PrismBlockEntity> {


    public static VoxelShape SHAPE = Shapes.empty();

    static {
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.625, 0.9375), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.0625, 0.625, 0.0625, 0.1875, 0.75, 0.9375), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.8125, 0.625, 0.0625, 0.9375, 0.75, 0.9375), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 0.625, 0.0625, 0.8125, 0.75, 0.1875), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 0.625, 0.8125, 0.8125, 0.75, 0.9375), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 0.71875, 0.1875, 0.8125, 0.71875, 0.8125), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.08125, 0, -0.09375, 0.26875, 0.1875, 0.09375), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.73125, 0, -0.09375, 0.91875, 0.1875, 0.09375), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.73125, 0, 0.90625, 0.91875, 0.1875, 1.09375), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.08125, 0, 0.90625, 0.26875, 0.1875, 1.09375), BooleanOp.OR);
    }

    public PrismBlock() {
        super(Properties.copy(Blocks.CAULDRON).noOcclusion(), VidaBlockEntityLoader.PRISM);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(pLevel, pPos, pPlayer);
            return InteractionResult.SUCCESS;
        }
    }

    protected void openContainer(Level pLevel, BlockPos pPos, Player pPlayer) {
        NetworkHooks.openScreen(
                (ServerPlayer) pPlayer,
                new SimpleMenuProvider(
                        (pContainerId,pPlayerInventory,pPlayerI) -> new PrismMenu(pContainerId, ContainerLevelAccess.create(pLevel, pPos), pPlayerI.getInventory(), pPos),
                        Component.translatable(String.format("%s:%s", MOD_ID, MENU_NAME))
                ),
                (packerBuffer) -> {
                    packerBuffer.writeBlockPos(pPos);
                });
    }


    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return super.getMenuProvider(pState, pLevel, pPos);
    }
}
