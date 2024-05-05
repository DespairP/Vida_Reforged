package teamHTBP.vidaReforged.core.common.system.guidebook;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@Data
@AllArgsConstructor
@Accessors(fluent = true,chain = true)
public class TeaconGuideBook {
    /**guidebook的Id*/
    @Expose(serialize = false,deserialize = false)
    private transient ResourceLocation guideBookId;

    private String id;
    /**主要标题*/
    private String titleKey;
    /**页*/
    private List<TeaconGuideBookPage> pages = new ArrayList<>();

    /**获取标题*/
    public Component getTitle(){
        return Component.translatable(titleKey);
    }

    /**页数*/
    public int getPageCount(){
        return pages.size();
    }
}
