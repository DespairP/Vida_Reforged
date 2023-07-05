package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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

    /**填入水*/
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult result) {
        if(level.isClientSide()){
            return super.use(blockState, level, pos, player, interactionHand, result);
        }


        // 进行交互的一定是要气息核心
        ItemStack handInItem = player.getItemInHand(interactionHand);
        if(!handInItem.is(VidaItemLoader.BREATH_CATCHER.get())){
            return super.use(blockState, level, pos, player, interactionHand, result);
        }

        CollectorBlockEntity blockEntity = (CollectorBlockEntity) level.getBlockEntity(pos);

        if(blockEntity.putItem(handInItem)){
            return InteractionResult.sidedSuccess(true);
        }


        return InteractionResult.PASS;
    }


}
