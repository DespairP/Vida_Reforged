package teamHTBP.vidaReforged;

import net.minecraftforge.common.ForgeConfigSpec;

/**Vida配置*/
public class VidaConfig {
    public static ForgeConfigSpec CONFIG_SPEC;
    public static ForgeConfigSpec.BooleanValue DEBUG_MODE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Vida Client Setting");
        DEBUG_MODE = builder.comment("DebugMode").define("debug_mode", false);
        builder.pop();
        CONFIG_SPEC = builder.build();
    }
}
