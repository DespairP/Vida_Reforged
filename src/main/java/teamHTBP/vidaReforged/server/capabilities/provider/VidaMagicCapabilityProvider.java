package teamHTBP.vidaReforged.server.capabilities.provider;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.server.capabilities.VidaMagicContainerCapability;
import teamHTBP.vidaReforged.server.capabilities.VidaManaCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

public class VidaMagicCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private IVidaMagicContainerCapability capability;

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER ?
                LazyOptional.of(this::getOrCreateCapability).cast() :
                LazyOptional.empty();
    }

    /**获取cap*/
    public IVidaMagicContainerCapability getOrCreateCapability(){
        if(this.capability == null){
            this.capability = new VidaMagicContainerCapability();
        }
        return this.capability;
    }

    @Override
    public CompoundTag serializeNBT() {
        return getOrCreateCapability().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getOrCreateCapability().deserializeNBT(nbt);
    }
}
