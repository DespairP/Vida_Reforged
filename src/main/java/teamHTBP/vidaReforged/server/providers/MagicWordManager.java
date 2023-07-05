package teamHTBP.vidaReforged.server.providers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.core.common.VidaConstant;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;

import java.util.Map;

public class MagicWordManager extends SimpleJsonResourceReloadListener {

    /**Logger*/
    public static final Logger LOGGER = LogManager.getLogger();
    /**gson*/
    private final Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);

    public MagicWordManager() {
        super(JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL), VidaConstant.DATA_MAGIC_WORD);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {

    }



}
