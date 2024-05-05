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
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicWordCapability;
import teamHTBP.vidaReforged.server.capabilities.VidaMagicWordCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

public class VidaMagicWordCapabilityProvider extends VidaBaseCapabilityProvider<IVidaMagicWordCapability> {
    public VidaMagicWordCapabilityProvider() {
        super(VidaCapabilityRegisterHandler.VIDA_MAGIC_WORD);
    }

    /**获取cap*/
    public IVidaMagicWordCapability getOrCreateCapability(){
        if(this.capability == null){
            this.capability = new VidaMagicWordCapability();
        }
        return this.capability;
    }
}
