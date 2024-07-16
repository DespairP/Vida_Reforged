package teamHTBP.vidaReforged.server.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.particles.VidaParticleAttributes;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaChunkCrystalCapability;
import teamHTBP.vidaReforged.server.blockEntities.FloatingCrystalBlockEntity;
import teamHTBP.vidaReforged.server.providers.ElementPotentialManager;
import teamHTBP.vidaReforged.server.providers.records.ElementPotential;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class VidaCrystalEvent {
    private static final int MAX_DISTANCE = 10;
    @SubscribeEvent
    public static void onPlantGrow(BlockEvent.CropGrowEvent.Post event){
        BlockPos pos = event.getPos();
        ChunkAccess chunkAccess = event.getLevel().getChunk(pos);
        RandomSource rand = event.getLevel().getRandom();
        Item cropBlockAsItem = event.getState().getBlock().asItem();
        if(cropBlockAsItem != Items.AIR && !event.getLevel().isClientSide() && chunkAccess instanceof LevelChunk chunk){
            ElementPotential potential = ElementPotentialManager.getPotential(cropBlockAsItem);
            chunk.getCapability(VidaCapabilityRegisterHandler.VIDA_CHUNK_CRYSTAL).ifPresent(cap -> {
                BlockPos crystalPos = tryAddPotentialToBlock(cap, event.getLevel(), pos, potential);
                if(!crystalPos.equals(BlockPos.ZERO) && event.getLevel() instanceof ServerLevel level){
                    level.sendParticles(
                            new BaseParticleType(
                                    VidaParticleTypeLoader.CUBE_2D_PARTICLE_TYPE,
                                    new VidaParticleAttributes(80, rand.nextFloat() * 0.01F, potential.getElement().getBaseColor().toARGB(),null, BlockPos.ZERO.getCenter().toVector3f())
                            ),
                            crystalPos.getX() + 0.6F,
                            crystalPos.getY() + 0.5F,
                            crystalPos.getZ() + 0.6F,
                            20,
                            0.2F,
                            0.2F,
                            0.2F,
                            rand.nextGaussian() * 0.0003F
                            );
                }
            });
        }
    }

    public static BlockPos tryAddPotentialToBlock(IVidaChunkCrystalCapability capability, LevelAccessor level, BlockPos blockPos, ElementPotential potential){
        if(potential == null || potential.getElement() == null || capability.getCrystalPosesByElement(potential.getElement()).size() == 0){
            return BlockPos.ZERO;
        }
        BlockPos nearestBlockPos = BlockPos.ZERO;
        double nearestDistance = Double.MAX_VALUE;
        List<BlockPos> poses = capability.getCrystalPosesByElement(potential.getElement());
        // 最近
        for(BlockPos testPos : poses){
            double distance = testPos.distSqr(blockPos);
            if(distance < nearestDistance){
                nearestDistance = distance;
                nearestBlockPos = testPos;
            }
        }
        if(nearestBlockPos == BlockPos.ZERO || nearestDistance > MAX_DISTANCE){
            return BlockPos.ZERO;
        }
        //
        if(level.getBlockEntity(nearestBlockPos) instanceof FloatingCrystalBlockEntity entity){
            entity.addPotential(potential.energy);
        }
        return nearestBlockPos;
    }

    @SubscribeEvent
    public static void onSaveChunk(final ChunkDataEvent.Save event){
        //final var chunkEnergy = getChunkEnergy(event.getLevel(), event.getPos()).orElseThrow(CapabilityNotPresentException::new);

        //TestMod3.network.send(new UpdateChunkEnergyValueMessage(chunkEnergy), PacketDistributor.PLAYER.with(player));
    }
}
