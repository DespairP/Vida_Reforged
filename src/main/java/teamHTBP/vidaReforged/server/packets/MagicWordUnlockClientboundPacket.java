package teamHTBP.vidaReforged.server.packets;

import com.google.gson.reflect.TypeToken;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.client.hud.VidaUnlockMagicWordScreen;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**玩家词条解锁时会发送消息给client端*/
public class MagicWordUnlockClientboundPacket {
    List<String> wordIdList = new ArrayList<>();
    /*Logger*/
    public static final Logger LOGGER = LogManager.getLogger();

    public MagicWordUnlockClientboundPacket() {
    }

    public MagicWordUnlockClientboundPacket(String ...wordId) {
        this.wordIdList.addAll(Arrays.stream(wordId).toList());
    }

    public MagicWordUnlockClientboundPacket(List<String> wordIdList) {
        this.wordIdList.addAll(wordIdList);
    }

    public static MagicWordUnlockClientboundPacket fromBytes(FriendlyByteBuf buffer){
        Type stringType = new TypeToken<List<String>>() {}.getType();
        List<String> wordIdList = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).fromJson(buffer.readUtf(), stringType);
        return new MagicWordUnlockClientboundPacket(wordIdList);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUtf(JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).toJson(wordIdList),1000);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            VidaUnlockMagicWordScreen.magicWords.addAll(this.wordIdList);
        });
        ctx.get().setPacketHandled(true);
    }
}
