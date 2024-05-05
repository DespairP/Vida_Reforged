package teamHTBP.vidaReforged.server.packets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;
import teamHTBP.vidaReforged.server.providers.VidaMagicManager;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MagicDatapackPacket {
    LinkedHashMap<ResourceLocation, VidaMagic> magicMap = new LinkedHashMap<>();

    public static final Logger LOGGER = LogManager.getLogger();

    public MagicDatapackPacket() {
    }

    public MagicDatapackPacket(LinkedHashMap<ResourceLocation,VidaMagic> magics) {
        this.magicMap = magics;
    }

    public static MagicDatapackPacket fromBytes(FriendlyByteBuf buffer){
        try {
            LinkedHashMap<ResourceLocation,VidaMagic> magicWords = new LinkedHashMap<>();
            magicWords.putAll(
                    buffer.readMap(
                            FriendlyByteBuf::readResourceLocation,
                            (buf)->{
                                Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);
                                JsonElement element = gson.fromJson(buf.readUtf(), JsonElement.class);
                                return VidaMagic.codec.parse(JsonOps.INSTANCE, element).getOrThrow(true, message -> LOGGER.error("vida magic parse error, message: {}" , message));
                            }
                    )
            );
            return new MagicDatapackPacket(magicWords);
        }catch (Exception ex){
            LOGGER.error("Magic packet error:" + ex.getMessage());
        }
        return new MagicDatapackPacket();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeMap(
                (Map<ResourceLocation, VidaMagic>) magicMap,
                FriendlyByteBuf::writeResourceLocation,
                ((buf, value) -> {
                    Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);
                    try {
                        JsonElement magicJson = VidaMagic.codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow(false, message -> LOGGER.error("Magic packet error:{}", message ));
                        buf.writeUtf(gson.toJson(magicJson));
                    }catch (Exception ex){
                        LOGGER.error("Magic packet error:" + ex.getMessage());
                    }
                })
        );
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                VidaMagicManager.setMagicIdMap(this.magicMap);
            }catch (Exception ex){
                LOGGER.error("Magic packet error:" + ex.getMessage());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
