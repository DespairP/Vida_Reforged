package teamHTBP.vidaReforged.server.capabilities.provider;

import net.minecraft.nbt.CompoundTag;
import teamHTBP.vidaReforged.core.api.capability.IVidaChunkCrystalCapability;
import teamHTBP.vidaReforged.server.capabilities.VidaChunkCrystalCapability;
import teamHTBP.vidaReforged.server.capabilities.VidaMagicToolCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

public class VidaChunkCrystalCapabilityProvider extends VidaBaseCapabilityProvider<IVidaChunkCrystalCapability>{
    public VidaChunkCrystalCapabilityProvider() {
        super(VidaCapabilityRegisterHandler.VIDA_CHUNK_CRYSTAL);
    }

    @Override
    public IVidaChunkCrystalCapability getOrCreateCapability() {
        if(this.capability == null){
            this.capability = new VidaChunkCrystalCapability();
        }
        return this.capability;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }
}
