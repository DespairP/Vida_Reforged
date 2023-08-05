package teamHTBP.vidaReforged.server.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicWordCapability;
import teamHTBP.vidaReforged.core.common.VidaConstant;
import teamHTBP.vidaReforged.server.capabilities.provider.VidaMagicCapabilityProvider;
import teamHTBP.vidaReforged.server.capabilities.provider.VidaMagicWordCapabilityProvider;
import teamHTBP.vidaReforged.server.capabilities.provider.VidaManaCapabilityProvider;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;


/**当玩家*/
@Mod.EventBusSubscriber()
public class VidaWandCapabilityHandler {
    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<ItemStack> event) {
        if(!event.getObject().is(VidaItemLoader.VIDA_WAND.get())){
            return;
        }
        event.addCapability(new ResourceLocation(VidaReforged.MOD_ID, VidaConstant.CAP_VIDA_MANA), new VidaManaCapabilityProvider());
        event.addCapability(new ResourceLocation(VidaReforged.MOD_ID, VidaConstant.CAP_VIDA_MAGIC_CONTAINER), new VidaMagicCapabilityProvider());
    }

    @SubscribeEvent
    public static void onPlayerAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if(entity instanceof Player player){
           event.addCapability(new ResourceLocation(VidaReforged.MOD_ID,VidaConstant.DATA_MAGIC_WORD),new VidaMagicWordCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        LazyOptional<IVidaMagicWordCapability> oldCap = event.getOriginal().getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_WORD);
        LazyOptional<IVidaMagicWordCapability> newCap = event.getEntity().getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_WORD);
        if (oldCap.isPresent() && newCap.isPresent()) {
            newCap.ifPresent((newCap$1) -> {
                oldCap.ifPresent((oldCap$1) -> {
                    newCap$1.deserializeNBT(oldCap$1.serializeNBT());
                });
            });
        }
    }
}
