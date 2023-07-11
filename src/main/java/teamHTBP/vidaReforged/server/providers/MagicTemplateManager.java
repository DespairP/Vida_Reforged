package teamHTBP.vidaReforged.server.providers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.VidaConstant;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 管理和注册所有魔法模板
 * */
public class MagicTemplateManager extends AbstractVidaManager{

    protected static Map<ResourceLocation, VidaMagic> magicResourceMap = new LinkedHashMap<>();
    protected static Map<String, VidaMagic> magicIdMap = new LinkedHashMap<>();

    public MagicTemplateManager() {
        super(VidaConstant.DATA_VIDA_MAGIC);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        magicResourceMap.clear();
        magicIdMap.clear();
        jsonElementMap.forEach(this::processElementMap);
    }

    protected void processElementMap(ResourceLocation location,JsonElement element){
        if(!element.isJsonObject()){
            LOGGER.warn(String.format("%s is not a valid magic", location.getPath()));
            return;
        }
        final JsonObject magicJsonObject = element.getAsJsonObject();
        final String modId = location.getNamespace();
        if(!magicJsonObject.has("name")){
            LOGGER.warn(String.format("%s has no valid magic name", location.getPath()));
            return;
        }
        VidaMagic magic = new VidaMagic(String.format("%s:%s", modId, magicJsonObject.get("name").getAsString()))
                .magicLocation(location)
                .magicType(!magicJsonObject.has("type") ? VidaMagic.VidaMagicType.NONE : VidaMagic.VidaMagicType.of(magicJsonObject.get("magicType").getAsString()))
                .icon(!magicJsonObject.has("icon") ? null : new ResourceLocation(magicJsonObject.get("icon").getAsString()))
                .iconIndex(!magicJsonObject.has("iconIndex") ? 0 : magicJsonObject.get("iconIndex").getAsInt())
                .element(!magicJsonObject.has("element") ? VidaElement.EMPTY : VidaElement.of(magicJsonObject.get("element").getAsString()))
                .isPlayerUsable(!magicJsonObject.has("usable") || magicJsonObject.get("usable").getAsBoolean())
                .description(magicJsonObject.has("description") ? Component.empty() : Component.translatable(magicJsonObject.get("description").getAsString()));
        magicIdMap.put(magic.magicName(), magic);
        magicResourceMap.put(location, magic);
    }

    public static Map<String,VidaMagic> getAllMagicAsString(){
        return magicIdMap;
    }

}
