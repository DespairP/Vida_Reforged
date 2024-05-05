package teamHTBP.vidaReforged.core.common.system.guidebook;

import lombok.Data;

import java.util.List;

/** 每页中的组件集合 */
@Data
public class VidaPageComponents {
    /*第几页*/
    int page;
    /**组件*/
    List<IVidaPageComponent> components;
    /**出现事件*/
    VidaScreenEvent init;

    public VidaPageComponents(int page, List<IVidaPageComponent> components) {
        this.page = page;
        this.components = components;
    }
}
