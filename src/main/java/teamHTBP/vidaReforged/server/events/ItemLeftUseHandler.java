package teamHTBP.vidaReforged.server.events;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

/**处理玩家左键物品后的操作*/
@Mod.EventBusSubscriber
public class ItemLeftUseHandler {
    @SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.LeftClickEmpty event) {
        if(event.getLevel().isClientSide || event.getHand() != InteractionHand.MAIN_HAND){
            return;
        }
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        if(item instanceof IItemLeftUseHandler itemHandler){
            itemHandler.onLeftClick(event.getLevel(), event.getEntity(), itemStack, event.getPos());
        }
    }


}
