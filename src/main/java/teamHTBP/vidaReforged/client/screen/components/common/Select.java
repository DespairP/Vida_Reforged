package teamHTBP.vidaReforged.client.screen.components.common;

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
import teamHTBP.vidaReforged.core.api.screen.StyleSheet;
import teamHTBP.vidaReforged.core.common.ui.layouts.GridLayoutMutation;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Select<T> extends VidaWidget implements IVidaNodes {
    /**/
    public final static int DEFAULT_BUTTON_AMOUNT = 5;
    /**子组件列表*/
    public final Map<T, SelectButton<T>> widgetMap;
    /**Id列表*/
    public final List<T> ids;
    /**渲染类型*/
    @StyleSheet
    public SelectType type = SelectType.VERTICAL;
    /**排布*/
    private GridLayoutMutation layout;
    /**按钮间距*/
    private int buttonGap = 0;

    /**渲染类型*/
    public enum SelectType {
        VERTICAL, HORIZONTAL, MULTIPLE_ROWS, DROP_LIST;
    }
    @Deprecated
    private static final Map<VidaElement, List<ARGBColor>> colorMap = ImmutableMap.of(
            VidaElement.GOLD, ImmutableList.of(ARGBColor.of(246, 211, 101), ARGBColor.of(253, 160, 133)),
            VidaElement.WOOD, ImmutableList.of(ARGBColor.of(11, 163, 96), ARGBColor.of(60, 186, 146)),
            VidaElement.AQUA, ImmutableList.of(ARGBColor.of(72, 198, 239), ARGBColor.of(111, 134, 214)),
            VidaElement.FIRE, ImmutableList.of(ARGBColor.of(255, 88, 88), ARGBColor.of(240, 152, 25)),
            VidaElement.EARTH, ImmutableList.of(ARGBColor.of(230, 185, 128), ARGBColor.of(234, 205, 163))
    );

    public Select(int x, int y, int buttonSize, int amount, ResourceLocation id) {
        super(x, y, buttonSize, buttonSize * amount, Component.literal("magic filter button list"), id);
        this.widgetMap = new LinkedHashMap<>();
        this.ids = new ArrayList<>();
        this.layout = new GridLayoutMutation(x, y);
    }

    public Select<T> addOption(final T id, Consumer<SelectButton<T>> initialModifier){
        this.ids.add(id);
        SelectButton<T> button = new SelectButton<>(
                getX(),
                getY() + this.widgetMap.size() * SelectButton.BTN_SIZE,
                SelectButton.BTN_SIZE,
                SelectButton.BTN_SIZE,
                id
        );
        if(initialModifier != null){
            initialModifier.accept(button);
        }
        this.widgetMap.put(id, button);
        // 根据类型加入到
        int rowIndex = 0;
        int colIndex = 0;
        switch (type){
            case VERTICAL -> {
                rowIndex = this.widgetMap.size() - 1;
            }
            case HORIZONTAL -> {
                colIndex = this.widgetMap.size() - 1;
            }
        }
        this.layout.addChild(button, rowIndex, colIndex, this.layout.defaultCellSetting().padding(buttonGap, buttonGap));
        return this;
    }

    @Override
    protected void onPositionMoved(int x, int y) {
        this.layout.setPosition(x, y);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.layout.arrangeElements();
        this.widgetMap.forEach((element, magicWordFilter) -> magicWordFilter.render(graphics, mouseX, mouseY, partialTicks));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {}

    public Select<T> setType(SelectType type) {
        this.type = type;
        return this;
    }

    public Select<T> setButtonGap(int gap){
        this.buttonGap = gap;
        return this;
    }

    public void setSelected(T id){
        for (Map.Entry<T, SelectButton<T>> entry : widgetMap.entrySet()) {
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
