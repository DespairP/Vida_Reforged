package teamHTBP.vidaReforged.client.events.registries;

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
import teamHTBP.vidaReforged.client.model.armors.boot.VidaApprenticeBoots;
import teamHTBP.vidaReforged.client.model.armors.chestplate.BlackMetalChestPlate;
import teamHTBP.vidaReforged.client.model.armors.chestplate.VidaBasedChestPlate;
import teamHTBP.vidaReforged.client.model.armors.head.BlackMetalHelmet;
import teamHTBP.vidaReforged.client.model.armors.head.VidaBasedHelmet;
import teamHTBP.vidaReforged.client.model.armors.leggings.BlackMetalLeggings;
import teamHTBP.vidaReforged.client.model.armors.leggings.VidaBasedLeggings;
import teamHTBP.vidaReforged.client.model.blockModel.InjectTableModel;
import teamHTBP.vidaReforged.client.model.itemModel.VidaWandModel;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.client.events.registries.ParticleProviderAutoRegistryHandler.LOGGER;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MOD_ID, value = Dist.CLIENT)
public class LayerRegistryHandler {
    private static Map<ModelLayerLocation, Supplier<? extends Model>> modelSuppliers = new HashMap<>();

    @SubscribeEvent
    public static void onRegisterLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        register(event, BlackMetalLeggings.LAYER_LOCATION, LayerManager::createMetalBodyLayer, BlackMetalLeggings.class);
        register(event, BlackMetalChestPlate.LAYER_LOCATION, LayerManager::createMetalBodyLayer, BlackMetalChestPlate.class);
        register(event, BlackMetalHelmet.LAYER_LOCATION, LayerManager::createMetalBodyLayer, BlackMetalHelmet.class);
        register(event, VidaBasedHelmet.APPRENTICE_LAYER_LOCATION, LayerManager::createApprenticeBodyLayer, VidaBasedHelmet.class);
        register(event, VidaBasedChestPlate.APPRENTICE_LAYER_LOCATION, LayerManager::createApprenticeBodyLayer, VidaBasedChestPlate.class);
        register(event, VidaBasedLeggings.APPRENTICE_LAYER_LOCATION, LayerManager::createApprenticeBodyLayer, VidaBasedLeggings.class);
        register(event, VidaApprenticeBoots.APPRENTICE_LAYER_LOCATION, LayerManager::createApprenticeBodyLayer, VidaApprenticeBoots.class);
        register(event, InjectTableModel.APPRENTICE_LAYER_LOCATION, InjectTableModel::createBodyLayer, InjectTableModel.class);
        register(event, VidaWandModel.LAYER_LOCATION, VidaWandModel::createWandBaseLayer, VidaWandModel.class);
        register(event, VidaWandModel.TEST_LAYER_LOCATION, VidaWandModel::createTestLayer, VidaWandModel.class);
        register(event, VidaWandModel.TEST_CORE_LAYER_LOCATION, VidaWandModel::createTestCoreLayer, VidaWandModel.class);
        register(event, VidaWandModel.STYLE_001_TOP_LAYER_LOCATION, VidaWandModel::createStyle_001_TopLayer, VidaWandModel.class);
        register(event, VidaWandModel.STYLE_001_CORE_LAYER_LOCATION, VidaWandModel::createStyle_001_CoreLayer, VidaWandModel.class);
        register(event, VidaWandModel.STYLE_001_CENTER_LAYER_LOCATION, VidaWandModel::createStyle_001_CenterLayer, VidaWandModel.class);
        register(event, VidaWandModel.STYLE_001_BOTTOM_LAYER_LOCATION, VidaWandModel::createStyle_001_BottomLayer, VidaWandModel.class);
    }


    /**
     * 注册模型layer
     * @param event 注册事件
     * @param layerLocation 模型路径
     * @param supplier 模型构建方法
     * @param modelClazz
     */
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


    public static <T extends Model> Supplier<T> getModelSupplier(ModelLayerLocation layerLocation, Class<T> clazz){
        return (Supplier<T>) modelSuppliers.get(layerLocation);
    }
}
