package teamHTBP.vidaReforged.helper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicInvokableManager;

/**
 * 释放
 * */
public class VidaMagicInvokeHelper {
    /**触发魔法*/
    public static void invokeMagic(IVidaMagicContainerCapability magicContainer, IVidaManaCapability manaContainer, Level level, Player player, VidaMagic currentMagic, ItemStack handInItem){
        LazyOptional<VidaMagic.IInvokable> optInvokable = LazyOptional.empty();

        if(currentMagic == null){
            return;
        }

        optInvokable = VidaMagicInvokableManager.getMagicInvokable(currentMagic);


        optInvokable.ifPresent((invokable) -> {
            invokable.invokeMagic(handInItem, currentMagic, magicContainer.getAttribute(), manaContainer, level, player);
        });

    }
}
