package teamHTBP.vidaReforged.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class VidaInventoryHelper {

    @OnlyIn(Dist.CLIENT)
    public static ItemStack getHandInItemByClient(InteractionHand hand){
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null){
            return ItemStack.EMPTY;
        }
        return mc.player.getItemInHand(hand);
    }
}
