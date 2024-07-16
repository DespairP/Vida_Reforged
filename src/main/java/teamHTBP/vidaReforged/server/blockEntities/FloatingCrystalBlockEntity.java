package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.utils.animation.Animator;
import teamHTBP.vidaReforged.core.utils.animation.DestinationAnimator;
import teamHTBP.vidaReforged.core.utils.animation.TimeInterpolator;
import teamHTBP.vidaReforged.core.utils.animation.calculator.IValueProvider;
import teamHTBP.vidaReforged.server.blocks.FloatingCrystalBlock;
import teamHTBP.vidaReforged.server.capabilities.VidaManaCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

import java.util.List;

public class FloatingCrystalBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {

    public IVidaManaCapability capability;
    public long floatingHeight = 0;

    public VidaElement element = VidaElement.EMPTY;

    public FloatingCrystalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.FLOATING_CRYSTAL.get(), pPos, pBlockState);
    }

    public VidaElement getElement() {
        return element;
    }

    public void setElement(VidaElement element) {
        this.element = element;
        getOrCreateCapability().setStrictElements(List.of(element));
        this.setChanged();
    }

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {
        if(!pLevel.isClientSide){
            return;
        }
        this.floatingHeight += 1;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compound = super.getUpdateTag();
        this.saveAdditional(compound);
        return compound;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        if(element != null){
            tag.putString("element", this.element.toString());
        }
        tag.put("cap",  getOrCreateCapability().serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        if(tag.contains("element")){
            this.element = VidaElement.of(tag.getString("element"));
        }
        getOrCreateCapability().deserializeNBT(tag.getCompound("cap"));
        super.load(tag);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == VidaCapabilityRegisterHandler.VIDA_MANA){
            return LazyOptional.of(this::getOrCreateCapability).cast();
        }
        return LazyOptional.empty();
    }

    /**获取cap*/
    public IVidaManaCapability getOrCreateCapability(){
        if(this.capability == null){
            this.capability = new VidaManaCapability(5000, true, List.of(element));
        }
        return this.capability;
    }

    public void setUpdated(){
        setChanged();
        level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 1 | 2);
    }

    public void addPotential(float potential){
        getOrCreateCapability().addMana(element, potential);
        setUpdated();
    }
}
