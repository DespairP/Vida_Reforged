package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.BasePurificationCauldronBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;

public class PurificationCauldron extends VidaBaseEntityBlock<BasePurificationCauldronBlockEntity> implements EntityBlock {
    private static final VoxelShape SHAPE;

    static {
        VoxelShape aPillar = Block.box(0.25, 2, 0.25, 2.25, 12, 2.25);//↖
        VoxelShape aBase = Block.box(0, 0, 0, 3, 3, 3);//↖基底
        VoxelShape upBase = Block.box(2, 1.75, 1, 14, 10.75, 2);
        VoxelShape bPillar = Block.box(13.75, 2, 0.25, 15.75, 12, 2.25);//↗
        VoxelShape bBase = Block.box(13, 0, 0, 16, 3, 3);//↗基底
        VoxelShape rightBase = Block.box(14, 1.75, 2, 15, 10.75, 14);
        VoxelShape cPillar = Block.box(13.75, 2, 13.75, 15.75, 12, 15.75);//↘
        VoxelShape cBase = Block.box(13, 0, 13, 16, 3, 16);//↘基底
        VoxelShape downBase = Block.box(2, 1.75, 14, 14, 10.75, 15);
        VoxelShape dPillar = Block.box(0.25, 2, 13.75, 2.25, 12, 15.75);//↙
        VoxelShape dBase = Block.box(0, 0, 13, 3, 3, 16);//↙基底
        VoxelShape leftBase = Block.box(1, 1.75, 2, 2, 10.75, 14);
        VoxelShape base = Block.box(1, 0.75, 1, 15, 1.75, 15);
        SHAPE = Shapes.or(aPillar, aBase, upBase, bPillar, bBase, rightBase, cPillar, cBase, downBase, dPillar, dBase, leftBase, base);
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
        if(level.isClientSide()){
            return super.use(blockState, level, pos, player, interactionHand, result);
        }


        // 进行交互的一定是要水桶
        ItemStack handInItem = player.getItemInHand(interactionHand);
        if(handInItem.getItem() != Items.WATER_BUCKET){
            return super.use(blockState, level, pos, player, interactionHand, result);
        }

        BasePurificationCauldronBlockEntity blockEntity = (BasePurificationCauldronBlockEntity) level.getBlockEntity(pos);
        if(!blockEntity.isWaterFilled && !blockEntity.isInProgress()){
            blockEntity.fillWater();

            //
            if(interactionHand == InteractionHand.MAIN_HAND){
                player.getInventory().removeItem(player.getInventory().selected, 1);
                player.getInventory().add(player.getInventory().selected,new ItemStack(Items.BUCKET));
            }else{
                player.getInventory().offhand.clear();
                player.getInventory().offhand.add(new ItemStack(Items.BUCKET));
            }


        }
        return InteractionResult.PASS;
    }
}
