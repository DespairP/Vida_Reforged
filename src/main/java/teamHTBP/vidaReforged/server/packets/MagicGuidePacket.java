package teamHTBP.vidaReforged.server.packets;

import com.google.gson.Gson;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.common.system.guidebook.TeaconGuideBook;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;
import teamHTBP.vidaReforged.server.providers.TeaconGuideBookManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Supplier;

public class MagicGuidePacket {
    public HashMap<String, TeaconGuideBook> pageMap = new HashMap<>();

    public static final Logger LOGGER = LogManager.getLogger();

    public MagicGuidePacket() {
    }

    public MagicGuidePacket(HashMap<String, TeaconGuideBook> guidePages) {
        this.pageMap = guidePages;
    }

    public static MagicGuidePacket fromBytes(FriendlyByteBuf buffer){
        try {
            LinkedHashMap<String,TeaconGuideBook> guidePages = new LinkedHashMap<>();
            guidePages.putAll(
                    buffer.readMap(
                            FriendlyByteBuf::readUtf,
                            (buf)->{
                                Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);
                                return gson.fromJson(buf.readUtf(), TeaconGuideBook.class);
                            }
                    )
            );
            return new MagicGuidePacket(guidePages);
        }catch (Exception ex){
            LOGGER.error("Magic packet error:" + ex.getMessage());
        }
        return new MagicGuidePacket();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeMap(
                pageMap,
                FriendlyByteBuf::writeUtf,
                (buf,value) -> {
                    Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);
                    if(value == null){
                        value = new TeaconGuideBook(null, "", "", new ArrayList<>());
                    }
                    String guide = gson.toJson(value);
                    buf.writeUtf(guide);
                }
        );
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                TeaconGuideBookManager.setPageIdMap(this.pageMap);
            }catch (Exception ex){
                LOGGER.error(ex);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
