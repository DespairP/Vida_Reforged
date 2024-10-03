package teamHTBP.vidaReforged.core.api.capability;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.server.capabilities.VidaMultiBlockCapability;

public interface IVidaCodecCapabilitySerializer<T> extends INBTSerializable<CompoundTag> {
    public static final Logger LOGGER = LogManager.getLogger();

    public Codec<T> getCodec();

    public void copyFrom(T record);

    @Override
    public default CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        try {
            tag = (CompoundTag) getCodec().encode((T)this, NbtOps.INSTANCE, tag).result().get();
        } catch (Exception ex){
            LOGGER.error(ex);
        }
        return tag;
    }

    @Override
    public default void deserializeNBT(CompoundTag nbt) {
        try {
            T cap = getCodec().parse(NbtOps.INSTANCE, nbt).get().orThrow();
            copyFrom(cap);
        } catch (Exception ex){
            LOGGER.error(ex);
        }
    }

}
