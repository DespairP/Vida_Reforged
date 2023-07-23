package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaTeaconGuidebookViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TeaconGuidebookPagesManager implements IGuidebookComponent{
    private final VidaTeaconGuidebookViewModel viewModel;
    private int width;
    private int height;
    private IGuidebookComponent rightComponent = null;

    public TeaconGuidebookPagesManager(VidaTeaconGuidebookViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public TeaconGuidebookPagesManager setHeight(int height) {
        this.height = height;
        return this;
    }

    public TeaconGuidebookPagesManager setWidth(int width) {
        this.width = width;
        return this;
    }

    public void init(){
        final int textWidth = (int)(this.width * 1.9f / 3.0f);
        final int textHeight = (int)(this.height * 2.0f / 3.0f);
        final int x = (int)(this.width * 1.0f / 3.0f);
        final int y = (int)(this.height - textHeight) / 2;
        int currentIndex = viewModel.page.getValue() - 1;
        this.rightComponent = viewModel.getGuidebook().pages().get(currentIndex).right().initComponent(x, y, textWidth, textHeight);
    }


    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if(this.rightComponent != null){
            this.rightComponent.render(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void setFocused(boolean p_265728_) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    public Collection<? extends GuiEventListener> getChildren(){
        List<GuiEventListener> listeners = new ArrayList<>();
        if(this.rightComponent != null){
            listeners.add((GuiEventListener) this.rightComponent);
        }
        return listeners;
    }
}
