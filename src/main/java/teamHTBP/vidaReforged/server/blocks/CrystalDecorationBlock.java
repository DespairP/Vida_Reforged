package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.common.block.MutiDoubleWithYBlock;
import teamHTBP.vidaReforged.server.blockEntities.CrystalDecorationBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.items.ElementGem;

public class CrystalDecorationBlock extends MutiDoubleWithYBlock implements EntityBlock {
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
