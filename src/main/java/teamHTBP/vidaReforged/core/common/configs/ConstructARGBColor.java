package teamHTBP.vidaReforged.core.common.configs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

import java.util.List;

/**YAML构造ARGB*/
public class ConstructARGBColor extends AbstractConstruct {
    private static final Logger LOGGER = LogManager.getLogger();
    @Override
    public Object construct(Node node) {
        try {
            return (Object) constructNode(node);
        } catch (Exception exception){
            LOGGER.error("ARGB", exception);
        }
        return ARGBColor.BLACK;
    }

    public static ARGBColor constructNode(Node node) {
        try {
            SequenceNode sequenceNode = (SequenceNode) node;
            List<Node> nodes = sequenceNode.getValue();

            List<Integer> colors = nodes.stream().map(nodeItem -> Integer.parseInt(((ScalarNode) nodeItem).getValue())).toList();
            return new ARGBColor(
                    colors.size() >= 1 ? colors.get(0) : 255,
                    colors.size() >= 2 ? colors.get(1) : 255,
                    colors.size() >= 3 ? colors.get(2) : 255,
                    colors.size() >= 4 ? colors.get(3) : 255
            );
        } catch (Exception exception){
            LOGGER.error("ARGB", exception);
        }
        return ARGBColor.BLACK;
    }
}
