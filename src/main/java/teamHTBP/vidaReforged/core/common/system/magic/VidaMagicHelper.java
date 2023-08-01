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
    public static void invokeMagic(IVidaMagicContainerCapability magicContainer, IVidaManaCapability manaContainer, Level level, Player player, VidaMagic currentMagic){
        Entity entity = VidaEntityLoader.PARTY_PARROT.get().create(level);

        VidaMagicContainer container = magicContainer.getContainer();
        MagicParticle particle = new MagicParticle(
                0xFFFFFF,
                0xFFFFFF,
                new MagicParticleAttribute((float)container.speed()),
                new MagicParticleAttribute(container.amount()),
                new MagicParticleAttribute(container.maxAge()),
                new MagicParticleType(),
                new MagicParticleAttribute((float) container.damage()),
                Optional.ofNullable(currentMagic).orElse(new VidaMagic("")).element()
        );
        if (entity instanceof PartyParrotProjecttile mpp) {
            mpp.initProjectile(player, particle);
            level.addFreshEntity(entity);
        }
    }
}
