package teamHTBP.vidaReforged.client.screen.screens.magicwordCrafting;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.hud.IVidaNodes;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;

import java.util.*;

public class MagicWordFilterList extends AbstractWidget implements IVidaNodes {
    public final static int BUTTON_AMOUNT = 5;
    public final Map<VidaElement, MagicWordFilter> widgetMap;
    private VidaElement selectedElement = VidaElement.EMPTY;
    private VidaMagicWordViewModel model;
    public MagicWordFilterList(int x, int y) {
        super(x, y, MagicWordFilter.PIXEL, MagicWordFilter.PIXEL * BUTTON_AMOUNT, Component.literal("magic filter button list"));
        this.widgetMap = new LinkedHashMap<>();
        this.initWidget();
    }

    public void initWidget(){
        //
        this.model = new ViewModelProvider(requireParent()).get(VidaMagicWordViewModel.class);

        int count = 0;
        for(VidaElement element : VidaElement.values()){
            if(element == VidaElement.EMPTY || element == VidaElement.VOID){
                continue;
            }
            widgetMap.put(element, new MagicWordFilter(this.model, getX(), getY() + count * MagicWordFilter.PIXEL, element));
            count ++;
        }

    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.widgetMap.forEach((element, magicWordFilter) -> magicWordFilter.render(graphics, mouseX, mouseY, partialTicks));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    public Collection<? extends GuiEventListener> children(){
        return this.widgetMap.values();
    }
}
