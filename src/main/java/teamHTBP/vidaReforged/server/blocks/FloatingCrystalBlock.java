package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.FloatingCrystalBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;

import java.util.function.Supplier;

public class FloatingCrystalBlock extends VidaBaseEntityBlock<FloatingCrystalBlockEntity> {
    private VidaElement crystalElement = VidaElement.EMPTY;
    public final VoxelShape SHAPE = Block.box(6, 6, 6, 12, 12, 12);

    public FloatingCrystalBlock(VidaElement element) {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion(), null);
        this.crystalElement = element;
    }

    @Override
    public @Nullable BlockEntityTicker getTicker(Level pLevel, BlockState pState, BlockEntityType pBlockEntityType) {
        return ( level, pos, state, entity ) -> ((FloatingCrystalBlockEntity)entity).doServerTick(level,pos,state,entity);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        FloatingCrystalBlockEntity entity = VidaBlockEntityLoader.FLOATING_CRYSTAL.get().create(pPos, pState);
        entity.setElement(crystalElement);
        return entity;
    }
}
