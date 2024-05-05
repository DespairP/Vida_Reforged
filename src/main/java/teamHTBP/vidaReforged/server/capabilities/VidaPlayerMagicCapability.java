package teamHTBP.vidaReforged.server.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.common.util.INBTSerializable;
import teamHTBP.vidaReforged.core.api.capability.IVidaPlayerMagicCapability;
import teamHTBP.vidaReforged.core.api.capability.Result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VidaPlayerMagicCapability implements IVidaPlayerMagicCapability, INBTSerializable<CompoundTag> {
    private Set<ResourceLocation> availableMagics = new HashSet<>();

    public final static Codec<VidaPlayerMagicCapability> CODEC = RecordCodecBuilder.create( ins -> ins.group(
            ResourceLocation.CODEC.listOf().fieldOf("availableMagics").orElse(new ArrayList<>()).forGetter(entity -> new ArrayList<>(entity.getAvailableMagic()))
    ).apply(ins, VidaPlayerMagicCapability::new));

    public VidaPlayerMagicCapability(){

    }

    public VidaPlayerMagicCapability(List<ResourceLocation> availableMagics) {
        this.availableMagics.addAll(availableMagics);
    }

    public void set(VidaPlayerMagicCapability capability) {
        this.availableMagics.addAll(capability.availableMagics);
    }

    @Override
    public Set<ResourceLocation> getAvailableMagic() {
        return availableMagics;
    }

    public Result addMagic(ResourceLocation magicId){
        return availableMagics.add(magicId) ? Result.SUCCESS : Result.PASS;
    }

    @Override
    public CompoundTag serializeNBT() {
        return (CompoundTag) CODEC.encodeStart(NbtOps.INSTANCE, this).get().left().orElse(new CompoundTag());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        VidaPlayerMagicCapability magicCapability = CODEC.parse(NbtOps.INSTANCE, nbt).get().left().orElse(new VidaPlayerMagicCapability());
        this.set(magicCapability);
    }
}
