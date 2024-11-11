package teamHTBP.vidaReforged.core.common.configs;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;

/**YAML构造ResourceLocation*/
public class ConstructResource extends AbstractConstruct {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Object construct(Node node) {
        try{
            return new ResourceLocation(((ScalarNode) node).getValue());
        }catch (Exception exception){
            LOGGER.error(exception);
        }
        return null;
    }
}
