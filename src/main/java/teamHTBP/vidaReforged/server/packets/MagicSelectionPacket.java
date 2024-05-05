package teamHTBP.vidaReforged.server.packets;

import com.google.gson.Gson;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.common.system.guidebook.TeaconGuideBook;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;
import teamHTBP.vidaReforged.server.menu.VidaWandCraftingTableMenu;
import teamHTBP.vidaReforged.server.providers.TeaconGuideBookManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MagicSelectionPacket {
    public LinkedHashMap<Integer, ResourceLocation> magics = new LinkedHashMap<>();

    public static final Logger LOGGER = LogManager.getLogger();

    public MagicSelectionPacket() {
    }

    public MagicSelectionPacket(Map<Integer, ResourceLocation> magics) {
        this.magics.putAll(magics);
    }

    public static MagicSelectionPacket fromBytes(FriendlyByteBuf buffer){
        try {
            Map<Integer, ResourceLocation> magics = buffer.readMap(FriendlyByteBuf::readInt, FriendlyByteBuf::readResourceLocation);
            return new MagicSelectionPacket(magics);
        }catch (Exception ex){
            LOGGER.error("Magic packet error:" + ex.getMessage());
        }
        return new MagicSelectionPacket();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeMap(this.magics, (buf, integer) -> buffer.writeInt(integer), (buf, location) -> buffer.writeResourceLocation(location));
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                if(ctx.get().getSender() == null) { return; }
                ServerPlayer player = ctx.get().getSender();
                if(player.containerMenu instanceof VidaWandCraftingTableMenu menu){
                    menu.setMagics(magics);
                }
            }catch (Exception ex){
                LOGGER.error(ex);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
