package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.FloatingCrystalBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.GlowingLightBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;

public class GlowingLightBlock extends VidaBaseEntityBlock<GlowingLightBlockEntity> {
    VidaElement element = VidaElement.EMPTY;
    public final VoxelShape SHAPE = Block.box(6, 3, 6, 9, 12, 9);

    public GlowingLightBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS).mapColor(DyeColor.YELLOW).noOcclusion(), VidaBlockEntityLoader.GLOWING_LIGHT);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    public GlowingLightBlock(VidaElement element) {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS).mapColor(DyeColor.YELLOW).noOcclusion(), VidaBlockEntityLoader.GLOWING_LIGHT);
        this.element = element;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        GlowingLightBlockEntity entity = VidaBlockEntityLoader.GLOWING_LIGHT.get().create(pPos, pState);
        entity.setElement(element);
        return entity;
    }
}
