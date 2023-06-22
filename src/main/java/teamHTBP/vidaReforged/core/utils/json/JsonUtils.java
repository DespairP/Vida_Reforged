package teamHTBP.vidaReforged.core.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;

/**
 * json处理类
 * @author DustW
 * */
public class JsonUtils {

    public static Gson getGson(JsonUtilType type){
        return type.getGson();
    }

    public enum JsonUtilType {
        /** 最 佳 单 例 */
        NORMAL("normal"),
        PRETTY("pretty"),
        NO_EXPOSE("noExpose");

        private Gson gson;
        private String typeName;

        JsonUtilType(String name) {
            GsonBuilder builder = new GsonBuilder()
                    // 关闭 html 转义
                    .disableHtmlEscaping()
                    // 开启复杂 Map 的序列化
                    .enableComplexMapKeySerialization()
                    // 注册自定义类型的序列化/反序列化器
                    //.registerTypeAdapter(Ingredient.class, new IngredientSerializer())
                    .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
                    // 注册自定义类型的序列化/反序列化器（附带子类）
                    .registerTypeHierarchyAdapter(Item.class, new ItemSerializer());
                    //.registerTypeHierarchyAdapter(IElement.class, new IElementSerializer());

            switch (name){
                case "normal" ->{
                    // 要求 *全部*字段都有 @Expose 注解的 Gson 实例
                    gson = builder.create();
                }
                case "pretty" ->{
                    // 输出的字符串漂亮一点的 Gson 实例 -> 输出到 json 文件（例如合成表）的，好看
                    builder.excludeFieldsWithoutExposeAnnotation();
                    gson = builder.create();
                }
                case "noExpose" ->{
                    // 无视 @Expose 注解的 Gson 实例
                    builder.setPrettyPrinting();
                    gson = builder.create();
                }
            }
        }

        public Gson getGson() {
            return gson;
        }

        public String getName() {
            return typeName;
        }
    }

}
