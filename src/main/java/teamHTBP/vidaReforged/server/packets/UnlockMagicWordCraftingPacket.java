package teamHTBP.vidaReforged.server.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.client.hud.VidaUnlockScreen;
import teamHTBP.vidaReforged.server.blockEntities.PrismBlockEntity;

import java.util.function.Supplier;

public class UnlockMagicWordCraftingPacket {
    String wordId = "";
    /*Logger*/
    public static final Logger LOGGER = LogManager.getLogger();

    public UnlockMagicWordCraftingPacket() {
    }

    public UnlockMagicWordCraftingPacket(String wordId) {
        this.wordId = wordId;
    }

    public static UnlockMagicWordCraftingPacket fromBytes(FriendlyByteBuf buffer){
        String wordId = buffer.readUtf(1000);
        return new UnlockMagicWordCraftingPacket(wordId);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.wordId,1000);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            //if (ctx.get().getSender() == null) return;
            VidaUnlockScreen.magicWords.add(this.wordId);
        });
        ctx.get().setPacketHandled(true);
    }
}
