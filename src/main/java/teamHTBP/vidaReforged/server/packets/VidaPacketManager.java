package teamHTBP.vidaReforged.server.packets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import teamHTBP.vidaReforged.VidaReforged;

import java.util.Optional;

public class VidaPacketManager {
    private static final String PROTOCOL_VERSION = "1.3";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(VidaReforged.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void sendToServer(Object message) {
        INSTANCE.sendToServer(message);
    }

    public static void sendToPlayer(Object message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void sendToPlayer(Object message, Player player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }

    public static void sendToEntity(Object message, Entity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }


    private static int id = 0;

    public static void register() {
        // s -> c
        Optional<NetworkDirection> client = Optional.of(NetworkDirection.PLAY_TO_CLIENT);
        // c -> s
        Optional<NetworkDirection> server = Optional.of(NetworkDirection.PLAY_TO_SERVER);

        INSTANCE.registerMessage(id++,
                MagicWordSelectPacket.class,
                MagicWordSelectPacket::toBytes,
                MagicWordSelectPacket::fromBytes,
                MagicWordSelectPacket::handler,
                server);
        INSTANCE.registerMessage(id++,
                MagicWordCraftingPacket.class,
                MagicWordCraftingPacket::toBytes,
                MagicWordCraftingPacket::fromBytes,
                MagicWordCraftingPacket::handler,
                server);
        INSTANCE.registerMessage(id++,
                PrismPacket.class,
                PrismPacket::toBytes,
                PrismPacket::fromBytes,
                PrismPacket::handler,
                server);
        INSTANCE.registerMessage(id++,
                MagicWordUnlockClientboundPacket.class,
                MagicWordUnlockClientboundPacket::toBytes,
                MagicWordUnlockClientboundPacket::fromBytes,
                MagicWordUnlockClientboundPacket::handler,
                client);
        INSTANCE.registerMessage(id++,
                OpenMagicWordScreenPacket.class,
                OpenMagicWordScreenPacket::toBytes,
                OpenMagicWordScreenPacket::fromBytes,
                OpenMagicWordScreenPacket::handler,
                server);
        INSTANCE.registerMessage(id++,
                MagicWordDatapackSyncClientPacket.class,
                MagicWordDatapackSyncClientPacket::toBytes,
                MagicWordDatapackSyncClientPacket::fromBytes,
                MagicWordDatapackSyncClientPacket::handler,
                client);
        INSTANCE.registerMessage(id++,
                MagicDatapackPacket.class,
                MagicDatapackPacket::toBytes,
                MagicDatapackPacket::fromBytes,
                MagicDatapackPacket::handler,
                client);
        INSTANCE.registerMessage(id++,
                MagicGuidePacket.class,
                MagicGuidePacket::toBytes,
                MagicGuidePacket::fromBytes,
                MagicGuidePacket::handler,
                client);
        INSTANCE.registerMessage(id++,
                OpenGuidebookPacket.class,
                OpenGuidebookPacket::toBytes,
                OpenGuidebookPacket::fromBytes,
                OpenGuidebookPacket::handler,
                client);
        INSTANCE.registerMessage(id++,
                MagicSwitchPacket.class,
                MagicSwitchPacket::toBytes,
                MagicSwitchPacket::fromBytes,
                MagicSwitchPacket::handler,
                server);
        INSTANCE.registerMessage(id++,
                MultiBlockSchedulerPacket.class,
                MultiBlockSchedulerPacket::toBytes,
                MultiBlockSchedulerPacket::fromBytes,
                MultiBlockSchedulerPacket::handler,
                client);
        INSTANCE.registerMessage(id++,
                MagicSelectionPacket.class,
                MagicSelectionPacket::toBytes,
                MagicSelectionPacket::fromBytes,
                MagicSelectionPacket::handler,
                server);
        INSTANCE.registerMessage(id++,
                ItemLeftClickClientboundPacket.class,
                ItemLeftClickClientboundPacket::toBytes,
                ItemLeftClickClientboundPacket::fromBytes,
                ItemLeftClickClientboundPacket::handler,
                server);
    }
}
