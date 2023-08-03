package teamHTBP.vidaReforged.server.items;

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicHelper;
import teamHTBP.vidaReforged.server.components.VidaWandTooltipComponent;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;
import teamHTBP.vidaReforged.server.entity.projectile.MagicParticleProjectile;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.providers.MagicTemplateManager;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
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
                 boolean isMagicReleased = doMagic(container, manaCapability, level, player, itemInHand);
             });
        });


        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        Style defaultStyle = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true);
        this.getContainerCapability(itemStack).ifPresent((containerCap) ->{
            if(containerCap.getContainer() != null){
                VidaMagicContainer container = containerCap.getContainer();
                components.add(Component.translatable("attribute.vida_forged.amount").append(String.valueOf(container.amount())).withStyle(defaultStyle));
                components.add(Component.translatable("attribute.vida_forged.damage").append(String.valueOf(container.damage())).withStyle(defaultStyle));
                components.add(Component.translatable("attribute.vida_forged.speed").append(String.format("%.2f x",container.speed())).withStyle(defaultStyle));
                components.add(Component.translatable("attribute.vida_forged.max_age").append(String.valueOf(container.maxAge())).withStyle(defaultStyle));
                components.add(Component.translatable("attribute.vida_forged.cool_down").append(String.valueOf(container.coolDown() / 1000) + 's').withStyle(defaultStyle));


                final List<String> magicList = container.magic();
                final String currentMagicId = magicList.size() > 0 ? magicList.get(0) : null;
                final VidaMagic currentMagic = MagicTemplateManager.getMagicById(currentMagicId);

                if(currentMagic != null){
                    VidaElement element = currentMagic.element() == null ? VidaElement.EMPTY : currentMagic.element();
                    components.add(Component.translatable("attribute.vida_forged.current_consume_element").withStyle(defaultStyle).append(
                            Component.translatable(String.format("element.vida_reforged.%s",currentMagic.element().toString().toLowerCase(Locale.ROOT)))
                                    .withStyle(style -> style.withColor(element.baseColor.argb()))
                    ));
                    components.add(Component.translatable("attribute.vida_forged.current_consume").withStyle(defaultStyle).append(String.valueOf(container.costMana())));

                }

            }
        });
    }

    /***/
    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        AtomicReference<TooltipComponent> componentReference = new AtomicReference<>(null);
        // 获取container中所存的魔法
        this.getContainerCapability(itemStack).ifPresent((containerCap) ->{
            if(containerCap.getContainer() != null){
                final List<String> magics = containerCap.getContainer().magic() == null ? ImmutableList.of() : containerCap.getContainer().magic();
                componentReference.set(
                        new VidaWandTooltipComponent(magics)
                );
            }

        });
        return Optional.ofNullable(componentReference.get());
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

        return super.getShareTag(stack);
    }

    /**处理Server端发来的CompoundTag，然后解析成Capability*/
    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
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

    public LazyOptional<IVidaMagicContainerCapability> getContainerCapability(ItemStack itemStack) {
        return itemStack.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
    }

    /**魔法释放处理逻辑*/
    public boolean doMagic(IVidaMagicContainerCapability magicContainer,IVidaManaCapability mana, Level level, Player player, ItemStack handInItem){
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

        VidaMagicHelper.invokeMagic(magicContainer, mana, level, player, currentMagic);

        return true;
    }
}
