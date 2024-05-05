package teamHTBP.vidaReforged.client.events;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.server.blocks.VidaFluidsLoader;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 用于指定方块或者物品渲染类型的事件注册类
 * @author Despair
 * */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class RenderTypeRegisterHandler {

    /**
     * 储存需要注册的方块
     */
    public static final Map<CustomModelRenderType, List<RegistryObject<Block>>> REGISTRY_BLOCK_LIST = new LinkedHashMap<>();
    /**
     * LOGGER
     */
    public static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onFluidRender(FMLClientSetupEvent event){
        ItemBlockRenderTypes.setRenderLayer(VidaFluidsLoader.VIVID_FLUID_STILL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(VidaFluidsLoader.VIVID_FLUID_FLOW.get(), RenderType.translucent());
    }

    /**渲染类型*/
    public enum CustomModelRenderType{
        CUTOUT(RenderType::cutout),
        SOLID(RenderType::solid),
        CUTOUT_MIPPED(RenderType::cutoutMipped),
        TRANSLUCENT(RenderType::translucent),
        TRANSLUCENT_MOVING_BLOCK(RenderType::translucentMovingBlock),
        TRANSLUCENT_NO_CRUMBLING(RenderType::translucentNoCrumbling);

        private Supplier<RenderType> type;

        CustomModelRenderType(Supplier<RenderType> type){
            this.type = type;
        }

        public RenderType getRenderType(){
            return type.get();
        }
    }
}
