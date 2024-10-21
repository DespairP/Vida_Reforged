package teamHTBP.vidaReforged.server.blockEntities;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.particles.VidaParticleAttributes;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class BasePurificationCauldronBlockEntity extends AbstractPurificationCauldronBlockEntity implements IVidaTickableBlockEntity {
    RandomSource rand;
    public BasePurificationCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.PURIFICATION_CAULDRON.get(), pPos, pBlockState);
        rand = RandomSource.create();
    }

    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity pBlockEntity){
        if(pLevel.isClientSide){
            setChanged();
            return;
        }
        //检查是否正在提炼，并继续
        if(this.isInProgress()){
            this.continuePurify();
        }

        //继续
        if(!this.isMainTaskComplete() && !this.isInProgress()){
            this.startNewSubTask();
        }

        //检查总进度是否完成
        if(this.isMainTaskComplete()){
            this.generateResult();
            this.completeMainTask();
        }
        setChanged();
        pLevel.sendBlockUpdated(pPos, pState, pState, 1 | 2);
    }

    @Override
    public Map<String, String> getDebugAttributes() {
        try {
            return ImmutableMap.of(
                    "mainElement", mainElement.name(),
                    "isWaterFilled", String.valueOf(isWaterFilled),
                    "progress", String.valueOf(progress),
                    "step", String.valueOf(step),
                    "targetSubProgress", String.valueOf(targetSubProgress),
                    "totalProgress", String.valueOf(totalProgress),
                    "isInProgress", String.valueOf(isInProgress),
                    "purificationItems", purificationItems.toString(),
                    "resultItems", resultItems.toString()
            );
        }catch (Exception ex){
            return ImmutableMap.of();
        }
    }

    @Override
    public void spawnAnimationParticle(Level level) {
        if(!level.isClientSide && rand.nextInt(30) > 28){
            int age = rand.nextInt(100, 120);
            double x = getBlockPos().getX() + 0.28F + rand.nextFloat() * 0.5F;
            double y = getBlockPos().getY() + 1F + rand.nextFloat() * 0.4f;
            double z = getBlockPos().getZ() + 0.28F + rand.nextFloat() * 0.5F;
            double scale = 0.025f;
            double speed = rand.nextFloat() * 0.25f + 0.105f;
            ((ServerLevel) level).sendParticles(
                    new BaseParticleType(
                            VidaParticleTypeLoader.CUBE_PARTICLE_TYPE,
                            new VidaParticleAttributes(age, (float) scale, mainElement.getBaseColor().toARGB(), null, null, new Vector3f(0, 0.1f, 0))
                    ),
                    x,
                    y,
                    z,
                    0,
                    0F,
                    -1F,
                    0F,
                    speed
            );
        }
    }
}
