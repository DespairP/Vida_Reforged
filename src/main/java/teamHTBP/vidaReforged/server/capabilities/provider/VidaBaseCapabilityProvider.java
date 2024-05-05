package teamHTBP.vidaReforged.server.capabilities.provider;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class VidaBaseCapabilityProvider<T extends INBTSerializable<CompoundTag>> implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    protected T capability;
    protected Capability<T> capToken;

    private CapabilityProvider<T> provider;


    public VidaBaseCapabilityProvider(Capability<T> capability){
        this.capToken = capability;
    }

    public VidaBaseCapabilityProvider(Capability<T> capability, CapabilityProvider<T> provider){
        this.capToken = capability;
        this.provider = provider;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == capToken && provider != null){
            return LazyOptional.of(this::getOrCreateCapability).cast();
        }
        return cap == capToken ?
                LazyOptional.of(this::getOrCreateCapability).cast() :
                LazyOptional.empty();
    }

    /**获取cap*/
    public T getOrCreateCapability(){
        if(capability == null){
            capability = provider.createCapability();
        }
        return capability;
    }

    @Override
    public CompoundTag serializeNBT() {
        return getOrCreateCapability().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getOrCreateCapability().deserializeNBT(nbt);
    }


    public interface CapabilityProvider<T> {
        public T createCapability();
    }
}
