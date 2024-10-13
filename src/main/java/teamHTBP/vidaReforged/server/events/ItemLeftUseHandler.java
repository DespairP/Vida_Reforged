package teamHTBP.vidaReforged.server.events;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.server.packets.ItemLeftClickClientboundPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;

import java.util.Map;

/**处理玩家左键物品后的操作*/
@Mod.EventBusSubscriber
public class ItemLeftUseHandler {
    @SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.LeftClickEmpty event) {
        if(event.getHand() != InteractionHand.MAIN_HAND){
            return;
        }
        VidaPacketManager.sendToServer(new ItemLeftClickClientboundPacket());
    }

    @SubscribeEvent
    public static void onUseItemWithEntity(PlayerInteractEvent.EntityInteract event){
        if(event.getLevel().isClientSide){
            return;
        }
        Player player = event.getEntity();
        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        Item item = itemStack.getItem();
        if(item instanceof IItemLeftUseHandler itemHandler){
            itemHandler.onLeftClick(event.getLevel(), player, itemStack, player.blockPosition());
        }
    }

}
