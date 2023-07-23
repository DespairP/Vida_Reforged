package teamHTBP.vidaReforged.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.GuideBookScrollTextArea;

public class TeaconGuideBookScreen extends Screen {
    GuideBookScrollTextArea textArea;


    public TeaconGuideBookScreen(String bookId) {
        super(Component.translatable("Teacon Guide Book"));
    }

    @Override
    protected void init() {
        super.init();
        final int textWidth = (int)(this.width * 1.9f / 3.0f);
        final int textHeight = (int)(this.height * 2.0f / 3.0f);
        final int x = (int)(this.width * 1.0f / 3.0f);
        final int y = (int)(this.height - textHeight) / 2;
        this.textArea = new GuideBookScrollTextArea(x, y, textWidth, textHeight);
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
        this.textArea.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
