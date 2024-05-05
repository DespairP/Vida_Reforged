package teamHTBP.vidaReforged.server.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import teamHTBP.vidaReforged.client.screen.screens.guidebook.VidaGuidebookListScreen;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import java.util.function.Supplier;

public class MagicSwitchPacket {
    public void MagicSwitchPacket(){}

    public static MagicSwitchPacket fromBytes(FriendlyByteBuf buffer){
        return new MagicSwitchPacket();
    }

    public void toBytes(FriendlyByteBuf buffer) {

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            if(player == null || !player.getItemInHand(InteractionHand.MAIN_HAND).is(VidaItemLoader.VIDA_WAND.get())){
                return;
            }
            ItemStack vidaWand = player.getItemInHand(InteractionHand.MAIN_HAND);
            vidaWand.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER).ifPresent(
                    cap -> {
                        int next = cap.getCurrentMagicIndex() + 1;
                        if(next >= cap.getAvailableMagics().size()){
                            next = -1;
                        }

                        cap.setCurrentMagicIndex(next);
                    }
            );

        });
        ctx.get().setPacketHandled(true);
    }
}
