package teamHTBP.vidaReforged.server.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicWordCapability;
import teamHTBP.vidaReforged.server.blockEntities.PrismBlockEntity;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.menu.MagicWordCraftingTableMenu;
import teamHTBP.vidaReforged.server.menu.MagicWordViewMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class OpenMagicWordScreenPacket {

    /*Logger*/
    public static final Logger LOGGER = LogManager.getLogger();

    public OpenMagicWordScreenPacket() {
    }

    public static OpenMagicWordScreenPacket fromBytes(FriendlyByteBuf buffer){
        return new OpenMagicWordScreenPacket();
    }

    public void toBytes(FriendlyByteBuf buffer) {

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;
            ServerPlayer player = ctx.get().getSender();

            final List<String> magicWords = new ArrayList<>();
            final LazyOptional<IVidaMagicWordCapability> wordCap = player.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_WORD);
            final AtomicReference<CompoundTag> tag = new AtomicReference<>(new CompoundTag());
            wordCap.ifPresent(cap -> {
                magicWords.addAll(cap.getAccessibleMagicWord());
                tag.set(cap.serializeNBT());
            });


            SimpleMenuProvider provider = new SimpleMenuProvider(
                    (pContainerId,pPlayerInventory,pPlayerInv) -> new MagicWordViewMenu(
                            pContainerId,
                            magicWords
                    ),
                    Component.translatable(String.format("%s:%s", MOD_ID, "magic_word_view"))
            );

            NetworkHooks.openScreen(
                    (ServerPlayer) player,
                    provider,
                    (packerBuffer) -> {
                        packerBuffer.writeNbt(tag.get());
                    });
        });
        ctx.get().setPacketHandled(true);
    }
}
