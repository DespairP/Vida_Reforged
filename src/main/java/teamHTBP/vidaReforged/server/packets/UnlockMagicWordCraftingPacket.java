package teamHTBP.vidaReforged.server.packets;

import com.google.gson.reflect.TypeToken;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3d;
import teamHTBP.vidaReforged.client.hud.VidaUnlockMagicWordScreen;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class UnlockMagicWordCraftingPacket {
    List<String> wordIdList = new ArrayList<>();
    /*Logger*/
    public static final Logger LOGGER = LogManager.getLogger();

    public UnlockMagicWordCraftingPacket() {
    }

    public UnlockMagicWordCraftingPacket(String ...wordId) {
        this.wordIdList.addAll(Arrays.stream(wordId).toList());
    }

    public UnlockMagicWordCraftingPacket(List<String> wordIdList) {
        this.wordIdList.addAll(wordIdList);
    }

    public static UnlockMagicWordCraftingPacket fromBytes(FriendlyByteBuf buffer){
        Type stringType = new TypeToken<List<String>>() {}.getType();
        List<String> wordIdList = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).fromJson(buffer.readUtf(), stringType);
        return new UnlockMagicWordCraftingPacket(wordIdList);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUtf(JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).toJson(wordIdList),1000);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            //if (ctx.get().getSender() == null) return;
            VidaUnlockMagicWordScreen.magicWords.addAll(this.wordIdList);
        });
        ctx.get().setPacketHandled(true);
    }
}
