package teamHTBP.vidaReforged.server.blockEntities;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class BasePurificationCauldronBlockEntity extends AbstractPurificationCauldronBlockEntity implements IVidaTickableBlockEntity {
    public BasePurificationCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.PURIFICATION_CAULDRON.get(), pPos, pBlockState);
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
}
