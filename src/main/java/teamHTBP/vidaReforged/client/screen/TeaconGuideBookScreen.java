package teamHTBP.vidaReforged.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.GuiBookPagination;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.GuideBookScrollTextArea;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.TeaconGuidebookPagesManager;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaTeaconGuidebookViewModel;
import teamHTBP.vidaReforged.core.common.system.guidebook.TeaconGuideBook;
import teamHTBP.vidaReforged.server.providers.TeaconGuideBookManager;

import java.util.ArrayList;
import java.util.List;

public class TeaconGuideBookScreen extends Screen {
    GuideBookScrollTextArea textArea;
    TeaconGuideBook book;
    int page = 1;
    VidaTeaconGuidebookViewModel viewModel;
    TeaconGuidebookPagesManager manager;
    GuiBookPagination pagination;

    public TeaconGuideBookScreen(String bookId) {
        super(Component.translatable("Teacon Guide Book"));
        TeaconGuideBook book = TeaconGuideBookManager.pageIdMap.get(bookId);
        this.viewModel = new VidaTeaconGuidebookViewModel(book);
        this.viewModel.setMaxPage(book != null ? Math.max(0,book.getPageCount()) : 0);
    }

    @Override
    protected void init() {
        super.init();
        this.manager = new TeaconGuidebookPagesManager(viewModel).setWidth(width).setHeight(height);
        this.manager.init();

        // 计算文本区高度和宽度
        final int textAreaHeight = (int)(this.height * 2.0f / 3.0f);
        final int x = (int)(this.width * 1.0f / 3.0f);
        final int y = (int)(this.height - textAreaHeight) * 2 / 3;

        this.pagination = new GuiBookPagination(viewModel, x, this.height - 20);
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        graphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderText(graphics, mouseX, mouseY, partialTicks);
    }

    public void renderText(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        this.manager.render(graphics, mouseX, mouseY, partialTicks);
        this.pagination.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> listeners = new ArrayList<>();
        listeners.addAll(this.manager.getChildren());
        listeners.addAll(this.pagination.getChildren());
        return listeners;
    }
}
