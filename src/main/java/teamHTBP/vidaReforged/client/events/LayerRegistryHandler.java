package teamHTBP.vidaReforged.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.model.armors.LayerManager;
import teamHTBP.vidaReforged.client.model.armors.chestplate.BlackMetalChestPlate;
import teamHTBP.vidaReforged.client.model.armors.head.BlackMetalHelmet;
import teamHTBP.vidaReforged.client.model.armors.leggings.BlackMetalLeggings;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.client.events.ParticleProviderRegHandler.LOGGER;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MOD_ID, value = Dist.CLIENT)
public class LayerRegistryHandler {
    private static Map<ModelLayerLocation, Supplier<? extends Model>> modelSuppliers = new HashMap<>();

    @SubscribeEvent
    public static void onRegisterLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        register(event, BlackMetalLeggings.LAYER_LOCATION, LayerManager::createBodyLayer, BlackMetalLeggings.class);
        register(event, BlackMetalChestPlate.LAYER_LOCATION, LayerManager::createBodyLayer, BlackMetalChestPlate.class);
        register(event, BlackMetalHelmet.LAYER_LOCATION, LayerManager::createBodyLayer, BlackMetalHelmet.class);
    }

    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event, ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier,Class<? extends Model> modelClazz){
        event.registerLayerDefinition(layerLocation, supplier);
        try {
            Constructor<? extends Model> constructor = modelClazz.getConstructor(ModelPart.class);
            modelSuppliers.put( layerLocation, () -> {
                try {
                    return constructor.newInstance(Minecraft.getInstance().getEntityModels().bakeLayer(layerLocation));
                }catch (Exception exception){
                    LOGGER.error("model register exception:" + exception);
                }
                return null;
            });
        }catch (Exception exception){
            LOGGER.error("model register exception:" + exception);
        }

    }


    public static <T extends Model> Supplier<T> getModelSupplier(ModelLayerLocation layerLocation,Class<T> clazz){
        return (Supplier<T>) modelSuppliers.get(layerLocation);
    }
}
