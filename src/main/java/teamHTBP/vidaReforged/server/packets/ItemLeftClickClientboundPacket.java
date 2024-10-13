package teamHTBP.vidaReforged.server.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import teamHTBP.vidaReforged.server.events.IItemLeftUseHandler;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import java.util.function.Supplier;

public class ItemLeftClickClientboundPacket {

    public ItemLeftClickClientboundPacket(){

    }

    public static ItemLeftClickClientboundPacket fromBytes(FriendlyByteBuf buffer){
        return new ItemLeftClickClientboundPacket();
    }

    public void toBytes(FriendlyByteBuf buffer) {

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            if(player == null || !player.getItemInHand(InteractionHand.MAIN_HAND).is(itemHolder -> itemHolder.get() instanceof IItemLeftUseHandler)){
                return;
            }
            ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            Item item = itemStack.getItem();
            if(item instanceof IItemLeftUseHandler itemHandler){
                itemHandler.onLeftClick(player.getCommandSenderWorld(), player, itemStack, player.blockPosition());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
