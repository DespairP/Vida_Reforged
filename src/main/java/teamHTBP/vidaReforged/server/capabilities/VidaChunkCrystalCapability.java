package teamHTBP.vidaReforged.server.capabilities;

import com.google.gson.annotations.Expose;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaChunkCrystalCapability;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class VidaChunkCrystalCapability implements IVidaChunkCrystalCapability, INBTSerializable<CompoundTag> {
    private List<BlockPos> crystalPoses;
    private Map<VidaElement, List<BlockPos>> crystalElementPoses;

    @Expose(deserialize = false, serialize = false)
    public final static Logger LOGGER = LogManager.getLogger();

    public final static Codec<VidaChunkCrystalCapability> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            BlockPos.CODEC.listOf().fieldOf("crystalPoses").orElse(new ArrayList<>()).forGetter(VidaChunkCrystalCapability::getAllCrystalPoses),
            Codec.unboundedMap(VidaElement.CODEC, BlockPos.CODEC.listOf()).orElseGet(item -> {LOGGER.warn(item);}, ConcurrentHashMap::new).fieldOf("crystalElementPoses").forGetter(VidaChunkCrystalCapability::getAllCrystalPosesMap)
    ).apply(ins, VidaChunkCrystalCapability::new));

    public VidaChunkCrystalCapability() {
        this.crystalElementPoses = new ConcurrentHashMap<>();
        this.crystalPoses = new ArrayList<>();
    }

    public VidaChunkCrystalCapability(List<BlockPos> crystalPoses, Map<VidaElement, List<BlockPos>> crystalElementPoses){
        this.crystalElementPoses = new ConcurrentHashMap<>();
        Set<BlockPos> blockPoses = new HashSet<>();
        for(VidaElement element : crystalElementPoses.keySet()){
            List<BlockPos> poses = crystalElementPoses.get(element);
            blockPoses.addAll(poses);
            this.crystalElementPoses.put(element, new ArrayList<>(poses));
        }
        this.crystalPoses = new ArrayList<>(blockPoses);
    }

    public void copy(VidaChunkCrystalCapability cap){
        this.crystalElementPoses = new ConcurrentHashMap<>();
        Set<BlockPos> blockPoses = new HashSet<>();
        for(VidaElement element : cap.getAllCrystalPosesMap().keySet()){
            List<BlockPos> poses = cap.getCrystalPosesByElement(element);
            blockPoses.addAll(poses);
            this.crystalElementPoses.put(element, new ArrayList<>(poses));
        }
        this.crystalPoses = new ArrayList<>(blockPoses);
    }

    @Override
    public List<BlockPos> getCrystalPosesByElement(VidaElement element) {
        return this.crystalElementPoses.getOrDefault(element, new ArrayList<>());
    }

    @Override
    public List<BlockPos> getAllCrystalPoses() {
        return crystalPoses;
    }

    @Override
    public Map<VidaElement, List<BlockPos>> getAllCrystalPosesMap() {
        return crystalElementPoses;
    }

    @Override
    public void addCrystalPos(VidaElement element, BlockPos pos) {
        if(!crystalPoses.contains(pos) && element != null){
            crystalPoses.add(pos);
            List<BlockPos> poses = crystalElementPoses.getOrDefault(element, new ArrayList<>());
            poses.add(pos);
            crystalElementPoses.put(element, poses);
        }
    }

    @Override
    public void removeCrystalPos(BlockPos pos) {
        crystalPoses.remove(pos);
        for(VidaElement element : crystalElementPoses.keySet()){
            Optional.ofNullable(this.crystalElementPoses.get(element)).ifPresent(item -> item.remove(pos));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        try{
            return (CompoundTag) CODEC.encodeStart(NbtOps.INSTANCE, this).get().left().orElseThrow();
        } catch (Exception ex){
            LOGGER.error(ex);
        }
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        try{
            VidaChunkCrystalCapability cap = CODEC.parse(NbtOps.INSTANCE, nbt).get().orThrow();
            copy(cap);
        }catch (Exception ex){
            LOGGER.warn(ex);
            copy(new VidaChunkCrystalCapability());
        }
    }
}
