package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import java.util.Collection;
import java.util.List;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaGuidebookTitle extends VidaWidget implements IVidaGuidebookComponent {
    public final ResourceLocation GUIDEBOOK_RESOURCE = new ResourceLocation(MOD_ID, "textures/gui/book.png");
    public final TextureSection TITLE_SEC = new TextureSection(GUIDEBOOK_RESOURCE, 0, 281, 64, 7, 512, 512);
    protected StringWidget widget;

    public VidaGuidebookTitle(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        this.widget = new StringWidget(component.copy().withStyle(Style.EMPTY.withBold(true)), mc.font);
        // 设置默认高度和宽度
        setWidth(mc.font.width(component));
        setHeight(mc.font.lineHeight + 10);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        widget.setX(getX());
        widget.setY(getY());
        int backgroundX = widget.getWidth() / 2 + getX();
        graphics.blit(
                GUIDEBOOK_RESOURCE,
                backgroundX - 32, getY() + 8, 0,
                TITLE_SEC.minU(), TITLE_SEC.minV(),
                TITLE_SEC.w(), TITLE_SEC.h(),
                512, 512
        );
        widget.renderWidget(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public Collection<? extends GuiEventListener> children() {
        return List.of();
    }

    @Override
    public void init() {

    }
}
