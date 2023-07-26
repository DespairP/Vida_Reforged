package teamHTBP.vidaReforged.core.common.system.guidebook;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.api.screen.IGuideBookSection;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;

import java.lang.reflect.Type;
import java.util.Map;

public class TeaconGuideBookSectionSerializer implements JsonSerializer<IGuideBookSection>, JsonDeserializer<IGuideBookSection> {
    /**LOGGER*/
    private static final Logger LOGGER = LogManager.getLogger();
    /**Map*/
    private static final Map<String,Class<? extends IGuideBookSection>> COMPONENT_MAP = ImmutableMap.of(
            "TEXT", TeaconGuideBookTextSection.class,
            "EMPTY", TeaconGuideBookEmptySection.class,
            "BLOCK", TeaconGuideBookBlockModelSection.class,
            "BLOCKLIST", TeaconGuideBookBlockModelList.class
    );

    Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(ResourceLocation.class,new ResourceLocation.Serializer())
            .create();

    @Override
    public IGuideBookSection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //
        JsonObject component = json.getAsJsonObject();
        //
        JsonElement componentTypeElement = component.get("type");
        if(componentTypeElement == null){
            LOGGER.error("componentType is required!please check the component string:{}", component.toString());
            throw new JsonParseException("componentType is required! please check the component");
        }
        String componentType = componentTypeElement.getAsString();
        Class<? extends IGuideBookSection> clazz = COMPONENT_MAP.get(componentType);
        return gson.fromJson(json, clazz);
    }

    @Override
    public JsonElement serialize(IGuideBookSection section, Type typeOfSrc, JsonSerializationContext context) {
        Class<? extends IGuideBookSection> guideBookComponentClazz = COMPONENT_MAP.get(section.getType());
        if(guideBookComponentClazz == null){
            LOGGER.error("cannot find componentType!please check the component type:{}", section.toString());
            throw new JsonParseException("cannot find componentType!please check the component type");
        }
        Type componentType = TypeToken.get(guideBookComponentClazz).getType();
        return context.serialize(section, componentType);
    }
}
