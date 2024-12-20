package teamHTBP.vidaReforged.server.events;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.server.mobs.*;

/**
 * 注册生物属性
 * @author DustW
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobsAttributeHandler {
    @SubscribeEvent
    public static void onEvent(EntityAttributeCreationEvent event) {
        event.put(VidaMobsLoader.ANCIENT_BELIEVER.get(), AncientBeliever.createAttributes().build());
        event.put(VidaMobsLoader.GLOW_LIGHT.get(), GlowLight.createAttributes().build());
        event.put(VidaMobsLoader.LIGHT_SHEEP.get(), LightSheep.createAttributes().build());
        event.put(VidaMobsLoader.ORANGE_SPOTTED_SPARROW.get(), OrangeSpottedSparrow.createAttributes().build());
        event.put(VidaMobsLoader.ELEMENTAL_SPIDER.get(), VidaElementSpider.createAttributes().build());
    }
}
