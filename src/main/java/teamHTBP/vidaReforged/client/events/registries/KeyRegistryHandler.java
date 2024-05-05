package teamHTBP.vidaReforged.client.events.registries;

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

/** 注册键位 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyRegistryHandler {
    public static final KeyMapping OPEN_MAGIC_WORD_KEY = new KeyMapping("key.keyboard.open_magic_word",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_CONTROL,
            "key.category.vida");

    public static final KeyMapping VIDA_WAND_SWITCH_SKILLS = new KeyMapping("key.keyboard.vida_wand.switch_skills",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_TAB,
            "key.category.vida");

    public static final Lazy<KeyMapping> OPEN_MAGIC_WORD_KEY_MAPPING = Lazy.of(() -> OPEN_MAGIC_WORD_KEY);
    public static final Lazy<KeyMapping> VIDA_WAND_SWITCH_SKILLS_KEY_MAPPING = Lazy.of(() -> VIDA_WAND_SWITCH_SKILLS);


    @SubscribeEvent
    public static void onClientSetup(RegisterKeyMappingsEvent event) {
        event.register(OPEN_MAGIC_WORD_KEY_MAPPING.get());
        event.register(VIDA_WAND_SWITCH_SKILLS_KEY_MAPPING.get());
    }

}
