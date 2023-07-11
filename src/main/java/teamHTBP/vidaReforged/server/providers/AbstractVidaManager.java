package teamHTBP.vidaReforged.server.providers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;

import java.util.Map;

public abstract class AbstractVidaManager extends SimpleJsonResourceReloadListener {
    /**Logger*/
    protected static final Logger LOGGER = LogManager.getLogger();
    /**GSON*/
    protected static final Gson gson = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL);

    public AbstractVidaManager(String path) {
        super(gson, path);
    }

}
