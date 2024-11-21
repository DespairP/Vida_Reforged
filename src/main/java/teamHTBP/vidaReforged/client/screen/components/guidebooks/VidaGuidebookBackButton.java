package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.components.common.IconButton;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaScreenEventChannelViewModel;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaScreenEvent;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.common.ui.style.Padding;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import java.util.Collection;
import java.util.List;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaGuidebookBackButton extends VidaWidget implements IVidaGuidebookComponent {
    public static final ResourceLocation BOOK_RESOURCE = new ResourceLocation(MOD_ID, "textures/gui/book.png");
    public static final TextureSection ICON_SECTION = new TextureSection(BOOK_RESOURCE, 97, 243,  14, 12, 512, 512);
    public static final TextureSection ICON_ACTIVE_SECTION = new TextureSection(BOOK_RESOURCE, 97, 259, 14, 12, 512, 512);
    private IconButton button;

    private VidaScreenEventChannelViewModel model;

    public VidaGuidebookBackButton(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        this.button = new IconButton.Builder()
                .width(width)
                .height(height)
                .imageTex(ICON_SECTION)
                .imageActiveTex(ICON_ACTIVE_SECTION)
                .rev(512)
                .message(Component.empty())
                .padding(new Padding(0, 0, 0, 0))
                .build(x, y);
        this.model = new ViewModelProvider(requireParent()).get(VidaScreenEventChannelViewModel.class);
    }

    @Override
    public void init() {

    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        button.setX(getX());
        button.setY(getY());
        button.setWidth(getWidth());
        button.setHeight(getHeight());

        button.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClick(double p_93634_, double p_93635_) {
        this.model.pushMessage(new VidaScreenEvent("close", null));
    }

    @Override
    public Collection<? extends GuiEventListener> children() {
        return List.of(this);
    }
}
