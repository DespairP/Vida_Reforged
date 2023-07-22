package teamHTBP.vidaReforged.server.recipe.ingredient;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.utils.json.ItemStackSerializer;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemStackListIngredient implements Predicate<List<ItemStack>> {
    @Expose
    public List<ItemStack> requiredItemStack = new ArrayList<>();

    @Override
    public boolean test(List<ItemStack> itemStacks) {
        if(requiredItemStack.isEmpty()){
            return false;
        }
        //
        final Map<Item, ItemStack> matchMap = itemStacks.stream().collect(Collectors.toMap(ItemStack::getItem, itemStack -> itemStack, (first, second) -> first));

        // 比对成功的次数
        int matchAmount = 0;
        for(ItemStack requiredStack : requiredItemStack){
            if(requiredItemStack.isEmpty()){
                continue;
            }
            ItemStack matchStack = matchMap.get(requiredStack.getItem());
            if(matchStack != null
                    && !matchStack.isEmpty()
                    && matchStack.is(requiredStack.getItem())
                    && matchStack.getCount() >= requiredStack.getCount()){
                matchAmount += 1;
            }
        }

        return matchAmount == requiredItemStack.size();
    }

    public JsonElement toJson() {
        return JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).toJsonTree(this.requiredItemStack);
    }

    public boolean isEmpty() {
        return this.requiredItemStack.size() == 0;
    }

    public static class Serializer implements JsonDeserializer<ItemStackListIngredient>, JsonSerializer<ItemStackListIngredient>{
        /** logger */
        public static final Logger LOGGER = LogManager.getLogger();
        @Override
        public ItemStackListIngredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ItemStackListIngredient ingredient = new ItemStackListIngredient();
            try {
                Type itemstackType = new TypeToken<List<ItemStack>>() {}.getType();
                List<ItemStack> itemStacks = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).fromJson(json, itemstackType);
                ingredient.requiredItemStack = new ArrayList<>();
                ingredient.requiredItemStack.addAll(itemStacks);
                return ingredient;
            }catch (Exception ex){
                LOGGER.error(ex.getMessage());
                LOGGER.error(ex.getStackTrace());
                return ingredient;
            }
        }

        @Override
        public JsonElement serialize(ItemStackListIngredient src, Type typeOfSrc, JsonSerializationContext context) {
            return src.toJson();
        }
    }
}
