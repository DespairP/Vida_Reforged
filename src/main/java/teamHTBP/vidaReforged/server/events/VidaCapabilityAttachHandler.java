package teamHTBP.vidaReforged.server.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.common.VidaConstant;
import teamHTBP.vidaReforged.helper.VidaElementHelper;
import teamHTBP.vidaReforged.server.capabilities.VidaPlayerMagicCapability;
import teamHTBP.vidaReforged.server.capabilities.provider.*;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;


/**当玩家*/
@Mod.EventBusSubscriber()
public class VidaCapabilityAttachHandler {
    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<ItemStack> event) {
        if(event.getObject().is(VidaItemLoader.VIDA_WAND.get())){
            event.addCapability(new ResourceLocation(VidaReforged.MOD_ID, VidaConstant.CAP_VIDA_MANA), new VidaManaCapabilityProvider(5000, false, VidaElementHelper.getNormalElements()));
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
           event.addCapability(new ResourceLocation(VidaReforged.MOD_ID, VidaConstant.CAP_VIDA_PLAYER_RPG_SKILL), new VidaPlayerSkillsCapabilityProvider(VidaCapabilityRegisterHandler.VIDA_RPG_SKILL));
        }
    }

    @SubscribeEvent
    public static void onAttachChunkCapabilityEvent(AttachCapabilitiesEvent<LevelChunk> event){
        event.addCapability(new ResourceLocation(VidaReforged.MOD_ID,VidaConstant.CAP_VIDA_CHUNK_CRYSTAL), new VidaChunkCrystalCapabilityProvider());
    }

    @SubscribeEvent
    public static void onAttachBlockEntityCapabilityEvent(AttachCapabilitiesEvent<BlockEntity> event){
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        copyCapability(
                event.getOriginal().getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_WORD),
                event.getEntity().getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_WORD)
        );
        copyCapability(
                event.getOriginal().getCapability(VidaCapabilityRegisterHandler.VIDA_PLAYER_MAGIC),
                event.getEntity().getCapability(VidaCapabilityRegisterHandler.VIDA_PLAYER_MAGIC)
        );
        copyCapability(
                event.getOriginal().getCapability(VidaCapabilityRegisterHandler.VIDA_RPG_SKILL),
                event.getEntity().getCapability(VidaCapabilityRegisterHandler.VIDA_RPG_SKILL)
        );
    }

    public static <T extends INBTSerializable<CompoundTag>> void copyCapability(LazyOptional<T> oldCap, LazyOptional<T> newCap){
        if (oldCap.isPresent() && newCap.isPresent()) {
            newCap.ifPresent((newCap$1) -> {
                oldCap.ifPresent((oldCap$1) -> {
                    newCap$1.deserializeNBT(oldCap$1.serializeNBT());
                });
            });
        }
    }
}
