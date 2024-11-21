package teamHTBP.vidaReforged.client.screen.components.common;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;

import java.util.LinkedList;
import java.util.List;

/**
 * 组件容器
 */
public class ComponentsContainer extends VidaWidget {
    List<VidaWidget> children = new LinkedList<>();
    BackgroundRenderer background;
    RearrangeHandler arrangement;

    public ComponentsContainer(int x, int y, int width, int height, BackgroundRenderer background, RearrangeHandler arrangement) {
        super(x, y, width, height, Component.literal("container"));
        this.background = background;
        this.arrangement = arrangement;
    }

    public void addChild(VidaWidget renderable){
        children.add(renderable);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if(background != null) {
            background.renderBackground(graphics, getX(), getY(), mouseX, mouseY, partialTicks);
        }
        if(arrangement != null){
            arrangement.rearrange(children);
        }
        for (VidaWidget child : children) {
            child.setX(getX());
            child.setY(getY());
            child.render(graphics, mouseX, mouseY, partialTicks);
        }
    }

    public interface BackgroundRenderer{
        public void renderBackground(GuiGraphics graphics, int x, int y, int mouseX, int mouseY, float partialTicks);
    }

    public interface RearrangeHandler{
        public void rearrange(List<VidaWidget> widgets);
    }
}
