package teamHTBP.vidaReforged.server.events;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.utils.reg.RegisterGroup;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import java.lang.reflect.Field;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.server.events.BlockItemAutoRegisterHandler.LOGGER;
import static teamHTBP.vidaReforged.server.events.BlockItemAutoRegisterHandler.REGISTRY_ITEMBLOCK_MAP;
import static teamHTBP.vidaReforged.server.items.VidaItemLoader.VIDA_WAND;
import static teamHTBP.vidaReforged.server.items.VidaItemLoader.VIVID_BUCKET;

/**
 * 创造物品栏注册
 * */
@Mod.EventBusSubscriber(modid = "vida_reforged", bus = Mod.EventBusSubscriber.Bus.MOD)
public class VidaItemGroupLoader {
    /**注册器*/
    public final static DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VidaReforged.MOD_ID);
    /**Vida创造模式标签*/
    public static RegistryObject<CreativeModeTab> TAB = CREATIVE_TAB.register("vida_reforged", () ->CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .title(Component.translatable("Vida"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(VIDA_WAND.get()))
            // Add default items to tab
            .build()
    );
    /**LOGGER*/
    public static final Logger LOGGER = LogManager.getLogger();


    @SubscribeEvent
    public static void buildContents(final BuildCreativeModeTabContentsEvent event) {
        if(event.getTab() == TAB.get()){
            REGISTRY_ITEMBLOCK_MAP.forEach((key,blockItem) ->{
                event.accept(blockItem);
            });
            event.accept(VIDA_WAND.get());
            event.accept(VidaItemLoader.BREATH_CATCHER.get());
            event.accept(VidaItemLoader.GOLD_ELEMENT_CORE.get());
            event.accept(VidaItemLoader.WOOD_ELEMENT_CORE.get());
            event.accept(VidaItemLoader.AQUA_ELEMENT_CORE.get());
            event.accept(VidaItemLoader.FIRE_ELEMENT_CORE.get());
            event.accept(VidaItemLoader.EARTH_ELEMENT_CORE.get());
            event.accept(VidaItemLoader.GOLD_GEM.get());
            event.accept(VidaItemLoader.WOOD_GEM.get());
            event.accept(VidaItemLoader.AQUA_GEM.get());
            event.accept(VidaItemLoader.FIRE_GEM.get());
            event.accept(VidaItemLoader.EARTH_GEM.get());
            event.accept(VidaItemLoader.CRIMSON_CREST_SEED_ITEM.get());
            event.accept(VidaItemLoader.PLAM_STEM_SEED_ITEM.get());
            event.accept(VidaItemLoader.HEART_OF_WAL_SEED_ITEM.get());
            event.accept(VidaItemLoader.NITRITE_THORNS_SEED_ITEM.get());
            event.accept(VidaItemLoader.SULLEN_HYDRANGEA_SEED_ITEM.get());
            event.accept(VidaItemLoader.SWEET_CYAN_REED_SEED_ITEM.get());
            event.accept(VidaItemLoader.FRIED_CRIMSON_CREST.get());
            event.accept(VidaItemLoader.HEART_OF_WAL_JUICE.get());
            event.accept(VidaItemLoader.PLAM_STEM_TEA.get());
            event.accept(VidaItemLoader.PROCESSED_SULLEN_HYDRANGEA_BERRY.get());
            event.accept(VidaItemLoader.SULLEN_HYDRANGEA_SOUP.get());
            event.accept(VidaItemLoader.NITRITE_TEA.get());
            event.accept(VidaItemLoader.DRIED_SWEET_CYAN_REED.get());
            event.accept(VidaItemLoader.BLACK_METAL_HELMET.get());
            event.accept(VidaItemLoader.BLACK_METAL_CHESTPLATE.get());
            event.accept(VidaItemLoader.BLACK_METAL_BOOTS.get());
            event.accept(VidaItemLoader.APPRENTICE_HELMET.get());
            event.accept(VidaItemLoader.APPRENTICE_CHESTPLATE.get());
            event.accept(VidaItemLoader.APPRENTICE_LEGGINGS.get());
            event.accept(VidaItemLoader.APPRENTICE_BOOTS.get());
            event.accept(VidaItemLoader.TEST_EQUIPMENT.get());
            event.accept(VidaItemLoader.TEST_CORE_EQUIPMENT.get());
            event.accept(VidaItemLoader.HEART_OF_WOOD.get());
            event.accept(VidaItemLoader.HEART_OF_WOOD_CORE.get());
            event.accept(VidaItemLoader.HEART_OF_WOOD_CENTER.get());
            event.accept(VidaItemLoader.HEART_OF_WOOD_BOTTOM.get());
            event.accept(VidaItemLoader.VIVID_BUCKET.get());
            event.accept(VidaItemLoader.VIDA_LEAVES.get());
            event.accept(VidaItemLoader.VIDA_BLUE_LEAVES.get());
            event.accept(VidaItemLoader.VIDA_GRASS);
            init(event);
        }
    }


    /**
     * 获取所有可以被注册的字段
     */
    private static void init(final BuildCreativeModeTabContentsEvent event) {
        try {
            for (Field decoratedBlock : VidaItemLoader.class.getDeclaredFields()) {
                if (decoratedBlock.getType() == RegistryObject.class && decoratedBlock.isAnnotationPresent(RegisterGroup.class)) {
                    decoratedBlock.setAccessible(true);
                    event.accept(((RegistryObject<Item>)decoratedBlock.get(null)).get());
                }
            }
        }catch (IllegalArgumentException | IllegalAccessException exception){
            LOGGER.error(exception);
        }
    }

}
