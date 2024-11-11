package teamHTBP.vidaReforged.core.common.record;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.gson.JsonParseException;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class SimpleTomlReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, CommentedFileConfig>> {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final String directory;

    public SimpleTomlReloadListener(String directory) {
        this.directory = directory;
    }

    @Override
    protected Map<ResourceLocation, CommentedFileConfig> prepare(ResourceManager manager, ProfilerFiller filler) {
        Map<ResourceLocation, CommentedFileConfig> map = new HashMap<>();
        scanDirectory(manager, this.directory, map);
        return map;
    }

    @Override
    protected void apply(Map<ResourceLocation, CommentedFileConfig> resource, ResourceManager manager, ProfilerFiller filler) {

    }

    public static void scanDirectory(ResourceManager p_279308_, String dictionary, Map<ResourceLocation, CommentedFileConfig> map) {
        FileToIdConverter filetoidconverter = FileToIdConverter.json(dictionary);

        for(Map.Entry<ResourceLocation, Resource> entry : filetoidconverter.listMatchingResources(p_279308_).entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            ResourceLocation resourcelocation1 = filetoidconverter.fileToId(resourcelocation);

            try (CommentedFileConfig config = CommentedFileConfig.of(entry.getKey().getPath())) {
                map.put(resourcelocation1, config);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                LOGGER.error("Couldn't parse data file {} from {}", resourcelocation1, resourcelocation, jsonparseexception);
            }
        }

    }

}
