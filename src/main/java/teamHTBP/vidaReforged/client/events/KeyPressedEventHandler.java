package teamHTBP.vidaReforged.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.events.registries.KeyRegistryHandler;
import teamHTBP.vidaReforged.client.screen.screens.magicwordAchieve.MagicWordScreen;
import teamHTBP.vidaReforged.helper.VidaInventoryHelper;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.packets.MagicSwitchPacket;
import teamHTBP.vidaReforged.server.packets.OpenMagicWordScreenPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class KeyPressedEventHandler {



    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event){
        if( KeyRegistryHandler.OPEN_MAGIC_WORD_KEY.isDown() ){
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                if(!(Minecraft.getInstance().screen instanceof MagicWordScreen)) {
                    VidaPacketManager.sendToServer(new OpenMagicWordScreenPacket());
                }
            });
            return;
        }

        if(KeyRegistryHandler.VIDA_WAND_SWITCH_ELEMENT.isDown()  && VidaInventoryHelper.getHandInItemByClient(InteractionHand.MAIN_HAND).is(VidaItemLoader.VIDA_WAND.get())){
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                VidaPacketManager.sendToServer(new MagicSwitchPacket(false));
            });
            return;
        }


        if(KeyRegistryHandler.VIDA_WAND_SWITCH_SKILLS.isDown() && VidaInventoryHelper.getHandInItemByClient(InteractionHand.MAIN_HAND).is(VidaItemLoader.VIDA_WAND.get())){
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                VidaPacketManager.sendToServer(new MagicSwitchPacket(true));
            });
            return;
        }


    }
}
