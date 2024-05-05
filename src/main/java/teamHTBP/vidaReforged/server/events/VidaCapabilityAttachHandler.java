package teamHTBP.vidaReforged.server.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicWordCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaPlayerMagicCapability;
import teamHTBP.vidaReforged.core.common.VidaConstant;
import teamHTBP.vidaReforged.server.capabilities.VidaPlayerMagicCapability;
import teamHTBP.vidaReforged.server.capabilities.provider.*;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;


/**当玩家*/
@Mod.EventBusSubscriber()
public class VidaCapabilityAttachHandler {
    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<ItemStack> event) {
        if(event.getObject().is(VidaItemLoader.VIDA_WAND.get())){
            event.addCapability(new ResourceLocation(VidaReforged.MOD_ID, VidaConstant.CAP_VIDA_MANA), new VidaManaCapabilityProvider());
            event.addCapability(new ResourceLocation(VidaReforged.MOD_ID, VidaConstant.CAP_VIDA_MAGIC_CONTAINER), new VidaMagicContainerCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onAttachWorldCapabilityEvent(AttachCapabilitiesEvent<Level> event){
        event.addCapability(new ResourceLocation(VidaReforged.MOD_ID,VidaConstant.CAP_VIDA_MULTI_BLOCK), new VidaMultiBlockCapabilityProvider());
    }

    @SubscribeEvent
    public static void onPlayerAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if(entity instanceof Player player){
           event.addCapability(new ResourceLocation(VidaReforged.MOD_ID,VidaConstant.DATA_MAGIC_WORD),new VidaMagicWordCapabilityProvider());
           event.addCapability(new ResourceLocation(VidaReforged.MOD_ID, VidaConstant.CAP_VIDA_PLAYER_MAGIC), new VidaBaseCapabilityProvider<>(VidaCapabilityRegisterHandler.VIDA_PLAYER_MAGIC, VidaPlayerMagicCapability::new) {});
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
