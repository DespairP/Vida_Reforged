package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.DistExecutor;
import teamHTBP.vidaReforged.client.screen.TeaconGuideBookScreen;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.TeaconGuideBookBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;

import java.util.function.Supplier;

/**
 *
 * */
public class TeaconGuideBookBlock extends VidaBaseEntityBlock<TeaconGuideBookBlockEntity> {
    private String bookId = "";

    public TeaconGuideBookBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion().noCollission(), VidaBlockEntityLoader.TEACON_GUIDEBOOK);
    }


    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            this.openScreen();
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void openScreen(){
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft.getInstance().setScreen(new TeaconGuideBookScreen(bookId));
        });
    }
}
