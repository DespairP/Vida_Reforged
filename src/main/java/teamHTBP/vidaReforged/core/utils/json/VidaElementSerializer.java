package teamHTBP.vidaReforged.core.utils.json;

import com.google.gson.*;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.core.api.VidaElement;

import java.lang.reflect.Type;

public class VidaElementSerializer implements JsonDeserializer<VidaElement>, JsonSerializer<VidaElement> {
    @Override
    public VidaElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String str = json.getAsString();
        return VidaElement.of(str);
    }

    @Override
    public JsonElement serialize(VidaElement src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
