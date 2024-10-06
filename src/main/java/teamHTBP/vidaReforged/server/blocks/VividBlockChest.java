package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.blockEntities.VividBlockChestEntity;
import teamHTBP.vidaReforged.server.menu.PrismMenu;

import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.server.menu.PrismMenu.MENU_NAME;

public class VividBlockChest extends AbstractChestBlock<VividBlockChestEntity> {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);


    protected VividBlockChest(Properties properties) {
        super(properties, VidaBlockEntityLoader.VIVID_CHEST::get);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean identify) {
        return DoubleBlockCombiner.Combiner::acceptNone;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(WATERLOGGED);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VividBlockChestEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult result) {
        if(!level.isClientSide){
            openContainer(state, level, pos, player);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.sidedSuccess(true);
    }

    protected void openContainer(BlockState state,Level pLevel, BlockPos pPos, Player pPlayer) {
        NetworkHooks.openScreen(
                (ServerPlayer) pPlayer,
                getMenuProvider(state, pLevel, pPos),
                (packerBuffer) -> {
                    packerBuffer.writeBlockPos(pPos);
                });
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return entityType == blockEntityType.get() ? IVidaTickableBlockEntity.getTicker() : null;
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return super.getMenuProvider(pState, pLevel, pPos);
    }
}
