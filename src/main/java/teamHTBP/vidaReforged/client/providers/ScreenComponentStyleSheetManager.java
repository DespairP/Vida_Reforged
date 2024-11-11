package teamHTBP.vidaReforged.client.providers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.*;
import teamHTBP.vidaReforged.core.common.configs.ConstructARGBColor;
import teamHTBP.vidaReforged.core.common.configs.ConstructLinearBackground;
import teamHTBP.vidaReforged.core.common.configs.ConstructResource;
import teamHTBP.vidaReforged.core.common.record.SimpleYamlResourceReloadListener;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScreenComponentStyleSheetManager extends SimpleYamlResourceReloadListener {
    private static final Map<ResourceLocation, ScreenComponentStyleSheet> styles = new LinkedHashMap<>();
    private static final SingleConstructor CONSTRUCTOR = new SingleConstructor(new LoaderOptions());
    private static final Tag COLOR = new Tag("!color");
    private static final ConstructARGBColor ARGB_COLOR = new ConstructARGBColor();
    private static final Tag LINEAR_GRADIENT = new Tag("!linear-gradient");
    private static final ConstructLinearBackground LINEAR_GRADIENT_CONSTRUCT = new ConstructLinearBackground();
    private static final Tag RESOURCE = new Tag("!res");
    private static final ConstructResource RESOURCE_CONSTRUCT = new ConstructResource();
    public ScreenComponentStyleSheetManager() {
        super("stylesheets");
        CONSTRUCTOR.addConstructor(COLOR, ARGB_COLOR);
        CONSTRUCTOR.addConstructor(LINEAR_GRADIENT, LINEAR_GRADIENT_CONSTRUCT);
        CONSTRUCTOR.addConstructor(RESOURCE, RESOURCE_CONSTRUCT);
    }

    @Override
    protected void loadYamlConfig(ResourceLocation resourceLocation, Node node, ResourceManager manager, ProfilerFiller filler) {
        try{
            MappingNode allComponentIds = (MappingNode) node;
            ScreenComponentStyleSheet screenComponentStyle = new ScreenComponentStyleSheet(resourceLocation);

            for(NodeTuple identify : allComponentIds.getValue()){
                // 读取组件Id
                String key = ((ScalarNode) identify.getKeyNode()).getValue();
                // 读取组件Id下所有的定义
                MappingNode value = (MappingNode) identify.getValueNode();
                for(NodeTuple attribute : value.getValue()){
                    // 获取定义和定义的内容
                    String attributeKey = ((ScalarNode) attribute.getKeyNode()).getValue();
                    Node attributeValueNode = attribute.getValueNode();
                    Tag attributeValueNodeTag = attributeValueNode.getTag();

                    Object attributeValue = null;
                    if(attributeValueNodeTag != null){
                        attributeValue = CONSTRUCTOR.parse(attributeValueNodeTag, attributeValueNode);
                    } else {
                        switch (attribute.getValueNode().getNodeId()){
                            case scalar -> {attributeValue = ((ScalarNode) attribute.getValueNode()).getValue();}
                            case sequence -> {attributeValue = ((SequenceNode) attribute.getValueNode()).getValue();}
                        }
                    }
                    screenComponentStyle.addComponentAndAttribute(key, attributeKey, attributeValue);
                }

                styles.put(resourceLocation, screenComponentStyle);
            }
        }catch (Exception exception){
            LOGGER.error(exception);
        }
    }

    public static ScreenComponentStyleSheet getStyleSheetById(ResourceLocation id){
        return styles.get(id);
    }


    public static class SingleConstructor extends SafeConstructor{
        /**
         * Create an instance
         *
         * @param loaderOptions - the configuration options
         */
        public SingleConstructor(LoaderOptions loaderOptions) {
            super(loaderOptions);
        }

        public Object parse(Tag tag, Node node){
            if(!this.yamlConstructors.containsKey(tag)){
                return null;
            }
            return this.yamlConstructors.get(tag).construct(node);
        }

        public void addConstructor(Tag tag, Construct constructor){
            this.yamlConstructors.put(tag, constructor);
        }
    }
}
