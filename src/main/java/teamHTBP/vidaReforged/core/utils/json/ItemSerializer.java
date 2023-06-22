package teamHTBP.vidaReforged.core.utils.json;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;

/**
 * Json 物品序列化器
 * @author DustW
 **/
public class ItemSerializer implements JsonSerializer<Item>,JsonDeserializer<Item> {
    @Override
    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.getAsString()));
    }

    @Override
    public JsonElement serialize(Item src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
