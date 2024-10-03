package teamHTBP.vidaReforged.server.capabilities.provider;

import net.minecraftforge.common.capabilities.Capability;
import teamHTBP.vidaReforged.core.api.capability.IVidaPlayerRPGSkillCapability;
import teamHTBP.vidaReforged.server.capabilities.VidaMultiBlockCapability;
import teamHTBP.vidaReforged.server.capabilities.VidaPlayerSkillsCapability;

import java.util.LinkedList;

public class VidaPlayerSkillsCapabilityProvider extends VidaBaseCapabilityProvider<IVidaPlayerRPGSkillCapability>{
    public VidaPlayerSkillsCapabilityProvider(Capability<IVidaPlayerRPGSkillCapability> capability) {
        super(capability);
    }

    @Override
    public IVidaPlayerRPGSkillCapability getOrCreateCapability() {
        if(this.capability == null){
            this.capability = new VidaPlayerSkillsCapability(0);
        }
        return this.capability;
    }
}
