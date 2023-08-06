package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.BasePurificationCauldronBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.CollectorBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import java.util.function.Supplier;

public class CollectorBlock extends VidaBaseEntityBlock<CollectorBlockEntity> {
    public CollectorBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion(), VidaBlockEntityLoader.COLLECTOR);
    }

    public static VoxelShape SHAPE = Shapes.empty();

    static {
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.1875, 0.8125), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 0.6875, 0.1875, 0.8125, 0.875, 0.8125), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 0.6875, 0.1875, 0.8125, 0.875, 0.8125), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.3125, 0.1875, 0.3125, 0.6875, 0.6875, 0.6875), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.0625, 0.5625, 0.0625, 0.25, 1, 0.25), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.0625, 0.5625, 0.75, 0.25, 1, 0.9375), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.75, 0.5625, 0.75, 0.9375, 1, 0.9375), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.75, 0.5625, 0.0625, 0.9375, 1, 0.25), BooleanOp.OR);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    /**填入水*/
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult result) {
        if(level.isClientSide()){
            return super.use(blockState, level, pos, player, interactionHand, result);
        }

        ItemStack handInItem = player.getItemInHand(interactionHand);
        CollectorBlockEntity blockEntity = (CollectorBlockEntity) level.getBlockEntity(pos);

        // 如果是shift+右键，把物品取出
        if(player.isShiftKeyDown() && handInItem.isEmpty() && blockEntity.canGetItem()){
            player.getInventory().add(blockEntity.getItem());
            return InteractionResult.sidedSuccess(true);
        }

        // 如果是普通右键，进行交互的一定是要气息核心
        if(!handInItem.is(VidaItemLoader.BREATH_CATCHER.get())){
            return InteractionResult.FAIL;
        }

        if(!player.isShiftKeyDown() && blockEntity.putItem(handInItem)){
            return InteractionResult.sidedSuccess(true);
        }

        return InteractionResult.PASS;
    }



}
