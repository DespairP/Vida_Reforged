package teamHTBP.vidaReforged.core.common.system.guidebook;

import com.google.gson.annotations.Expose;
import lombok.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VidaPageList {
    /**list的id*/
    public ResourceLocation id;
    /**list介绍*/
    public Component description;
    /**是否启用搜索功能*/
    public boolean isSearchEnabled;
    /**是否有背景*/
    public boolean isBackgroundEnabled;
}
