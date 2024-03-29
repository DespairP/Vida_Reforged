package teamHTBP.vidaReforged.server.providers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.VidaConstant;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicContainer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        jsonElementMap.forEach(this::processSkillMap);
    }

    /**解析每个技能json文件*/
    protected void processSkillMap(ResourceLocation location,JsonElement element){
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
        VidaMagic magic = new VidaMagic(modId, magicJsonObject.get("name").getAsString())
                .regex(!magicJsonObject.has("regex") ? null : magicJsonObject.get("regex").getAsString())
                .path(location)
                .magicType(!magicJsonObject.has("type") ? null : magicJsonObject.get("magicType").getAsString())
                .spriteLocation(!magicJsonObject.has("sprite") ? null : new ResourceLocation(magicJsonObject.get("sprite").getAsString()))
                .spriteSize(!magicJsonObject.has("spriteSize") ? 192 : magicJsonObject.get("spriteSize").getAsInt())
                .iconIndex(!magicJsonObject.has("iconIndex") ? 0 : magicJsonObject.get("iconIndex").getAsInt())
                .iconSize(!magicJsonObject.has("iconSize") ? 32 : magicJsonObject.get("spriteSize").getAsInt())
                .element(!magicJsonObject.has("element") ? VidaElement.EMPTY : VidaElement.of(magicJsonObject.get("element").getAsString()))
                .isPlayerUsable(!magicJsonObject.has("usable") || magicJsonObject.get("usable").getAsBoolean())
                .description(magicJsonObject.has("description") ? Component.empty() : Component.translatable(magicJsonObject.get("description").getAsString()));
        magicIdMap.put(magic.magicId(), magic);
        magicResourceMap.put(location, magic);
    }

    @Deprecated
    public static Map<String,VidaMagic> getAllMagicAsString(){
        return magicIdMap;
    }

    public static VidaMagic getMagicById(String magicId){
        return magicIdMap.getOrDefault(magicId,null);
    }

    public static Map<String,VidaMagic> getMagicByIds(List<String> magicIdList){
        return
                magicIdMap.values()
                        .stream()
                        .filter(magic -> magicIdList.contains(magic.magicId()))
                        .collect(Collectors.toMap(VidaMagic::magicId, vidaMagic -> vidaMagic));
    }

    public static LinkedHashMap<String, VidaMagic> getMagicIdMap() {
        return new LinkedHashMap<>(magicIdMap);
    }

    @OnlyIn(Dist.CLIENT)
    public static void setMagicIdMap(LinkedHashMap<String, VidaMagic> magics){
        magicIdMap = new LinkedHashMap<>();
        magicIdMap.putAll(magics);
        magicResourceMap = magicIdMap.values().stream().collect(Collectors.toMap(VidaMagic::path,vidaMagic -> vidaMagic, (a,b) -> a));
    }

    public static Set<String> getMagicsKey(){
        return magicIdMap.keySet();
    }

}
