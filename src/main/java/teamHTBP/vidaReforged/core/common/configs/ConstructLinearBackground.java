package teamHTBP.vidaReforged.core.common.configs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.nodes.*;
import teamHTBP.vidaReforged.client.shaders.GradientShader;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

import java.util.ArrayList;
import java.util.List;

/**YAML构造Gradient*/
public class ConstructLinearBackground extends AbstractConstruct {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Object construct(Node node) {
        try {
            MappingNode mappingNode = (MappingNode) node;

            float angle = 0;
            List<Float> stops = new ArrayList<>();
            List<ARGBColor> colors = new ArrayList<>();

            for(NodeTuple attributeNode : mappingNode.getValue()){
                ScalarNode attributeName = (ScalarNode) attributeNode.getKeyNode();

                switch (attributeName.getValue()){
                    case "angle" -> {
                        ScalarNode attributeValue = (ScalarNode) attributeNode.getValueNode();
                        angle = Float.parseFloat(attributeValue.getValue());
                    }
                    case "path" -> {
                        SequenceNode attributeValue = (SequenceNode) attributeNode.getValueNode();
                        stops = attributeValue.getValue().stream().map(nodeV -> Float.parseFloat(((ScalarNode)nodeV).getValue())).toList();
                    }
                    case "colors" -> {
                        SequenceNode attributeValue = (SequenceNode) attributeNode.getValueNode();
                        colors = attributeValue.getValue().stream().map(nodeV -> ConstructARGBColor.constructNode(nodeV)).toList();
                    }
                }
            }
            GradientShader.LinearGradient gradient = new GradientShader.LinearGradient(angle);
            for(int i = 0; i < colors.size(); i++){
                gradient.addColor(stops.get(i), colors.get(i));
            }
            return gradient;
        } catch (Exception exception) {
            LOGGER.error(exception);
        }
        return new Object();
    }
}
