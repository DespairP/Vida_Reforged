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
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.server.capabilities.VidaManaCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

import java.util.List;

public class VidaManaCapabilityProvider extends VidaBaseCapabilityProvider<IVidaManaCapability> {
    private final int maxMana;
    private final boolean isLimited;
    private final List<VidaElement> strictedElements;
    public VidaManaCapabilityProvider(int maxMana, boolean isLimited, List<VidaElement> strictedElements) {
        super(VidaCapabilityRegisterHandler.VIDA_MANA);
        this.maxMana = maxMana;
        this.isLimited = isLimited;
        this.strictedElements = strictedElements;
    }

    /**获取cap*/
    public IVidaManaCapability getOrCreateCapability(){
        if(this.capability == null){
            this.capability = new VidaManaCapability(maxMana, isLimited, strictedElements);
        }
        return this.capability;
    }
}
