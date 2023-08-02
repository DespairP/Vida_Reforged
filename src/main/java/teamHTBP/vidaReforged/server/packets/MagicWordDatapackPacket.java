package teamHTBP.vidaReforged.server.packets;

import com.google.gson.Gson;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MagicWordDatapackPacket {
    LinkedHashMap<String,MagicWord> magicWords = new LinkedHashMap<>();

    public static final Logger LOGGER = LogManager.getLogger();

    public MagicWordDatapackPacket() {
    }

    public MagicWordDatapackPacket(LinkedHashMap<String,MagicWord> words) {
        this.magicWords = words;
    }

    public static MagicWordDatapackPacket fromBytes(FriendlyByteBuf buffer){
        try {
            LinkedHashMap<String,MagicWord> magicWords = new LinkedHashMap<>();
            magicWords.putAll(
                    buffer.readMap(
                            FriendlyByteBuf::readUtf,
                            (buf)->{
                                Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);
                                return gson.fromJson(buf.readUtf(), MagicWord.class);
                            }
                    )
            );
            return new MagicWordDatapackPacket(magicWords);
        }catch (Exception ex){
            LOGGER.error("MagicWord packet error:" + ex.getMessage());
        }
        return new MagicWordDatapackPacket();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeMap(
                magicWords,
                FriendlyByteBuf::writeUtf,
                (buf,value) -> {
                    Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);
                    if(value == null){
                        value = new MagicWord();
                    }
                    String magicWrapper = gson.toJson(value);
                    buf.writeUtf(magicWrapper);
                }
        );
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                MagicWordManager.setMagicWordIdMap(this.magicWords);
            }catch (Exception ex){
                LOGGER.error(ex);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
