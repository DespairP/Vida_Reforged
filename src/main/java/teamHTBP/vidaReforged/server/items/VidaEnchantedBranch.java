package teamHTBP.vidaReforged.server.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.server.events.IItemLeftUseHandler;

import java.util.function.Consumer;

/**附魔树枝*/
public class VidaEnchantedBranch extends VidaWand implements IItemLeftUseHandler {

    public VidaEnchantedBranch(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    /**没有模型*/
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {}

    @Override
    public void onLeftClick(Level level, Player player, ItemStack itemStack, BlockPos pos) {

    }
}
