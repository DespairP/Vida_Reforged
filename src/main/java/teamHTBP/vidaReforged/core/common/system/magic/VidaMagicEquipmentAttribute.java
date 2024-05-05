package teamHTBP.vidaReforged.core.common.system.magic;

import lombok.Data;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.client.events.registries.LayerRegistryHandler;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.item.Position;

/**
 * 饰品所有基础属性
 *
 * */
@Data
public class VidaMagicEquipmentAttribute {
    /**饰品提供的魔法，如果没有就是EMPTY或者空*/
    private String magicId = "";
    /**饰品能消耗的能量*/
    private double manaBaseCost = 0;
    /**饰品消耗系数*/
    private double factor = 1;
    /**饰品所属元素*/
    private VidaElement element = VidaElement.EMPTY;
    /**饰品对应的模型路径，{@link LayerRegistryHandler}*/
    private ResourceLocation modelLayerLocation;
    /**饰品对应的模型材质*/
    private ResourceLocation modelTexture;
    /**饰品影响的发射物个数*/
    private int shootBaseCount = 1;
    /**饰品位置*/
    private Position position;
}
