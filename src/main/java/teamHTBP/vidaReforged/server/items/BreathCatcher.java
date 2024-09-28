package teamHTBP.vidaReforged.server.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;
import teamHTBP.vidaReforged.server.entity.FloatingItemEntity;

/**气息核心*/
public class BreathCatcher extends Item {
    public BreathCatcher() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide){
            FloatingItemEntity entity = new FloatingItemEntity(level, player.getEyePosition().add(player.getLookAngle().scale(3)), player.position(), new Vector3d(0.1F, 0.5f, 0.1f));
            entity.setItem(new ItemStack(VidaItemLoader.EARTH_GEM.get(), 1));
            entity.setToPos(player.position());
            level.addFreshEntity(entity);
        }
        return super.use(level, player, hand);
    }
}
