package teamHTBP.vidaReforged.server.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
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

        //
        LazyOptional<IVidaManaCapability> manaCap = itemInHand.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
        manaCap.ifPresent(manaCapability -> {
             magicContainerInfo.ifPresent((container)->{
                 for(int i = 0; i < container.amount(); i++){
                     Entity entity = VidaEntityLoader.MAGIC_PARTICLE_PROJECTILE.get().create(level);
                     if (entity instanceof MagicParticleProjectile mpp) {
                         mpp.initMagicParticleProjectile(player);
                         Vec3 lookAngle = player.getLookAngle();
                         mpp.setPos(player.getEyePosition().scale(2).yRot((float) Math.toRadians(10 * i)));
                         mpp.setDeltaMovement(lookAngle.scale(mpp.particle.speed().value()));
                         level.addFreshEntity(entity);
                     }
                 }
             });
        });



        return super.use(level, player, hand);
    }

    @Override
    public @Nullable CompoundTag getShareTag(ItemStack stack) {
        AtomicReference<CompoundTag> tag = new AtomicReference<>(stack.getOrCreateTag());
        LazyOptional<IVidaManaCapability> manaCap = stack.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);

        manaCap.ifPresent(cap -> {
            CompoundTag manaTag = cap.serializeNBT();
            tag.get().put("mana", manaTag);
        });

        LazyOptional<IVidaMagicContainerCapability> containerCap = stack.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
        containerCap.ifPresent(cap ->{
            CompoundTag manaTag = cap.serializeNBT();
            tag.get().put("container", manaTag);
        });

        return tag.get();
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        if(nbt == null){
            return;
        }
        LazyOptional<IVidaManaCapability> manaCap = stack.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
        manaCap.ifPresent(cap -> cap.deserializeNBT(nbt.getCompound("mana")));
        LazyOptional<IVidaMagicContainerCapability> containerCap = stack.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
        containerCap.ifPresent(cap -> cap.deserializeNBT(nbt.getCompound("container")));
    }

    @Override
    public LazyOptional<IVidaManaCapability> getManaCapability(ItemStack itemStack) {
        return itemStack.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
    }
}
