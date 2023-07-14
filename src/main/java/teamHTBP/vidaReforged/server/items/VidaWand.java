package teamHTBP.vidaReforged.server.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.items.IVidaManaConsumable;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicContainer;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;
import teamHTBP.vidaReforged.server.entity.projectile.MagicParticleProjectile;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.providers.MagicTemplateManager;

import java.util.List;
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
        LazyOptional<IVidaMagicContainerCapability> containerCap = itemInHand.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);

        // 获取物品的能量
        LazyOptional<IVidaManaCapability> manaCap = itemInHand.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);

        //处理
        manaCap.ifPresent((manaCapability) -> {
            containerCap.ifPresent((container)->{
                 doMagic(container, manaCapability, level, player, itemInHand);
             });
        });

        return super.use(level, player, hand);
    }

    /**发送Packet到Client端时，Server端需要将Capability解析*/
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

    /**处理Server端发来的CompoundTag，然后解析成Capability*/
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

    /**获取ManaCapability*/
    @Override
    public LazyOptional<IVidaManaCapability> getManaCapability(ItemStack itemStack) {
        return itemStack.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
    }

    /**魔法释放处理逻辑*/
    public boolean doMagic(IVidaMagicContainerCapability magicContainer,IVidaManaCapability mana,Level level,Player player,ItemStack handInItem){
        final long currentMillSecond = System.currentTimeMillis();
        final VidaMagicContainer container = magicContainer.getContainer();
        final List<String> magicList = container.magic();
        final String currentMagicId = magicList.size() > 0 ? magicList.get(0) : null;
        final VidaMagic currentMagic = MagicTemplateManager.getMagicById(currentMagicId);

        if(currentMagic == null){
            return false;
        }

        //如果还在冷却中，不释放魔法
        if(magicContainer.isInCoolDown(currentMillSecond)){
            return false;
        }

        //
        if(!mana.testConsume(currentMagic.element(), container.costMana())){
            return false;
        }

        mana.consumeMana(currentMagic.element(), container.costMana());
        container.lastInvokeMillSec(currentMillSecond);
        Entity entity = VidaEntityLoader.MAGIC_PARTICLE_PROJECTILE.get().create(level);
        if (entity instanceof MagicParticleProjectile mpp) {
            mpp.initMagicParticleProjectile(player);
            level.addFreshEntity(entity);
        }

        System.out.println(container);

        return true;
    }
}
