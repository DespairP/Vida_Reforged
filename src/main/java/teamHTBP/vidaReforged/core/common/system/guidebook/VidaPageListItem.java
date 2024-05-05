package teamHTBP.vidaReforged.core.common.system.guidebook;

import lombok.Data;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

@Data
public class VidaPageListItem {
    /**详情的id*/
    ResourceLocation id;
    /**父节点*/
    List<VidaPageListItem> parents;
    /**父节点Id*/
    List<ResourceLocation> parentIds;
    /**具体处于哪个list中*/
    final ResourceLocation list;
    /**优先级*/
    int priority;
    /**详情的介绍*/
    final Component description;
    /**显示*/
    DisplayInfo info;
    /**点击事件*/
    VidaScreenEvent event;

    public VidaPageListItem(ResourceLocation id, List<ResourceLocation> parentIds, int priority, ResourceLocation list, Component description, DisplayInfo info, Optional<VidaScreenEvent>  event) {
        this.id = id;
        this.parentIds = parentIds;
        this.priority = priority;
        this.list = list;
        this.description = description;
        this.info = info;
        this.event = event.orElse(new VidaScreenEvent());
    }


    public Optional<VidaScreenEvent> getEvent() {
        return Optional.ofNullable(event);
    }
}
