package teamHTBP.vidaReforged.server.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.server.blockEntities.MagicWordCraftingTableBlockEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MagicWordPacket {
    Map<VidaElement,String> magicWords = new LinkedHashMap<>();
    BlockPos pos = BlockPos.ZERO;

    public static final Logger LOGGER = LogManager.getLogger();

    public MagicWordPacket() {
    }

    public MagicWordPacket(BlockPos pos,Map<VidaElement, String> magicWords) {
        this.magicWords = magicWords;
        this.pos = pos;
    }

    public static MagicWordPacket fromBytes(FriendlyByteBuf buffer){
        Map<VidaElement,String> magicWords = new LinkedHashMap<>();
        try {
            BlockPos pos = buffer.readBlockPos();
            magicWords.putAll(buffer.readMap((buf -> VidaElement.valueOf(buf.readUtf())), FriendlyByteBuf::readUtf));
            return new MagicWordPacket(pos, magicWords);
        }catch (Exception ex){
            LOGGER.error("MagicWord packet error:" + ex.getMessage());
        }
        return new MagicWordPacket();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeMap(
                magicWords,
                (buf,value)->{
                    buf.writeUtf(value.toString());
                },
                FriendlyByteBuf::writeUtf
        );
    }


    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;
            try {
                ServerLevel level = ctx.get().getSender().serverLevel();
                if(!level.isLoaded(pos)){
                    return;
                }
                if(level.getBlockEntity(pos) instanceof MagicWordCraftingTableBlockEntity entity){
                    entity.setMagicWordMap(new HashMap<>(magicWords));
                    entity.setChanged();
                    level.sendBlockUpdated(pos, entity.getBlockState(), entity.getBlockState(), 1 | 2);
                }

            }catch (Exception ex){
                LOGGER.error("MagicWord packet error:" + ex.getMessage());
            }
        });
    }

}
