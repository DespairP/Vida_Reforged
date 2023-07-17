package teamHTBP.vidaReforged.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import teamHTBP.vidaReforged.client.screen.components.MagicWordButton;
import teamHTBP.vidaReforged.client.screen.components.MagicWordButtonList;
import teamHTBP.vidaReforged.client.screen.components.MagicWordListWidget;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.server.menu.MagicWordCraftingTableMenu;

import java.util.List;

public class MagicWordCraftingTableScreen extends AbstractContainerScreen<MagicWordCraftingTableMenu> {
    MagicWordListWidget magicWordWidget;
    MagicWordButtonList magicWordListButton;

    final VidaMagicWordViewModel viewModel;

    public MagicWordCraftingTableScreen(MagicWordCraftingTableMenu menu, Inventory inventory, Component p_97743_) {
        super(menu, inventory, Component.translatable("magic_word_crafting_table"));
        viewModel = new VidaMagicWordViewModel();
    }

    @Override
    protected void init() {
        super.init();
        double factor = minecraft.getWindow().getGuiScale();
        int screenWidth = this.width;
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int componentHeight = (int) (screenHeight - 28);
        int x = screenWidth - 200;
        int y = (int)((screenHeight - componentHeight) / 2);

        magicWordWidget = new MagicWordListWidget( viewModel, x, y, 0, componentHeight, factor);
        magicWordListButton = new MagicWordButtonList(viewModel, x - MagicWordButton.PIXEL, y + componentHeight - MagicWordButton.PIXEL * MagicWordButtonList.BUTTON_AMOUNT);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        this.renderBackground(graphics);
    }

    @Override
    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderMagicWords(graphics, mouseX, mouseY, partialTicks);
    }

    public void renderMagicWords(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if(magicWordWidget != null){
            magicWordWidget.render(graphics, mouseX, mouseY, partialTicks);
            magicWordListButton.render(graphics, mouseX, mouseY, partialTicks);
        }

    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
        return super.mouseScrolled(p_94686_, p_94687_, p_94688_);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> listeners = (List<GuiEventListener>) super.children();
        listeners.addAll(this.magicWordListButton.getChildren());
        listeners.add(this.magicWordWidget);
        listeners.addAll(this.magicWordWidget.getChildren());
        return listeners;
    }

    @Override
    protected void clearWidgets() {
        this.viewModel.selectedMagicWord.clearObservers();
        this.viewModel.selectedFilterElement.clearObservers();
        super.clearWidgets();
    }
}
