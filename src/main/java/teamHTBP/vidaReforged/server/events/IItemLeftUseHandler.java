package teamHTBP.vidaReforged.server.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public interface IItemLeftUseHandler {

    public void onLeftClick(Level level, Player player, ItemStack itemStack, BlockPos pos);
}
