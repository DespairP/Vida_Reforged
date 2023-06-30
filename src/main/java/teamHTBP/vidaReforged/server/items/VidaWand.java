package teamHTBP.vidaReforged.server.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.core.common.magic.particle.MagicParticle;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;
import teamHTBP.vidaReforged.server.entity.projectile.MagicParticleProjectile;

public class VidaWand extends Item {
    public VidaWand() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        Entity entity = VidaEntityLoader.MAGIC_PARTICLE_PROJECTILE.get().create(level);

        if (entity instanceof MagicParticleProjectile mpp) {
            mpp.initMagicParticleProjectile(player);
            level.addFreshEntity(entity);
        }

        return super.use(level, player, hand);
    }
}
