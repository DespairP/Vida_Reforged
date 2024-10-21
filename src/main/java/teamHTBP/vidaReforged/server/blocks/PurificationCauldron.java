package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.BasePurificationCauldronBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

public class PurificationCauldron extends VidaBaseEntityBlock<BasePurificationCauldronBlockEntity> implements EntityBlock {
    private static VoxelShape SHAPE = Shapes.empty();

    static {
        SHAPE = Shapes.join(SHAPE, Shapes.box(0, 0, 0, 0.1875, 0.875, 0.1875), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.8125, 0, 0, 1, 0.875, 0.1875), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.8125, 0, 0.8125, 1, 0.875, 1), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0, 0, 0.8125, 0.1875, 0.875, 1), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.0625, 0.125, 0.0625, 0.9375, 0.25, 0.9375), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.8125, 0.25, 0.1875, 0.9375, 0.8125, 0.8125), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 0.25, 0.0625, 0.8125, 0.8125, 0.1875), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.0625, 0.25, 0.1875, 0.1875, 0.8125, 0.8125), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 0.25, 0.8125, 0.8125, 0.8125, 0.9375), BooleanOp.OR);
    }


    public PurificationCauldron() {
        super(BlockBehaviour.Properties.of().noOcclusion(), VidaBlockEntityLoader.PURIFICATION_CAULDRON);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    /**当玩家抛入物品时*/
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) {
        if (level.isClientSide()) {
            return;
        }

        // 抛入物品判定
        if (entityIn instanceof ItemEntity dropItemEntity) {
            BasePurificationCauldronBlockEntity blockEntity = (BasePurificationCauldronBlockEntity) level.getBlockEntity(pos);
            ItemStack itemStack = dropItemEntity.getItem();
            if (blockEntity != null && blockEntity.addItem(itemStack)) {
                dropItemEntity.discard();
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_NEIGHBORS);
            }
        }

        super.entityInside(state, level, pos, entityIn);
    }

    /**填入水*/
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult result) {
        if(!level.isClientSide){
            // 填充水
            emptyBucket(blockState, level, pos, player, interactionHand);
            // 物品交互
            tryAddItem(blockState, level, pos, player, interactionHand);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public void tryAddItem(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand){
        ItemStack handInItem = player.getItemInHand(hand);
        BasePurificationCauldronBlockEntity blockEntity = (BasePurificationCauldronBlockEntity) level.getBlockEntity(pos);
        if (blockEntity != null && blockEntity.addItem(handInItem.copy())) {
            if(!player.getAbilities().instabuild){
                handInItem.setCount(0);
            }
            level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
            level.playSound((Player)null, player, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 0.5F);
        }
    }

    public void emptyBucket(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand){
        ItemStack handInItem = player.getItemInHand(hand);
        BasePurificationCauldronBlockEntity blockEntity = (BasePurificationCauldronBlockEntity) level.getBlockEntity(pos);
        if(handInItem.is(VidaItemLoader.VIVID_BUCKET.get()) && blockEntity != null && !blockEntity.isWaterFilled && !blockEntity.isInProgress()){
            blockEntity.fillWater();
            level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
            level.playSound((Player)null, player, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            if(!player.getAbilities().instabuild){
                player.setItemInHand(hand, ItemUtils.createFilledResult(handInItem, player, new ItemStack(Items.BUCKET)));
            }
        }
    }
}
