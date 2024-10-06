package teamHTBP.vidaReforged.server.packets;

import com.google.gson.Gson;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;
import teamHTBP.vidaReforged.server.events.VidaSyncDataPackHandler;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

/**
 * 玩家进入世界时，将server端的所有词条同步给client端
 * @see VidaSyncDataPackHandler
 * */
public class MagicWordDatapackSyncClientPacket {
    LinkedHashMap<String, MagicWord> magicWords = new LinkedHashMap<>();

    public static final Logger LOGGER = LogManager.getLogger();

    public MagicWordDatapackSyncClientPacket() {}

    public MagicWordDatapackSyncClientPacket(LinkedHashMap<String, MagicWord> words) {
        this.magicWords = words;
    }

    public static MagicWordDatapackSyncClientPacket fromBytes(FriendlyByteBuf buffer) {
        try {
            LinkedHashMap<String, MagicWord> magicWords = new LinkedHashMap<>(
                    buffer.readMap(
                            FriendlyByteBuf::readUtf,
                            (buf) -> {
                                Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);
                                return gson.fromJson(buf.readUtf(), MagicWord.class);
                            }
                    )
            );
            return new MagicWordDatapackSyncClientPacket(magicWords);
        } catch (Exception ex) {
            LOGGER.error("magic-word packet handling error occurs:" + ex.getMessage());
        }
        return new MagicWordDatapackSyncClientPacket();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeMap(
                magicWords,
                FriendlyByteBuf::writeUtf,
                (buf, value) -> {
                    Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);
                    if (value == null) {
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
            } catch (Exception ex) {
                LOGGER.error(ex);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
