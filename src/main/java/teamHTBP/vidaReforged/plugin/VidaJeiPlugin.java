package teamHTBP.vidaReforged.plugin;


import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.runtime.config.IJeiConfigManager;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.MagicWordCraftingTableScreen;
import teamHTBP.vidaReforged.client.screen.MagicWordScreen;
import teamHTBP.vidaReforged.client.screen.PrismScreen;
import teamHTBP.vidaReforged.core.common.system.guidebook.TeaconGuideBook;

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
        registration.addGuiContainerHandler(PrismScreen.class, HideJEIGuiHandler.create(PrismScreen.class));
        registration.addGlobalGuiHandler(new HideJEIScreenHandler());
    }
}
