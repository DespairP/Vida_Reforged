package teamHTBP.vidaReforged.server.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.server.events.BlockItemAutoRegisterHandler.REGISTRY_ITEMBLOCK_MAP;
import static teamHTBP.vidaReforged.server.items.VidaItemLoader.VIDA_WAND;

@Mod.EventBusSubscriber(modid = "vida_reforged", bus = Mod.EventBusSubscriber.Bus.MOD)
public class VidaItemGroupLoader {
    public static CreativeModeTab TAB = CreativeModeTab.builder()
            .withTabsAfter(CreativeModeTabs.SEARCH)
            .title(Component.translatable("item_group." + MOD_ID + ".vida_tab"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(VIDA_WAND.get()))
            // Add default items to tab
            .build();


    @SubscribeEvent
    public static void buildContents(final BuildCreativeModeTabContentsEvent event) {
        if(event.getTab() == TAB){
            REGISTRY_ITEMBLOCK_MAP.forEach((key,blockItem) ->{
                event.accept(blockItem);
            });
        }
    }




}
