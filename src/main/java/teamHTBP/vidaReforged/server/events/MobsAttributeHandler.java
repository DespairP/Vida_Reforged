package teamHTBP.vidaReforged.server.events;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.server.mobs.AncientBeliever;
import teamHTBP.vidaReforged.server.mobs.VidaMobsLoader;

/**
 * @author DustW
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobsAttributeHandler {
    @SubscribeEvent
    public static void onEvent(EntityAttributeCreationEvent event) {
        event.put(VidaMobsLoader.ANCIENT_BELIEVER.get(), AncientBeliever.createAttributes().build());

    }
}
