package teamHTBP.vidaReforged.core.common.system.magic;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticle;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticleAttribute;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticleType;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;
import teamHTBP.vidaReforged.server.entity.projectile.PartyParrotProjecttile;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class VidaMagicHelper {

    public static VidaElement getCurrentMagicElement(ItemStack wandStack){
        return VidaElement.EMPTY;
    }



    public static List<VidaElement> getCurrentMagicElements(ItemStack wandStack){
        return ImmutableList.of(VidaElement.EMPTY);
    }

    public static VidaMagicContainer getCurrentMagic(ItemStack wandStack){
        if(wandStack == null || wandStack.isEmpty()){
            return null;
        }
        LazyOptional<IVidaMagicContainerCapability> containerCap = wandStack.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
        AtomicReference<VidaMagicContainer> container = new AtomicReference<>(null);
        containerCap.ifPresent((capability) -> container.set(capability.getContainer()));
        return container.get();
    }

    /**触发魔法*/
    public static void invokeMagic(IVidaMagicContainerCapability magicContainer, IVidaManaCapability manaContainer, Level level, Player player, VidaMagic currentMagic,ItemStack handInItem){
        LazyOptional<VidaMagic.IInvokable> optInvokable = LazyOptional.empty();

        if(currentMagic == null){
            return;
        }

        if(currentMagic.regex() == null){
            optInvokable = VidaMagicManager.getMagicInvokable(currentMagic.magicName());
        }else{
            optInvokable = VidaMagicManager.getMagicInvokableRegex(currentMagic.regex());
        }


        optInvokable.ifPresent((invokable) -> {
            invokable.invokeMagic(handInItem, currentMagic, magicContainer.getContainer(), manaContainer, level, player);
        });


    }
}
