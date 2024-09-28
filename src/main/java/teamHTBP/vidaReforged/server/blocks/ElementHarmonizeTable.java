package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.core.common.block.MutiDoubleBlock;
import teamHTBP.vidaReforged.server.blockEntities.ElementHarmonizeTableBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;

import java.util.Optional;
import java.util.function.Supplier;

public class ElementHarmonizeTable extends MutiDoubleBlock implements EntityBlock {
    @NotNull final VidaElement element;
    @NotNull Supplier<BlockEntityType<ElementHarmonizeTableBlockEntity>> entityTypeSupplier;
    final static Logger LOGGER = LogManager.getLogger();

    public ElementHarmonizeTable(Properties properties, VidaElement element) {
        super(properties);
        this.element = element;
        this.entityTypeSupplier = VidaBlockEntityLoader.ELEMENT_HARMONIZE_TABLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? entityTypeSupplier.get().create(pos, state) : null;
    }

    public VidaElement getElement() {
        return element;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult result) {
        try {
            if (!level.isClientSide) {
                Optional<ElementHarmonizeTableBlockEntity> blockEntityOptional = level.getBlockEntity(state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below(), entityTypeSupplier.get());
                if(!blockEntityOptional.isPresent()){
                    return InteractionResult.PASS;
                }
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                ElementHarmonizeTableBlockEntity blockEntity = blockEntityOptional.get();
                if (!player.isShiftKeyDown() && blockEntity.setItem(stack.copyWithCount(1))) {
                    stack.shrink(1);
                    blockEntity.setUpdated();
                    return InteractionResult.sidedSuccess(true);
                }
                if (player.isShiftKeyDown() && player.addItem(blockEntity.getItem())){
                    blockEntity.setUpdated();
                    return InteractionResult.sidedSuccess(true);
                }
            }
        } catch (Exception exception){
            LOGGER.error(exception);
        }
        return super.use(state, level, pos, player, interactionHand, result);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return entityType == entityTypeSupplier.get() ? IVidaTickableBlockEntity.getTicker() : null;
    }
}
