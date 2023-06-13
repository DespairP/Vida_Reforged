package teamHTBP.vidaReforged.server.events;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.utils.reg.RegisterItemBlock;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.server.items.VidaItemLoader.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockItemAutoRegisterHandler {
    /**
     * 储存需要注册的方块
     */
    public static final Map<String, RegistryObject<Block>> REGISTRY_BLOCK_LIST = new LinkedHashMap<>();
    /**
     * 储存注册完成的ItemBlock,字段名称或者
     */
    public static final Map<String, RegistryObject<Item>> REGISTRY_ITEMBLOCK_MAP = new LinkedHashMap<>();
    /**
     * LOGGER
     */
    public static final Logger LOGGER = LogManager.getLogger();

    /**
     * 对方块进行BlockItem的注册
     * 分别调用{@link teamHTBP.vidaReforged.server.events.BlockItemAutoRegisterHandler#init()}进行需要注册的Block的获取，
     * 注入请用{@link net.minecraftforge.registries.ObjectHolder}
     */
    @SubscribeEvent
    public static void registerBlockItems(RegisterEvent event) throws IllegalAccessException {
        // 只有在forge注册item的时候才可以进行注册
        if(!event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)){
            return;
        }
        // 获取注册方块的字段
        init();
        // 依次进行注册
        REGISTRY_BLOCK_LIST.forEach((key, block) -> {
            ResourceLocation registerName = block.getId();
            if(!block.isPresent()){
                return;
            }
            BlockItem blockItem = new BlockItem(block.get(),new Item.Properties());

            event.register(ForgeRegistries.Keys.ITEMS, helper -> {
                helper.register(registerName, blockItem);
            });
            // 注册完成后放入Map中
            REGISTRY_ITEMBLOCK_MAP.put(key, RegistryObject.create(registerName, event.getForgeRegistry()));
        });
        // 进行注入
        //inject();
    }


    /**
     * 获取所有可以被注册的字段
     */
    private static void init() throws IllegalAccessException {
        for (Field decoratedBlock : VidaBlockLoader.class.getDeclaredFields()) {
            if (decoratedBlock.getType() == RegistryObject.class && decoratedBlock.isAnnotationPresent(RegisterItemBlock.class)) {
                decoratedBlock.setAccessible(true);
                REGISTRY_BLOCK_LIST.put(decoratedBlock.getName(), (RegistryObject<Block>) decoratedBlock.get(null));
            }
        }
    }
}
