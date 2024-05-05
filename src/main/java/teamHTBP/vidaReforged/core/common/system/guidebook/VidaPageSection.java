package teamHTBP.vidaReforged.core.common.system.guidebook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.resources.ResourceLocation;

/**在进入选择Section页面后，每个Item项目*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VidaPageSection {
    /**section名字*/
    public String name;
    /**section图标*/
    public ResourceLocation icon;
    /**点击事件*/
    public VidaScreenEvent click;
}
