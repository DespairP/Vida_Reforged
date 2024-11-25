package teamHTBP.vidaReforged.client.screen.components.common;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

/**可变的图像组件*/
public class ImageMutableWidget extends VidaWidget {
    private TextureSection section;
    private int rev;

    public ImageMutableWidget(int x, int y, int width, int height, Component component, TextureSection section) {
        super(x, y, width, height, component);
        this.section = section;
    }

    protected void updateWidgetNarration(NarrationElementOutput p_275454_) {
    }

    public void setSection(TextureSection section) {
        this.section = section;
        this.width = section != null ? section.width() : 0;
        this.height = section != null ? section.height() : 0;
    }

    public void renderWidget(GuiGraphics p_283475_, int p_281265_, int p_281555_, float p_282690_) {
        if(section == null){
            return;
        }
        int i = this.rev;
        int j = this.rev;
        p_283475_.blit(this.section.location(), this.getX(), this.getY(), section.minU(), section.minV(), section.w(), section.h(), i, j);
    }

    @Override
    public int getWidth() {
        return section == null ? 0 : section.w();
    }

    @Override
    public int getHeight() {
        return section == null ? 0 : section.h();
    }

    public void setRev(int size){
        this.rev = size;
    }
}
