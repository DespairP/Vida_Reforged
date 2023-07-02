package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
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

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (worldIn.isClientSide) {
            return;
        }

        if (entityIn instanceof ItemEntity itemEntity) {
            BasePurificationCauldronBlockEntity entity = (BasePurificationCauldronBlockEntity) worldIn.getBlockEntity(pos);
            ItemStack itemStack = itemEntity.getItem();
            if (entity != null && entity.addItem(itemStack)) {
                entityIn.discard();
                worldIn.sendBlockUpdated(pos, state, state, Block.UPDATE_NEIGHBORS);
            }
        }

        super.entityInside(state, worldIn, pos, entityIn);
    }


}
