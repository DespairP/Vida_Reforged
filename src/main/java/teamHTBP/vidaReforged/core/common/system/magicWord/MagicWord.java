package teamHTBP.vidaReforged.core.common.system.magicWord;

import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.core.api.VidaElement;

/**词条*/
public class MagicWord {
    /**词条名字*/
    public String name;
    /**词条介绍*/
    public String description;
    /**词条所属元素*/
    public VidaElement element;
    /**出现比重*/
    public String weight;

    public Component getTranslateName(){
        return Component.translatable(name);
    }

    public Component getTranslateDescription(){
        return Component.translatable(description);
    }
}
