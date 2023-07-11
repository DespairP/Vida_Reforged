package teamHTBP.vidaReforged.server.events;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.utils.reg.RegisterItemBlock;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.server.events.BlockItemAutoRegisterHandler.REGISTRY_ITEMBLOCK_MAP;
import static teamHTBP.vidaReforged.server.items.VidaItemLoader.VIDA_WAND;

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


    @SubscribeEvent
    public static void buildContents(final BuildCreativeModeTabContentsEvent event) {
        if(event.getTab() == TAB.get()){
            REGISTRY_ITEMBLOCK_MAP.forEach((key,blockItem) ->{
                event.accept(blockItem);
            });
            event.accept(VIDA_WAND.get());
            event.accept(VidaItemLoader.BREATH_CATCHER.get());
        }
    }




}
