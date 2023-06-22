package teamHTBP.vidaReforged.core.utils.json;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;

/**
 * ItemStack序列化/反序列化
 * @author DustW
 **/
public class ItemStackSerializer implements JsonDeserializer<ItemStack>,JsonSerializer<ItemStack> {
    private final static String AMOUNT_KEY = "amount";

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.getAsJsonObject().get("item").getAsString()));

        if (item != null) {
            int amount = json.getAsJsonObject().get(AMOUNT_KEY).getAsInt();
            return new ItemStack(item, amount);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.addProperty("item", src.toString());
        result.addProperty(AMOUNT_KEY, src.getCount());
        return result;
    }
}
