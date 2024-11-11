package teamHTBP.vidaReforged.client.providers;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScreenComponentStyleSheet {
    private ResourceLocation screenId;
    private Map<String, Map<String, Object>> styles;

    public ScreenComponentStyleSheet(ResourceLocation screenId) {
        this.screenId = screenId;
        this.styles = new LinkedHashMap<>();
    }

    /**添加组件*/
    public void addComponentAndAttribute(String componentId, String attribute, Object attributeValue){
        Map<String, Object> attributeMap = styles.getOrDefault(componentId, new HashMap<>());
        attributeMap.put(attribute, attributeValue);
        styles.put(componentId, attributeMap);
    }

    public Map<String, Map<String, Object>> getStyles() {
        return styles;
    }
}
