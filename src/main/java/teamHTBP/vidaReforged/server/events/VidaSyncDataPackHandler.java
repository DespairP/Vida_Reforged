package teamHTBP.vidaReforged.server.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.server.packets.MagicGuidePacket;
import teamHTBP.vidaReforged.server.packets.MagicDatapackPacket;
import teamHTBP.vidaReforged.server.packets.MagicWordDatapackPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;
import teamHTBP.vidaReforged.server.providers.VidaMagicManager;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;
import teamHTBP.vidaReforged.server.providers.TeaconGuideBookManager;

@Mod.EventBusSubscriber
public class VidaSyncDataPackHandler {
    @SubscribeEvent
    public static void syncDataPack(PlayerEvent.PlayerLoggedInEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof ServerPlayer player){
            VidaPacketManager.sendToEntity(new MagicWordDatapackPacket(MagicWordManager.getMagicWordIdMap()), player);
            VidaPacketManager.sendToEntity(new MagicDatapackPacket(VidaMagicManager.getMagicIdMap()), player);
            VidaPacketManager.sendToEntity(new MagicGuidePacket(TeaconGuideBookManager.getPageIdMap()), player);
        }
    }
}
