package teamHTBP.vidaReforged.core.api.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import teamHTBP.vidaReforged.core.api.VidaElement;

import java.util.List;
import java.util.Map;

public interface IVidaChunkCrystalCapability extends INBTSerializable<CompoundTag> {
    public List<BlockPos> getCrystalPosesByElement(VidaElement element);
    public List<BlockPos> getAllCrystalPoses();
    public Map<VidaElement, List<BlockPos>> getAllCrystalPosesMap();
    public void addCrystalPos(VidaElement element, BlockPos pos);
    public void removeCrystalPos(BlockPos pos);
}
