package teamHTBP.vidaReforged.server.advancement;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;

public class VidaAdvancementTriggers {
    public static MagicWordTrigger MAGIC_WORD_TRIGGER;

    static {
        MAGIC_WORD_TRIGGER = register(new MagicWordTrigger());
    }

    private static <T extends CriterionTrigger<?>> T register(T instance) {
        return CriteriaTriggers.register(instance);
    }

    public static void init(){}
}
