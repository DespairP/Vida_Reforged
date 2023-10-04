package teamHTBP.vidaReforged.client.events;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.screen.screens.magicwordAchieve.MagicWordScreen;
import teamHTBP.vidaReforged.server.packets.OpenMagicWordScreenPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class KeyPressedEventHandler {

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event){
        if( KeyRegisterEventHandler.OPEN_MAGIC_WORD_KEY.isDown() ){
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                if(!(Minecraft.getInstance().screen instanceof MagicWordScreen)) {
                    VidaPacketManager.sendToServer(new OpenMagicWordScreenPacket());
                }
            });
        }
    }
}
