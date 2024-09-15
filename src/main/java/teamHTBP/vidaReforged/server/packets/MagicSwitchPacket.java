package teamHTBP.vidaReforged.server.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import java.util.function.Supplier;

public class MagicSwitchPacket {
    boolean isSwitchMagic = false;

    public MagicSwitchPacket(){
        this(false);
    }

    public MagicSwitchPacket(boolean isSwitchMagic) {
        this.isSwitchMagic = isSwitchMagic;
    }

    public static MagicSwitchPacket fromBytes(FriendlyByteBuf buffer){
        boolean isSwitchMagic = buffer.readBoolean();
        return new MagicSwitchPacket(isSwitchMagic);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBoolean(isSwitchMagic);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            if(player == null || !player.getItemInHand(InteractionHand.MAIN_HAND).is(VidaItemLoader.VIDA_WAND.get())){
                return;
            }
            if(player.isUsingItem()){
                return;
            }
            ItemStack vidaWand = player.getItemInHand(InteractionHand.MAIN_HAND);
            vidaWand.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER).ifPresent(
                    cap -> {
                        if(isSwitchMagic){
                            int next = cap.getCurrentMagicIndex() + 1;
                            if(next >= cap.getAvailableMagics().size()){
                                next = -1;
                            }
                            cap.setCurrentMagicIndex(next);
                            return;
                        }
                        // 切换元素
                        cap.setCurrentElementOverride(cap.getCurrentElementOverride().next());
                    }
            );

        });
        ctx.get().setPacketHandled(true);
    }
}
