package teamHTBP.vidaReforged.server.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.api.items.IVidaManaConsumable;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicAttribute;
import teamHTBP.vidaReforged.server.components.VidaWandTooltipComponent;
import teamHTBP.vidaReforged.server.entity.LazerEntity;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Vida法杖，
 * 提供魔力储存和魔法储存的
 * */
public class
VidaWand extends Item implements IVidaManaConsumable {
    public static int holdTime = 0;

    public VidaWand() {
        super(new Item.Properties().stacksTo(1));
    }

    /**当法杖nbt更新时，防止触发更新动作*/
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged && !oldStack.equals(newStack);
    }

    /**鼠标释放技能*/
    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int power) {
        if(!(entity instanceof ServerPlayer)){
            return;
        }

        ServerPlayer player = (ServerPlayer) entity;



        // 重置蓄力时长
        holdTime = 0;
    }

    /**保存蓄力时长*/
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int time) {
        super.onUseTick(level, livingEntity, itemStack, time);
        if(!level.isClientSide){
            return;
        }
        holdTime =  Math.min(this.getUseDuration(itemStack) - time, getMaxUseDuration(itemStack));
    }

    /**使用时判定能不能释放技能*/
    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack handInItem = player.getItemInHand(hand);

        if( !level.isClientSide ) {
            LazerEntity _entity1 = VidaEntityLoader.TRAIL.get().create(level);
            _entity1.init(player);
            level.addFreshEntity(_entity1);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.pass(handInItem);
    }

    /**是否能释放技能*/
    public boolean canReleaseMagic(ItemStack itemStack){
        return true;
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


        return true;
    }

    /**显示法杖各个属性*/
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
//        Style defaultStyle = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true);
//        this.getContainerCapability(itemStack).ifPresent((containerCap) ->{
//            if(containerCap.getAttribute() != null){
//                VidaMagicAttribute attribute = containerCap.getAttribute();
//                //components.add();
//            }
//        });
    }

    /***/
    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        AtomicReference<VidaWandTooltipComponent> componentReference = new AtomicReference<>(new VidaWandTooltipComponent());
        // 获取container中所存的魔法
        this.getContainerCapability(itemStack).ifPresent((containerCap) ->{

        });

        //
        this.getManaCapability(itemStack).ifPresent((manaCap)->{
            if(manaCap.getAllElementsMana() != null){
                componentReference.get().setMana(new HashMap<>(manaCap.getAllElementsMana()));
                componentReference.get().setMaxMana(manaCap.getMaxMana());
            }
        });

        return Optional.ofNullable(componentReference.get());
    }



    /**发送Packet到Client端时，Server端需要将Capability解析*/
    @Override
    public @Nullable CompoundTag getShareTag(ItemStack stack) {
        AtomicReference<CompoundTag> tag = new AtomicReference<>(super.getShareTag(stack));
        tag.set(tag.get() == null ? stack.getOrCreateTag() : tag.get());
        LazyOptional<IVidaManaCapability> manaCap = stack.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);

        manaCap.ifPresent(cap -> {
            CompoundTag manaTag = cap.serializeNBT();
            tag.get().put("mana", manaTag);
        });

        LazyOptional<IVidaMagicContainerCapability> containerCap = stack.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
        containerCap.ifPresent(cap ->{
            CompoundTag containerTag = cap.serializeNBT();
            tag.get().put("container", containerTag);
        });

        return tag.get();
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

    public VidaMagicAttribute getMagicContainer(ItemStack itemStack) {
        final AtomicReference<VidaMagicAttribute> magicContainer = new AtomicReference<>();
        itemStack.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER).ifPresent(containerCap -> {
        });
        return magicContainer.get();
    }

    /**魔法释放处理逻辑*/
    public boolean doMagic(IVidaMagicContainerCapability magicContainer,IVidaManaCapability mana, Level level, Player player, ItemStack handInItem){
//        final long currentMillSecond = System.currentTimeMillis();
//        final VidaMagicAttribute container = magicContainer.getContainer();
//        final List<String> magicList = container.magic();
//        final String currentMagicId = magicList.size() > 0 ? magicList.get(0) : null;
//        final VidaMagic currentMagic = MagicTemplateManager.getMagicById(currentMagicId);
//
//
//        if(currentMagic == null){
//            return false;
//        }
//
//        //如果还在冷却中，不释放魔法
//        if(magicContainer.isInCoolDown(currentMillSecond)){
//            return false;
//        }
//
//        //
//        if(!mana.testConsume(currentMagic.element(), container.costMana())){
//            return false;
//        }
//
//        mana.consumeMana(currentMagic.element(), container.costMana());
//        container.lastInvokeMillSec(currentMillSecond);
//
//        VidaMagicInvokeHelper.invokeMagic(magicContainer, mana, level, player, currentMagic, handInItem);

        return false;
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
