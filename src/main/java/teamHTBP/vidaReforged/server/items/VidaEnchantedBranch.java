package teamHTBP.vidaReforged.server.items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.api.items.IVidaItemWithToolTip;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.server.events.IItemLeftUseHandler;
import teamHTBP.vidaReforged.server.providers.VidaMagicManager;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**附魔树枝*/
public class VidaEnchantedBranch extends VidaWand implements IItemLeftUseHandler, IVidaItemWithToolTip {
    private static final Logger LOGGER = LogManager.getLogger();

    public VidaEnchantedBranch(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return super.use(level, player, hand);
    }

    /**没有模型*/
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {}

    @Override
    public void onLeftClick(Level level, Player player, ItemStack handInItem, BlockPos pos) {
        // 清空魔力
        if(player.isShiftKeyDown()){
            IVidaManaCapability manaContainerOpt = getManaCapability(handInItem).orElseThrow(IllegalAccessError::new);
            manaContainerOpt.setMana(VidaElement.GOLD, 0);
            manaContainerOpt.setMana(VidaElement.WOOD, 0);
            manaContainerOpt.setMana(VidaElement.FIRE, 0);
            manaContainerOpt.setMana(VidaElement.EARTH, 0);
            manaContainerOpt.setMana(VidaElement.AQUA, 0);
            return;
        }
        // 如果不能释放，则直接返回
        if (!canReleaseMagic(handInItem)) {
            return;
        }
        VidaMagic currentMagic = getCurrentMagic(handInItem);
        if (currentMagic != null && !currentMagic.isChargeable()) {
            doMagic(handInItem, level, player);
        }
    }

    @Override
    public boolean doMagic(ItemStack stack, Level level, Player player) {
        stack.setDamageValue(getDamage(stack) + 1);
        stack.hurtAndBreak(1, player, (itemstack) -> {
            itemstack.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return super.doMagic(stack, level, player);
    }

    @Override
    public void setCoolDown(ItemStack stack, Player player) {
        player.getCooldowns().addCooldown(stack.getItem(), 20);
    }

    public static void onInitEnhancementBranch(IVidaMagicContainerCapability capability){
        capability.setSingleMagic(0, new ResourceLocation(VidaReforged.MOD_ID, "star_glint"));
    }

    public boolean canAttackBlock(BlockState p_43291_, Level p_43292_, BlockPos p_43293_, Player p_43294_) {
        return !p_43294_.isCreative();
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return super.initCapabilities(stack, nbt);
    }

    public boolean canReleaseMagic(ItemStack itemStack) {
        try{
            IVidaManaCapability manaContainer = getManaCapability(itemStack).orElseThrow(NullPointerException::new);
            Map<VidaElement, Double> elementsMana = manaContainer.getAllElementsMana();
            for(double mana : elementsMana.values()){
                if(!Double.isNaN(mana) && mana >= 1){
                    return true;
                }
            }
        }catch (Exception ex){
            LOGGER.error(ex);
        }
        return false;
    }
}
