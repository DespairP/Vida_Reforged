package teamHTBP.vidaReforged.server.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.client.screen.screens.guidebook.VidaGuidebookListScreen;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

public class VidaPacketClientHandler {

    @OnlyIn(Dist.CLIENT)
    public static void handleMultiBlockSchedulerPacket(MultiBlockSchedulerPacket packet){
        ClientLevel level = (ClientLevel) Minecraft.getInstance().player.level();
        if(level.dimension().equals(packet.level)){
            level.getCapability(VidaCapabilityRegisterHandler.VIDA_MULTI_BLOCK).ifPresent(cap -> cap.setJobs(packet.jobs));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleOpen(Player player){
        Minecraft.getInstance().setScreen(new VidaGuidebookListScreen(player));
    }
}
