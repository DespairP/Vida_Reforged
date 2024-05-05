package teamHTBP.vidaReforged.core.common.system.guidebook;

import lombok.Data;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@Data
public class VidaPageDetail {
    ResourceLocation id;

    Component title;

    List<VidaPageComponents> pages;

    List<IVidaPageComponent> detailComponents;

    public VidaPageDetail(ResourceLocation id, Component title, List<VidaPageComponents> pages, List<IVidaPageComponent> detailComponents) {
        this.id = id;
        this.title = title;
        this.pages = pages;
        this.detailComponents = detailComponents;
    }
}
