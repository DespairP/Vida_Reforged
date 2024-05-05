package teamHTBP.vidaReforged.server.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenGuidebookPacket {

    public void OpenGuidebookPacket(){}

    public static OpenGuidebookPacket fromBytes(FriendlyByteBuf buffer){
        return new OpenGuidebookPacket();
    }

    public void toBytes(FriendlyByteBuf buffer) {

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            VidaPacketClientHandler.handleOpen(ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }
}
