package teamHTBP.vidaReforged.server.blocks;


import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.VaseBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;

import java.util.function.Supplier;

/**花瓶*/
public class VaseBlock extends VidaBaseEntityBlock<VaseBlockEntity> {
    private static VoxelShape shape = Shapes.empty();

    static {
        shape = Shapes.join(shape, Shapes.box(0.21875, 0, 0.21875, 0.78125, 0.5625, 0.78125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.34375, 0.5625, 0.34375, 0.65625, 0.6875, 0.65625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.28125, 0.6875, 0.28125, 0.71875, 0.8125, 0.71875), BooleanOp.OR);
    }

    public VaseBlock(Properties properties) {
        super(properties, VidaBlockEntityLoader.VASE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        return shape;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        ItemStack handInItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if(!level.isClientSide && blockEntity instanceof VaseBlockEntity vaseBlockEntity && handInItem.is(ItemTags.FLOWERS)){
            if(vaseBlockEntity.putFlower(handInItem) && !player.getAbilities().instabuild){
                handInItem.shrink(1);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        return super.use(state, level, pos, player, interactionHand, hitResult);
    }
}
