package teamHTBP.vidaReforged.server.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicWordCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

import java.util.function.Predicate;

public class MagicWordTrigger extends SimpleCriterionTrigger<MagicWordTriggerInstance> {
    static final ResourceLocation ID = new ResourceLocation(VidaReforged.MOD_ID, "magic_word");
    @Override
    protected @NotNull MagicWordTriggerInstance createInstance(JsonObject object, ContextAwarePredicate predicate, DeserializationContext context) {
        int amount = object.get("amount").getAsInt();
        return new MagicWordTriggerInstance(getId(), predicate, amount);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, magicWordTriggerInstance -> magicWordTriggerInstance.matches(player));
    }
}
