package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.server.menu.VividChestBlockMenu;

public class VividBlockChestEntity extends RandomizableContainerBlockEntity implements LidBlockEntity, IVidaTickableBlockEntity {
    private final ChestLidController chestLidController = new ChestLidController();
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            VividBlockChestEntity.playSound(level, pos, state, SoundEvents.CHEST_OPEN);
        }

        protected void onClose(Level level, BlockPos pos, BlockState state) {
            VividBlockChestEntity.playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
        }

        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int p_155364_, int p_155365_) {
            VividBlockChestEntity.this.signalOpenCount(level, pos, state, p_155364_, p_155365_);
        }

        protected boolean isOwnContainer(Player player) {
            return true;
        }
    };


    public VividBlockChestEntity(BlockPos pos, BlockState state) {
        super(VidaBlockEntityLoader.VIVID_CHEST.get(), pos, state);
    }


    static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent soundEvent) {
        RandomSource randomSource = RandomSource.create();
        level.playSound((Player)null, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, soundEvent, SoundSource.BLOCKS, 0.5F, randomSource.nextFloat() * 0.1F + 0.9F);
    }

    public void startOpen(Player p_59120_) {
        if (!this.remove && !p_59120_.isSpectator()) {
            this.openersCounter.incrementOpeners(p_59120_, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void stopOpen(Player p_59118_) {
        if (!this.remove && !p_59118_.isSpectator()) {
            this.openersCounter.decrementOpeners(p_59118_, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    protected void signalOpenCount(Level p_155333_, BlockPos p_155334_, BlockState p_155335_, int p_155336_, int p_155337_) {
        Block block = p_155335_.getBlock();
        p_155333_.blockEvent(p_155334_, block, 1, p_155337_);
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }

    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items);
        }

    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> newItems) {
        this.items = newItems;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.vivid_chest");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new VividChestBlockMenu(containerId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public float getOpenNess(float partialTicks) {
        return this.chestLidController.getOpenness(partialTicks);
    }

    public boolean triggerEvent(int p_59285_, int p_59286_) {
        if (p_59285_ == 1) {
            this.chestLidController.shouldBeOpen(p_59286_ > 0);
            return true;
        } else {
            return super.triggerEvent(p_59285_, p_59286_);
        }
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {
        if(pLevel.isClientSide){
            this.chestLidController.tickLid();
            return;
        }
    }
}
