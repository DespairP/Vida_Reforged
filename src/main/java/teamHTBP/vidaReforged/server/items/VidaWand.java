package teamHTBP.vidaReforged.server.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.items.IVidaManaConsumable;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicContainer;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;
import teamHTBP.vidaReforged.server.entity.projectile.MagicParticleProjectile;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

import java.util.concurrent.atomic.AtomicReference;

import static teamHTBP.vidaReforged.core.common.system.magic.VidaMagicHelper.getCurrentMagic;

/**Vida法杖*/
public class VidaWand extends Item implements IVidaManaConsumable {
    public VidaWand() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if(level.isClientSide){
            return super.use(level, player, hand);
        }
        ItemStack itemInHand = player.getItemInHand(hand);
        // 获取此次释放魔法需要的信息
        final LazyOptional<VidaMagicContainer> magicContainerInfo = LazyOptional.of(() -> getCurrentMagic(itemInHand));

        // 获取信息


        //
        LazyOptional<IVidaManaCapability> manaCap = itemInHand.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
        manaCap.ifPresent(cap -> {
             //System.out.println(cap.getCurrentMana());

        });

        Entity entity = VidaEntityLoader.MAGIC_PARTICLE_PROJECTILE.get().create(level);

        if (entity instanceof MagicParticleProjectile mpp) {
            mpp.initMagicParticleProjectile(player);
            level.addFreshEntity(entity);
        }

        return super.use(level, player, hand);
    }

    @Override
    public @Nullable CompoundTag getShareTag(ItemStack stack) {
        AtomicReference<CompoundTag> tag = new AtomicReference<>(stack.getOrCreateTag());
        LazyOptional<IVidaManaCapability> manaCap = stack.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);

        manaCap.ifPresent(cap -> {
            tag.set(cap.serializeNBT());
        });

        return tag.get();
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        LazyOptional<IVidaManaCapability> manaCap = stack.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
        manaCap.ifPresent(cap -> cap.deserializeNBT(nbt));
    }

    @Override
    public LazyOptional<IVidaManaCapability> getManaCapability(ItemStack itemStack) {
        return itemStack.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
    }
}
