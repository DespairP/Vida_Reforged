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
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.server.capabilities.VidaManaCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

public class VidaManaCapabilityProvider extends VidaBaseCapabilityProvider<IVidaManaCapability> {
    public VidaManaCapabilityProvider() {
        super(VidaCapabilityRegisterHandler.VIDA_MANA);
    }

    /**获取cap*/
    public IVidaManaCapability getOrCreateCapability(){
        if(this.capability == null){
            this.capability = new VidaManaCapability(5000);
        }
        return this.capability;
    }
}
