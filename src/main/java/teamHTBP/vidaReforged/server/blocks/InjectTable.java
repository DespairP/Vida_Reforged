package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.CrystalDecorationBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.InjectTableBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.items.ElementGem;

import java.util.function.Supplier;

public class InjectTable extends VidaBaseEntityBlock<InjectTableBlockEntity> {
    public InjectTable() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion(), VidaBlockEntityLoader.INJECT_TABLE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if(level.isClientSide){
            return InteractionResult.PASS;
        }
        ItemStack handInItem = player.getItemInHand(hand);
        InjectTableBlockEntity entity = (InjectTableBlockEntity) level.getBlockEntity(pos);
        boolean entityHasItem = entity != null && entity.hasItem();

        // 获取在注入台内的item
        if(entityHasItem && player.isShiftKeyDown()){
            player.getInventory().add(entity.getItem());
            entity.setUpdated();
            return InteractionResult.SUCCESS;
        }

        // 如果实例不存在，不参与逻辑
        if(entity == null){
            return InteractionResult.FAIL;
        }

        // 如果不是能放入的东西，跳过
        if(entityHasItem || !handInItem.is(itemHolder -> itemHolder.get() instanceof TieredItem)){
            return InteractionResult.FAIL;
        }

        // 放入物品
        entity.putItem(handInItem.copyWithCount(1));
        handInItem.shrink(1);
        entity.setUpdated();
        return InteractionResult.CONSUME;
    }
}
