package teamHTBP.vidaReforged.server.items;

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.client.hud.VidaWandStaminaScreen;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.items.IVidaManaConsumable;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicContainer;
import teamHTBP.vidaReforged.helper.VidaMagicInvokeHelper;
import teamHTBP.vidaReforged.server.components.VidaWandTooltipComponent;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.providers.MagicTemplateManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static teamHTBP.vidaReforged.core.common.VidaConstant.TAG_HOLD_TIME;

/**Vida法杖*/
public class VidaWand extends Item implements IVidaManaConsumable {
    public VidaWand() {
        super(new Item.Properties().stacksTo(1));
    }

    /**释放技能*/
    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int power) {
        if(!(entity instanceof ServerPlayer)){
            return;
        }
        ServerPlayer player = (ServerPlayer) entity;
        int lastPower = this.getUseDuration(itemStack) - power;

        releaseMagic(level, player, player.getUsedItemHand());

        // 重置蓄力时长
        VidaWandStaminaScreen.holdTime = 0;
    }

    /**保存蓄力时长*/
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int time) {
        super.onUseTick(level, livingEntity, itemStack, time);
        if(!level.isClientSide){
            return;
        }
        VidaWandStaminaScreen.holdTime =  Math.min(this.getUseDuration(itemStack) - time, getMaxUseDuration(itemStack));
    }

    /**使用时判定能不能释放技能*/
    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack handInItem = player.getItemInHand(hand);

        if(!canReleaseMagic(handInItem)){
            return InteractionResultHolder.fail(handInItem);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.pass(handInItem);
    }

    /**是否能释放技能*/
    public boolean canReleaseMagic(ItemStack itemStack){
        final long currentMillSecond = System.currentTimeMillis();
        // 判定有没有魔法，是否在冷却状态
        final List<String> magicIdList = new ArrayList<>();
        final AtomicBoolean isInCoolDown = new AtomicBoolean();
        final VidaMagicContainer magicContainer = this.getMagicContainer(itemStack);
        LazyOptional.of( () -> magicContainer).ifPresent((container) ->{
            magicIdList.addAll(magicContainer.magic() == null ? ImmutableList.of() : magicContainer.magic());
            isInCoolDown.set(currentMillSecond - container.lastInvokeMillSec() - container.coolDown() < 0);
        });

        // 没有capabilities，没有魔法，还在冷却中都不能释放魔法
        if(magicContainer == null || magicIdList.size() == 0 || isInCoolDown.get()){
            return false;
        }

        // 判定有没有足够的能量
        final VidaMagic currentMagic = MagicTemplateManager.getMagicById(magicIdList.get(0));
        final AtomicBoolean hasEnoughMana = new AtomicBoolean();
        this.getManaCapability(itemStack).ifPresent(manaCap -> {
            hasEnoughMana.set(manaCap.testConsume(currentMagic.element(), magicContainer.costMana()));
        });

        //
        return hasEnoughMana.get();
    }

    /**
     * 施法
     * @return 魔法是否被释放
     * */
    public boolean releaseMagic(Level level, Player player, InteractionHand hand){
        ItemStack itemInHand = player.getItemInHand(hand);
        // 获取此次释放魔法需要的信息
        LazyOptional<IVidaMagicContainerCapability> containerCap = itemInHand.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);

        // 获取物品的能量
        LazyOptional<IVidaManaCapability> manaCap = itemInHand.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);

        AtomicBoolean isMagicReleased = new AtomicBoolean();

        //处理
        manaCap.ifPresent((manaCapability) -> {
            containerCap.ifPresent((container)->{
                isMagicReleased.set( doMagic(container, manaCapability, level, player, itemInHand) );
            });
        });

        return isMagicReleased.get();
    }

    /**显示法杖各个属性*/
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
        AtomicReference<VidaWandTooltipComponent> componentReference = new AtomicReference<>(new VidaWandTooltipComponent());
        // 获取container中所存的魔法
        this.getContainerCapability(itemStack).ifPresent((containerCap) ->{
            if(containerCap.getContainer() != null){
                final List<String> magics = containerCap.getContainer().magic() == null ? ImmutableList.of() : containerCap.getContainer().magic();
                componentReference.get().setMagics(magics);
            }
        });

        //
        this.getManaCapability(itemStack).ifPresent((manaCap)->{
            if(manaCap.getCurrentMana() != null){
                componentReference.get().setMana(new HashMap<>(manaCap.getCurrentMana()));
                componentReference.get().setMaxMana(manaCap.maxMana());
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

    public VidaMagicContainer getMagicContainer(ItemStack itemStack) {
        final AtomicReference<VidaMagicContainer> magicContainer = new AtomicReference<>();
        itemStack.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER).ifPresent(containerCap -> {
            magicContainer.set(containerCap.getContainer());
        });
        return magicContainer.get();
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

        VidaMagicInvokeHelper.invokeMagic(magicContainer, mana, level, player, currentMagic, handInItem);

        return true;
    }


    public int getUseDuration(ItemStack itemStack) {
        return 20000;
    }

    public static int getMaxUseDuration(ItemStack itemStack){
        return 20;
    }

    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }
}
