package teamHTBP.vidaReforged.client.screen.screens.magicwordCrafting;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.hud.IVidaNodes;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

import java.util.*;

public class MagicWordFilterList<T> extends VidaWidget implements IVidaNodes {
    /**/
    public final static int DEFAULT_BUTTON_AMOUNT = 5;
    /**子组件列表*/
    public final Map<T, MagicWordFilterButton<T>> widgetMap;
    /**Id列表*/
    public final List<T> ids;
    private static final Map<VidaElement, List<ARGBColor>> colorMap = ImmutableMap.of(
            VidaElement.GOLD, ImmutableList.of(ARGBColor.of(246, 211, 101), ARGBColor.of(253, 160, 133)),
            VidaElement.WOOD, ImmutableList.of(ARGBColor.of(11, 163, 96), ARGBColor.of(60, 186, 146)),
            VidaElement.AQUA, ImmutableList.of(ARGBColor.of(72, 198, 239), ARGBColor.of(111, 134, 214)),
            VidaElement.FIRE, ImmutableList.of(ARGBColor.of(255, 88, 88), ARGBColor.of(240, 152, 25)),
            VidaElement.EARTH, ImmutableList.of(ARGBColor.of(230, 185, 128), ARGBColor.of(234, 205, 163))
    );

    public MagicWordFilterList(int x, int y, int buttonSize, int amount, ResourceLocation id) {
        super(x, y, buttonSize, buttonSize * amount, Component.literal("magic filter button list"), id);
        this.widgetMap = new LinkedHashMap<>();
        this.ids = new ArrayList<>();
    }

    public MagicWordFilterList<T> addOption(final T id, MagicWordFilterButton.ClickListener<T> listener){
        this.ids.add(id);
        MagicWordFilterButton<T> button = new MagicWordFilterButton<>(
                getX(),
                getY() + this.widgetMap.size() * MagicWordFilterButton.BTN_SIZE,
                MagicWordFilterButton.BTN_SIZE,
                MagicWordFilterButton.BTN_SIZE,
                id
        );
        button.setClickListener(listener);
        this.widgetMap.put(id, button);
        return this;
    }


    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.widgetMap.forEach((element, magicWordFilter) -> magicWordFilter.render(graphics, mouseX, mouseY, partialTicks));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {}

    public void setSelected(T id){
        for (Map.Entry<T, MagicWordFilterButton<T>> entry : widgetMap.entrySet()) {
            if(entry.getKey().equals(id)){
                entry.getValue().setSelected(true);
            }
            if(!entry.getKey().equals(id)) {
                entry.getValue().setSelected(false);
            }
        }
    }

    public Collection<? extends GuiEventListener> children() {
        return this.widgetMap.values();
    }

    @Override
    public Collection<VidaWidget> childrenNode() {
        return new ArrayList<>(this.widgetMap.values());
    }
}
