package teamHTBP.vidaReforged.helper;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.client.events.registries.LayerRegistryHandler;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class VidaModelHelper {

    /**获取model*/
    public static <T extends Model> T getModel(ModelLayerLocation layerLocation, Class<T> clazz){
        return LayerRegistryHandler.getModelSupplier(layerLocation, clazz).get();
    }
}
