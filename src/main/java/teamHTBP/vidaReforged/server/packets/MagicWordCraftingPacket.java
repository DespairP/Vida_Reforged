package teamHTBP.vidaReforged.server.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.server.blockEntities.MagicWordCraftingTableBlockEntity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**词条合成台合成通知*/
public class MagicWordCraftingPacket {
    BlockPos pos = BlockPos.ZERO;
    /*Logger*/
    public static final Logger LOGGER = LogManager.getLogger();

    public MagicWordCraftingPacket() {
    }

    public MagicWordCraftingPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static MagicWordCraftingPacket fromBytes(FriendlyByteBuf buffer){
        BlockPos pos = buffer.readBlockPos();
        return new MagicWordCraftingPacket(pos);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;
            ServerLevel level = ctx.get().getSender().serverLevel();
            if(!level.isLoaded(pos)){
                return;
            }
            if(level.getBlockEntity(pos) instanceof MagicWordCraftingTableBlockEntity entity){
                entity.isCrafting = true;
                entity.setChanged();
                level.sendBlockUpdated(pos, entity.getBlockState(), entity.getBlockState(), 1 | 2);
            }
        });
    }
}
