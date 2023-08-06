package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicWordCapability;
import teamHTBP.vidaReforged.core.common.blockentity.VidaBaseEntityBlock;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.menu.MagicWordCraftingTableMenu;
import teamHTBP.vidaReforged.server.menu.TimeElementCraftingTableMenuContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.server.menu.TimeElementCraftingTableMenuContainer.MENU_NAME;

public class MagicWordCraftingTable extends VidaBaseEntityBlock {

    public static VoxelShape SHAPE = Shapes.empty();

    static {
        SHAPE = Shapes.join(SHAPE, Shapes.box(0, 0.4375, 0, 1, 0.8125, 1), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.75, 0, 0.75, 1, 0.25, 1), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.75, 0, 0, 1, 0.25, 0.25), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0, 0, 0, 0.25, 0.25, 0.25), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0, 0, 0.75, 0.25, 0.25, 1), BooleanOp.OR);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    public MagicWordCraftingTable() {
        super(Properties.of().noOcclusion(), VidaBlockEntityLoader.MAGIC_WORD_CRAFTING);
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(pLevel, pPos, pPlayer);
            return InteractionResult.CONSUME;
        }
    }

    protected void openContainer(Level pLevel, BlockPos pPos, Player pPlayer) {
        final List<String> magicWords = new ArrayList<>();
        final LazyOptional<IVidaMagicWordCapability> wordCap = pPlayer.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_WORD);
        final AtomicReference<CompoundTag> tag = new AtomicReference<>(new CompoundTag());
        wordCap.ifPresent(cap -> {
            magicWords.addAll(cap.getAccessibleMagicWord());
            tag.set(cap.serializeNBT());
        });

        NetworkHooks.openScreen(
                (ServerPlayer) pPlayer,
                getMenuProvider(pLevel,pPos,pPlayer,magicWords),
                (packerBuffer) -> {
                    packerBuffer.writeBlockPos(pPos);
                    packerBuffer.writeNbt(tag.get());
                });
    }

    public MenuProvider getMenuProvider(Level pLevel, BlockPos pPos, Player pPlayer, List<String> magicWords){
        return new SimpleMenuProvider(
                (pContainerId,pPlayerInventory,pPlayerInv) -> new MagicWordCraftingTableMenu(
                        pContainerId,
                        ContainerLevelAccess.create(pLevel, pPos),
                        pPlayerInv.getInventory(),
                        pPos,
                        magicWords
                ),
                Component.translatable(String.format("%s:%s", MOD_ID, "magic_word_crafting_table"))
        );
    }

}
