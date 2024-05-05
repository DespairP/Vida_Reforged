package teamHTBP.vidaReforged.server.capabilities.provider;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.server.capabilities.VidaMagicToolCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

public class VidaMagicContainerCapabilityProvider extends VidaBaseCapabilityProvider<IVidaMagicContainerCapability> {

    public VidaMagicContainerCapabilityProvider() {
        super(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
    }

    /**获取cap*/
    public IVidaMagicContainerCapability getOrCreateCapability(){
        if(this.capability == null){
            this.capability = new VidaMagicToolCapability();
        }
        return this.capability;
    }
}
