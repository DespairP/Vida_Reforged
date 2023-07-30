package teamHTBP.vidaReforged.client.events;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyRegisterEventHandler {
    public static final KeyMapping OPEN_MAGIC_WORD_KEY = new KeyMapping("key.keyboard.open_magic_word",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_ALT,
            "key.category.vida");

    public static final Lazy<KeyMapping> OPEN_MAGIC_WORD_KEY_MAPPING = Lazy.of(() -> OPEN_MAGIC_WORD_KEY);

    @SubscribeEvent
    public static void onClientSetup(RegisterKeyMappingsEvent event) {
        event.register(OPEN_MAGIC_WORD_KEY_MAPPING.get());
    }

}
