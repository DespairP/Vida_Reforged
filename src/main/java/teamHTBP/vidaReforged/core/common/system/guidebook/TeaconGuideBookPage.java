package teamHTBP.vidaReforged.core.common.system.guidebook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.core.api.screen.IGuideBookSection;

@Data
@AllArgsConstructor
@Accessors(fluent = true,chain = true)
public class TeaconGuideBookPage {
    /**page的Id*/
    private String pageId;
    /**副标题*/
    private String subTitleKey;
    /***/
    private IGuideBookSection left;

    private IGuideBookSection right;



    public Component getSubTitle(){
        return Component.translatable(subTitleKey);
    }
}
