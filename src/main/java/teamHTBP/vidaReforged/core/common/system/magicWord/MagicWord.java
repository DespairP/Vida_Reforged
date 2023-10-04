package teamHTBP.vidaReforged.core.common.system.magicWord;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.core.api.VidaElement;

/**
 * 词条
 * */
@Accessors(chain = true, fluent = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MagicWord {
    /**词条名字（id）*/
    private String name;
    /**翻译前缀*/
    @Expose(deserialize = false, serialize = false)
    private String namePrefix = "magic_word.name";
    /**词条ResourceLocation*/
    @Expose(deserialize = false, serialize = false)
    private ResourceLocation location;
    /**图标location*/
    private ResourceLocation icon;
    /**词条介绍*/
    private String description;
    /**词条所属元素*/
    private VidaElement element;
    /**出现比重*/
    private double chance;


    public Component getTranslateName(){
        return Component.translatable(String.format("%s.%s", namePrefix, name));
    }

    public Component getDescriptionComponent(){
        return Component.translatable(description);
    }

    public static int getIconSize(){
        return 16;
    }
}
