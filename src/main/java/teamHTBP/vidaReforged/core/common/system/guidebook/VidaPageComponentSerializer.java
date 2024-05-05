package teamHTBP.vidaReforged.core.common.system.guidebook;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.utils.json.ItemStackSerializer;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 指导书组件json序列化类 <br/>
 * <pre>
 * {@link VidaPageSection} - 分类
 * {@link VidaPageList} - 进入分类后，显示的列表
 * {@link VidaPageListItem} - 列表中的一项
 * {@link VidaPageDetail} - 项目打开后，显示的信息
 * {@link VidaScreenEvent} - 定义事件
 * </pre>
 */
public class VidaPageComponentSerializer implements JsonSerializer<IVidaPageComponent>,JsonDeserializer<IVidaPageComponent> {
    /**LOGGER*/
    private static final Logger LOGGER = LogManager.getLogger();
    /**GSON*/
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
            .registerTypeHierarchyAdapter(Component.class, new Component.Serializer())
            .serializeNulls()
            .create();
    /**Type对应的类*/
    private static final Map<String,Class<? extends IVidaPageComponent>> FACTORIES = Map.of(
            "title", VidaPageBaseComponent.VidaTitleComponent.class,
            "back", VidaPageBaseComponent.VidaBackButtonComponent.class,
            "text", VidaPageBaseComponent.VidaTextAreaComponent.class,
            "white", VidaPageBaseComponent.VidaWhiteComponent.class,
            "displayItem", VidaPageBaseComponent.VidaDisplayComponent.class,
            "pagination", VidaPageBaseComponent.VidaPaginationComponent.class,
            "recipe_simple", VidaPageBaseComponent.VidaRecipeComponent.class
    );

    private static final Gson NORMAL_GSON = new GsonBuilder().registerTypeAdapter(IVidaPageComponent.class, new VidaPageComponentSerializer()).create();

    public final static Codec<IVidaPageComponent> CODEC = ExtraCodecs.JSON.xmap((json) -> NORMAL_GSON.fromJson(json, IVidaPageComponent.class), NORMAL_GSON::toJsonTree);

    /**反序列化guidebook component*/
    @Override
    public IVidaPageComponent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        //
        JsonObject component = jsonElement.getAsJsonObject();
        //
        JsonElement componentTypeElement = component.get("type");
        if(componentTypeElement == null){
            LOGGER.error("componentType is required!please check the component string:{}", component.toString());
            throw new JsonParseException("componentType is required!please check the component");
        }
        String componentType = componentTypeElement.getAsString();
        Class<? extends IVidaPageComponent> clazz = getComponentByTypeName(componentType);

        return GSON.fromJson(jsonElement, clazz);
    }

    /**序列化*/
    @Override
    public JsonElement serialize(IVidaPageComponent iGuidebookComponent, Type type, JsonSerializationContext jsonSerializationContext) {
        Class<? extends IVidaPageComponent> guideBookComponentClazz = getComponentByTypeName(iGuidebookComponent.getType());
        if(guideBookComponentClazz == null){
            LOGGER.error("cannot find componentType!please check the component type:{}", iGuidebookComponent.toString());
            throw new JsonParseException("cannot find componentType!please check the component type");
        }
        Type componentType = TypeToken.get(guideBookComponentClazz).getType();
        return jsonSerializationContext.serialize(iGuidebookComponent, componentType);
    }

    /**根据type来获取组件类型*/
    public Class<? extends IVidaPageComponent> getComponentByTypeName(String type) {
        return FACTORIES.get(type);
    }
}
