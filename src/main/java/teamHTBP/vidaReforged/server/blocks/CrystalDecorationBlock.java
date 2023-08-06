package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.common.block.MutiDoubleWithYBlock;
import teamHTBP.vidaReforged.server.blockEntities.CrystalDecorationBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.items.ElementGem;

public class CrystalDecorationBlock extends MutiDoubleWithYBlock implements EntityBlock {

    public static VoxelShape SHAPE = Shapes.empty();

    static {
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.125, 0, 0.125, 0.875, 0.25, 0.875), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.25, 0.25, 0.25, 0.75, 0.6875, 0.75), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 0.6875, 0.1875, 0.8125, 0.8125, 0.8125), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 1.4375, 0.1875, 0.8125, 1.5625, 0.8125), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.21875, 0.8125, 0.21875, 0.78125, 1.4375, 0.78125), BooleanOp.OR);
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? SHAPE.move(0,-1,0) : SHAPE;
    }

    public CrystalDecorationBlock() {
        super(Properties.copy(Blocks.OAK_WOOD));
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if(level.isClientSide){
            return InteractionResult.PASS;
        }
        ItemStack handInItem = player.getItemInHand(hand);
        CrystalDecorationBlockEntity entity = getEntity(level, pos, state);

        // 如果有物品，取出物品
        if(entity.hasItem() && player.isShiftKeyDown()) {
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), entity.getItemStack()));
            return InteractionResult.PASS;
        }

        //
        if(entity.hasItem() || !handInItem.is(itemHolder -> itemHolder.get() instanceof ElementGem)){
            return InteractionResult.PASS;
        }

        entity.putItem(handInItem.copyWithCount(1));
        handInItem.shrink(1);
        entity.setUpdated();
        return InteractionResult.SUCCESS;
    }

    private CrystalDecorationBlockEntity getEntity(Level level, BlockPos pos, BlockState state){
        if(state.getValue(HALF) == DoubleBlockHalf.LOWER){
            return (CrystalDecorationBlockEntity) level.getBlockEntity(pos);
        }
        return (CrystalDecorationBlockEntity) level.getBlockEntity(pos.below());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if(state.getValue(HALF) == DoubleBlockHalf.LOWER){
            return VidaBlockEntityLoader.GEM_SHELF.get().create(pos, state);
        }
        return null;
    }
}
