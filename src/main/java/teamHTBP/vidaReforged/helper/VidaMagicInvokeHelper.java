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
    public static void invokeMagic(ItemStack handInItem, VidaMagic currentMagic, Level level, Player player){
        if(level.isClientSide){
            return;
        }

        if(currentMagic == null){
            return;
        }

        VidaMagic.IInvokable invokable = VidaMagicInvokableManager.getMagicInvokable(currentMagic);


        invokable.invokeMagic(handInItem, currentMagic, level, player);

    }
}
