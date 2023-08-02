package teamHTBP.vidaReforged.server.providers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.core.common.VidaConstant;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MagicWordManager extends SimpleJsonResourceReloadListener {
    /**Logger*/
    public static final Logger LOGGER = LogManager.getLogger();
    /**gson*/
    private final Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);
    /***/
    private static Map<String,MagicWord> magicWordIdMap = new LinkedHashMap<>();

    public MagicWordManager() {
        super(JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL), VidaConstant.DATA_MAGIC_WORD);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        magicWordIdMap.clear();
        pObject.forEach(this::handleElement);
    }

    protected void handleElement(ResourceLocation location, JsonElement element){
        try{
            MagicWord word = gson.fromJson(element, MagicWord.class);
            final String modId = location.getNamespace();
            // 检查有没有id
            if(word.name() == null){
                LOGGER.error(String.format("%s has no value:name,will not register", location.toString()));
                return;
            }
            // 设置数据
            word.name(String.format("%s:%s", modId, word.name())).location(location);
            magicWordIdMap.put(word.name(),word);
        }catch (Exception ex){
            LOGGER.error(ex.getMessage());
        }
    }


    public static MagicWord getMagicWord(String magicWordId){
        return magicWordIdMap.getOrDefault(magicWordId,null);
    }

    public static List<MagicWord> getAllMagicWords(){
        return magicWordIdMap.values().stream().toList();
    }

    @OnlyIn(Dist.CLIENT)
    public static void setMagicWordIdMap(Map<String,MagicWord> magicWords){
        magicWordIdMap = new LinkedHashMap<>();
        magicWordIdMap.putAll(magicWords);
    }

    public static LinkedHashMap<String, MagicWord> getMagicWordIdMap() {
        return new LinkedHashMap<>(magicWordIdMap);
    }

    public static Set<String> getAllMagicWordIds(){
        return magicWordIdMap.keySet();
    }
}
