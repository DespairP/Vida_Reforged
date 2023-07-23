package teamHTBP.vidaReforged.client.screen.components.magicWords;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class MagicSelectedWordListWidget extends AbstractWidget {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 78;
    private final VidaMagicWordViewModel viewModel;
    private final Map<VidaElement, Vector2f> offsetMap = ImmutableMap.of(
            VidaElement.GOLD, new Vector2f(33, 1),
            VidaElement.WOOD, new Vector2f(1, 25),
            VidaElement.AQUA, new Vector2f(65, 25),
            VidaElement.FIRE, new Vector2f(14, 60),
            VidaElement.EARTH, new Vector2f(52, 60)
    );

    private Map<VidaElement,MagicSelectedWordWidget> widgetMap = new LinkedHashMap<>();

    public MagicSelectedWordListWidget(VidaMagicWordViewModel viewModel,int x, int y) {
        super(x, y, WIDTH, HEIGHT, Component.translatable("selected_magic_words"));
        this.viewModel = viewModel;
        this.init();
    }

    public void init(){
        for(VidaElement element : offsetMap.keySet()){
            Vector2f offsetXY = offsetMap.get(element);
            widgetMap.put(element, new MagicSelectedWordWidget(
                    viewModel,
                    getX() + (int) offsetXY.x,
                    getY() + (int) offsetXY.y,
                    element
            ));
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        widgetMap.forEach((element, magicSelectedWordWidget) -> magicSelectedWordWidget.render(graphics, mouseX, mouseY, partialTicks));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    public Collection<? extends GuiEventListener> getChildren(){
        return this.widgetMap.values();
    }
}
