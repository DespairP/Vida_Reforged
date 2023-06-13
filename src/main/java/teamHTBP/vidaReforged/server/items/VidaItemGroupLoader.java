package teamHTBP.vidaReforged.server.items;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.server.events.BlockItemAutoRegisterHandler.REGISTRY_ITEMBLOCK_MAP;
import static teamHTBP.vidaReforged.server.items.VidaItemLoader.VIDA_WAND;

@Mod.EventBusSubscriber(modid = "vida_reforged", bus = Mod.EventBusSubscriber.Bus.MOD)
public class VidaItemGroupLoader {
    public static CreativeModeTab tabs;
    @SubscribeEvent
    public static void buildContents(final CreativeModeTabEvent.Register event) {
        tabs = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 9)
                .title(Component.translatable("item_group." + MOD_ID + ".vida_tab"))
                // Set icon of creative tab
                .icon(() -> new ItemStack(VIDA_WAND.get()))
                // Add default items to tab
                .displayItems((params, output) -> {
                    //register blocks
                    REGISTRY_ITEMBLOCK_MAP.forEach((key, item)->{
                        output.accept(new ItemStack(item.get()));
                    });
                    output.accept(new ItemStack(VIDA_WAND.get()));
                })
                .build();
        event.registerCreativeModeTab(
                new ResourceLocation(MOD_ID, "vida_tab"),
                builder -> builder.withTabFactory(builder1 -> tabs)
        );
    }
}
