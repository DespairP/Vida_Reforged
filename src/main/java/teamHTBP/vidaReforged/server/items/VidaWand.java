package teamHTBP.vidaReforged.server.items;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.client.renderer.item.JackInTheBoxRenderer;
import teamHTBP.vidaReforged.client.hud.VidaWandStaminaScreen;
import teamHTBP.vidaReforged.client.model.itemstackModel.VidaWandItemModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.api.items.IVidaManaConsumable;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicAttribute;
import teamHTBP.vidaReforged.helper.VidaMagicInvokeHelper;
import teamHTBP.vidaReforged.server.components.VidaWandTooltipComponent;
import teamHTBP.vidaReforged.server.entity.LazerEntity;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Vida法杖，
 * 提供魔力储存和魔法储存的
 */
public class VidaWand extends Item implements IVidaManaConsumable {
    public static int holdTime = 0;

    public VidaWand(Item.Properties properties) {
        super(properties);
    }

    /**
     * 当法杖nbt更新时，防止触发更新动作
     */
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged && !oldStack.equals(newStack);
    }

    /**
     * 鼠标释放技能
     */
    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int power) {
        // 重置蓄力时长
        holdTime = 0;
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }
    }

    /**
     * 保存蓄力时长
     */
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int time) {
        super.onUseTick(level, livingEntity, itemStack, time);
        if (!level.isClientSide) {
            return;
        }
        holdTime = Math.min(this.getUseDuration(itemStack) - time, getMaxUseDuration(itemStack));
    }

    /**
     * 使用时判定能不能释放技能
     */
    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack handInItem = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND) {
            return InteractionResultHolder.pass(handInItem);
        }
        // 如果不能释放，则直接返回
        if (!canReleaseMagic(handInItem)) {
            return InteractionResultHolder.pass(handInItem);
        }
        VidaMagic currentMagic = getCurrentMagic(handInItem);
        if (currentMagic != null && !currentMagic.isChargeable()) {
            return doMagic(handInItem, level, player) ? InteractionResultHolder.sidedSuccess(handInItem, level.isClientSide) : InteractionResultHolder.fail(handInItem);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.pass(handInItem);
    }

    /**
     * 是否能释放技能
     */
    public static boolean canReleaseMagic(ItemStack itemStack) {
        try {
            IVidaMagicContainerCapability magicContainer = getContainerCapability(itemStack).orElseThrow(NullPointerException::new);
            VidaMagic magic = magicContainer.getCurrentMagic();
            VidaMagicAttribute containerAttribute = magicContainer.getAttribute();
            // 当前魔法为空则不释放
            if (magic == null || containerAttribute == null || magic.attribute() == null) {
                return false;
            }
            // 测试是否可以释放
            VidaMagicAttribute magicAttribute = magic.attribute();
            VidaElement overrideElement = magicContainer.getCurrentElementOverride();
            IVidaManaCapability manaContainer = getManaCapability(itemStack).orElseThrow(NullPointerException::new);
            boolean canRelease = manaContainer.testConsume(overrideElement == VidaElement.EMPTY || overrideElement == VidaElement.VOID ? magic.element() : overrideElement, magicAttribute.baseCostMana());
            // 返回结果
            return canRelease;
        } catch (Exception ex) {
            return false;
        }
    }

    public static VidaMagic getCurrentMagic(ItemStack itemStack) {
        IVidaMagicContainerCapability magicContainer = getContainerCapability(itemStack).orElseThrow(NullPointerException::new);
        return magicContainer.getCurrentMagic();
    }

    /**
     * 显示法杖各个属性
     */
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {

    }

    /***/
    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        AtomicReference<VidaWandTooltipComponent> componentReference = new AtomicReference<>(new VidaWandTooltipComponent());
        // 获取container中所存的魔法
        getContainerCapability(itemStack).ifPresent((containerCap) -> {

        });

        //
        getManaCapability(itemStack).ifPresent((manaCap) -> {
            if (manaCap.getAllElementsMana() != null) {
                componentReference.get().setMana(new HashMap<>(manaCap.getAllElementsMana()));
                componentReference.get().setMaxMana(manaCap.getMaxMana());
            }
        });

        return Optional.ofNullable(componentReference.get());
    }


    /**
     * 发送Packet到Client端时，Server端需要将Capability解析
     */
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
        containerCap.ifPresent(cap -> {
            CompoundTag containerTag = cap.serializeNBT();
            tag.get().put("container", containerTag);
        });

        return tag.get();
    }

    /**
     * 处理Server端发来的CompoundTag，然后解析成Capability
     */
    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        if (nbt == null) {
            return;
        }
        LazyOptional<IVidaManaCapability> manaCap = stack.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
        manaCap.ifPresent(cap -> cap.deserializeNBT(nbt.getCompound("mana")));
        LazyOptional<IVidaMagicContainerCapability> containerCap = stack.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
        containerCap.ifPresent(cap -> cap.deserializeNBT(nbt.getCompound("container")));
    }

    /**
     * 获取ManaCapability
     */
    public static LazyOptional<IVidaManaCapability> getManaCapability(ItemStack itemStack) {
        return itemStack.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
    }

    public static LazyOptional<IVidaMagicContainerCapability> getContainerCapability(ItemStack itemStack) {
        return itemStack.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
    }

    public static VidaMagicAttribute getMagicContainer(ItemStack itemStack) {
        final AtomicReference<VidaMagicAttribute> magicContainer = new AtomicReference<>();
        itemStack.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER).ifPresent(containerCap -> {
        });
        return magicContainer.get();
    }

    /**
     * 魔法释放处理逻辑
     */
    public boolean doMagic(ItemStack stack, Level level, Player player) {
        player.getCooldowns().addCooldown(stack.getItem(), 10);
        // 获取基本信息
        final IVidaManaCapability manaContainer = getManaCapability(stack).orElseThrow(NullPointerException::new);
        final IVidaMagicContainerCapability magicContainer = getContainerCapability(stack).orElseThrow(NullPointerException::new);
        // 获取当前魔法
        VidaMagic currentMagic = magicContainer.getCurrentMagic();
        if (currentMagic == null || currentMagic.attribute() == null) {
            return false;
        }
        // 检查是否能消耗魔法
        if (!manaContainer.testConsume(currentMagic.element(), currentMagic.attribute().baseCostMana())) {
            return false;
        }
        manaContainer.consumeMana(currentMagic.element(), currentMagic.attribute().baseCostMana());

        VidaMagicInvokeHelper.invokeMagic(stack, currentMagic, level, player);

        return true;
    }


    public int getUseDuration(ItemStack itemStack) {
        return 20000;
    }

    public static int getMaxUseDuration(ItemStack itemStack) {
        return 20;
    }

    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        IClientItemExtensions extensions = new IClientItemExtensions() {
            VidaWandItemModel model;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (model == null) {
                    model = new VidaWandItemModel();
                }
                return model;
            }
        };
        consumer.accept(extensions);
    }
}
