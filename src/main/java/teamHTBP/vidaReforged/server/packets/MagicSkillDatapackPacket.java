package teamHTBP.vidaReforged.server.packets;

import com.google.gson.Gson;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;
import teamHTBP.vidaReforged.server.providers.MagicTemplateManager;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

public class MagicSkillDatapackPacket {
    LinkedHashMap<String, VidaMagic> magicMap = new LinkedHashMap<>();

    public static final Logger LOGGER = LogManager.getLogger();

    public MagicSkillDatapackPacket() {
    }

    public MagicSkillDatapackPacket(LinkedHashMap<String,VidaMagic> magics) {
        this.magicMap = magics;
    }

    public static MagicSkillDatapackPacket fromBytes(FriendlyByteBuf buffer){
        try {
            LinkedHashMap<String,VidaMagic> magicWords = new LinkedHashMap<>();
            magicWords.putAll(
                    buffer.readMap(
                            FriendlyByteBuf::readUtf,
                            (buf)->{
                                Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);
                                return gson.fromJson(buf.readUtf(), VidaMagic.class);
                            }
                    )
            );
            return new MagicSkillDatapackPacket(magicWords);
        }catch (Exception ex){
            LOGGER.error("Magic packet error:" + ex.getMessage());
        }
        return new MagicSkillDatapackPacket();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeMap(
                magicMap,
                FriendlyByteBuf::writeUtf,
                (buf,value) -> {
                    Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);
                    if(value == null){
                        value = new VidaMagic("vida_reforged:empty");
                    }
                    String magicWrapper = gson.toJson(value);
                    buf.writeUtf(magicWrapper);
                }
        );
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                MagicTemplateManager.setMagicIdMap(this.magicMap);
            }catch (Exception ex){
                LOGGER.error(ex);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
