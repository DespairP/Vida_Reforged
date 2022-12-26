package teamHTBP.vidaReforged;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import teamHTBP.vidaReforged.server.mobs.MobsLoader;

/**
 * Vida Reforged
 * @author DespairP
 * @Date 2022/12/24
 *
 * */
@Mod("vida_reforged")
public class VidaReforged {
    public static final String MOD_ID = "vida_reforged";

    //将所有事件/注册器接入管线
    public VidaReforged() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MobsLoader.ENTITY_TYPES.register(bus);
    }
}
