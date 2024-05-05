package teamHTBP.vidaReforged.server.capabilities.provider;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaMultiBlockCapability;
import teamHTBP.vidaReforged.server.capabilities.VidaManaCapability;
import teamHTBP.vidaReforged.server.capabilities.VidaMultiBlockCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

import java.util.LinkedList;

public class VidaMultiBlockCapabilityProvider extends VidaBaseCapabilityProvider<IVidaMultiBlockCapability> {


    public VidaMultiBlockCapabilityProvider() {
        super(VidaCapabilityRegisterHandler.VIDA_MULTI_BLOCK);
    }

    /**获取cap*/
    public IVidaMultiBlockCapability getOrCreateCapability(){
        if(this.capability == null){
            this.capability = new VidaMultiBlockCapability(new LinkedList<>());
        }
        return this.capability;
    }
}
