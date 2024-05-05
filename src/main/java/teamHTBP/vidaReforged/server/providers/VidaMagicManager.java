package teamHTBP.vidaReforged.server.providers;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.core.common.VidaConstant;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理和注册所有魔法模板
 * */
public class VidaMagicManager extends AbstractVidaManager{
    protected static Map<ResourceLocation, VidaMagic> magicResourceMap = new LinkedHashMap<>();
    public VidaMagicManager() {
        super(VidaConstant.DATA_VIDA_MAGIC);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        magicResourceMap.clear();
        jsonElementMap.forEach(this::processSkillMap);
    }

    /**解析每个技能json文件*/
    protected void processSkillMap(ResourceLocation location,JsonElement element){
        if(!element.isJsonObject()){
            LOGGER.warn(String.format("%s is not a valid magic", location.getPath()));
            return;
        }
        try {
            VidaMagic magic = VidaMagic.codec.parse(JsonOps.INSTANCE, element).getOrThrow(true, message -> LOGGER.error("vida magic parse error, message: {}" , message));
            magic.magicId(location);
            magicResourceMap.put(location, magic);
        }catch (Exception ex){
            LOGGER.error("vida magic parse error, message: {}" , ex.getMessage());
        }
    }


    public static Map<ResourceLocation,VidaMagic> getMagicByIds(List<ResourceLocation> magicIdList){
        return
                VidaMagicManager.magicResourceMap.values()
                        .stream()
                        .filter(magic -> magicIdList.contains(magic.magicId()))
                        .collect(Collectors.toMap(VidaMagic::magicId, vidaMagic -> vidaMagic));
    }

    public static LinkedHashMap<ResourceLocation, VidaMagic> getMagicIdMap() {
        return new LinkedHashMap<>(magicResourceMap);
    }

    @OnlyIn(Dist.CLIENT)
    public static void setMagicIdMap(LinkedHashMap<ResourceLocation, VidaMagic> magics){
        VidaMagicManager.magicResourceMap = magics;
    }

    public static VidaMagic getMagicByMagicId(ResourceLocation id){
        return magicResourceMap.get(id);
    }

    public static Set<ResourceLocation> getMagicsKey(){
        return magicResourceMap.keySet();
    }

}
