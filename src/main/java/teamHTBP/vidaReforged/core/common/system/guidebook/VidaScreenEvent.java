package teamHTBP.vidaReforged.core.common.system.guidebook;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**页面事件*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VidaScreenEvent {
    /**事件类型，例如Close/Open*/
    public String type;
    /**事件需要传输的数据*/
    public JsonElement data;
}
