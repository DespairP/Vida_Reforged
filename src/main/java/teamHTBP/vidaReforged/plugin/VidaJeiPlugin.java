package teamHTBP.vidaReforged.plugin;


import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.screens.magicwordCrafting.MagicWordCraftingTableScreen;
import teamHTBP.vidaReforged.client.screen.screens.magicwordAchieve.MagicWordScreen;
import teamHTBP.vidaReforged.client.screen.screens.common.PrismScreen;
import teamHTBP.vidaReforged.client.screen.VidaWandCraftingScreen;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

@JeiPlugin
public class VidaJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MOD_ID, "jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(MagicWordScreen.class, HideJEIGuiHandler.create(MagicWordScreen.class));
        registration.addGuiContainerHandler(MagicWordCraftingTableScreen.class, HideJEIGuiHandler.create(MagicWordCraftingTableScreen.class));
        registration.addGuiContainerHandler(VidaWandCraftingScreen.class, HideJEIGuiHandler.create(VidaWandCraftingScreen.class));
        registration.addGuiContainerHandler(PrismScreen.class, HideJEIGuiHandler.create(PrismScreen.class));
        registration.addGlobalGuiHandler(new HideJEIScreenHandler());
    }
}
