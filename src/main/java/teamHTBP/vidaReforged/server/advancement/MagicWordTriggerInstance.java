package teamHTBP.vidaReforged.server.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.LazyOptional;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicWordCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class MagicWordTriggerInstance extends AbstractCriterionTriggerInstance {
    private final int amount;

    public MagicWordTriggerInstance(ResourceLocation id, ContextAwarePredicate predicate,int amount) {
        super(id, predicate);
        this.amount = amount;
    }

    public JsonObject serializeToJson(SerializationContext context) {
        JsonObject jsonobject = super.serializeToJson(context);
        jsonobject.addProperty("amount", this.amount);
        return jsonobject;
    }

    protected boolean matches(ServerPlayer player){
        LazyOptional<IVidaMagicWordCapability> capability = player.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_WORD);
        AtomicBoolean isMatch = new AtomicBoolean();
        capability.ifPresent(cap -> {
            isMatch.set(cap.getAccessibleMagicWord().size() >= amount);
        });
        return isMatch.get();
    }
}
