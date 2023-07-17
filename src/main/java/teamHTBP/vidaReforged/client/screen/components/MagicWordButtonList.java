package teamHTBP.vidaReforged.client.screen.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.component.IDataObserver;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;

import java.util.*;

public class MagicWordButtonList extends AbstractWidget {
    public final static int BUTTON_AMOUNT = 5;
    public final Map<VidaElement, MagicWordButton> widgetMap;
    private VidaElement selectedElement = VidaElement.EMPTY;
    private final VidaMagicWordViewModel model;
    public MagicWordButtonList(VidaMagicWordViewModel model, int x, int y) {
        super(x, y, MagicWordButton.PIXEL, MagicWordButton.PIXEL * BUTTON_AMOUNT, Component.translatable("magic filter button list"));
        this.widgetMap = new LinkedHashMap<>();
        this.model = model;
        this.initWidget();
    }

    public void initWidget(){
        int count = 0;
        for(VidaElement element : VidaElement.values()){
            if(element == VidaElement.EMPTY || element == VidaElement.VOID){
                continue;
            }
            widgetMap.put(element, new MagicWordButton(this.model, getX(), getY() + count * MagicWordButton.PIXEL, element));
            count ++;
        }

    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.widgetMap.forEach((element, magicWordButton) -> magicWordButton.render(graphics, mouseX, mouseY, partialTicks));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    public Collection<? extends GuiEventListener> getChildren(){
        return this.widgetMap.values();
    }
}
