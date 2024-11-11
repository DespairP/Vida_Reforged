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
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimpleYamlResourceReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, Node>> {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected final String directory;
    public static final Yaml YAML = new Yaml();

    public SimpleYamlResourceReloadListener(String directory) {
        this.directory = directory;
    }

    @Override
    protected Map<ResourceLocation, Node> prepare(ResourceManager manager, ProfilerFiller filler) {
        Map<ResourceLocation, Node> map = new HashMap<>();
        scanDirectory(manager, this.directory, map);
        return map;
    }

    @Override
    protected void apply(Map<ResourceLocation, Node> resource, ResourceManager manager, ProfilerFiller filler) {
        resource.forEach((fileId, yml) -> loadYamlConfig(fileId, yml, manager, filler));
    }

    protected void loadYamlConfig(ResourceLocation resourceLocation, Node yaml, ResourceManager manager, ProfilerFiller filler ){
    }

    public static void scanDirectory(ResourceManager p_279308_, String dictionary, Map<ResourceLocation, Node> map) {
        FileToIdConverter filetoidconverter = new FileToIdConverter(dictionary, ".yaml");

        for(Map.Entry<ResourceLocation, Resource> entry : filetoidconverter.listMatchingResources(p_279308_).entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            ResourceLocation resourcelocationFile = filetoidconverter.fileToId(resourcelocation);

            try {
                map.put(resourcelocationFile, YAML.compose(entry.getValue().openAsReader()));
            } catch (IllegalArgumentException | IOException | JsonParseException jsonparseexception) {
                LOGGER.error("Couldn't parse data file {} from {}", resourcelocationFile, resourcelocation, jsonparseexception);
            }
        }

    }
}
