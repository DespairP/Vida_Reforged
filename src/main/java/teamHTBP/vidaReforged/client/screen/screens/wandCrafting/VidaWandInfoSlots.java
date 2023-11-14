package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.common.ScrolledContainer;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaWandCraftingViewModel;
import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class VidaWandInfoSlots extends VidaWidget {
    public ScrolledContainer<VidaWidget> container;
    private VidaWandCraftingViewModel viewModel;
    public VidaWandInfoSlots(int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal(""));
        this.viewModel = new ViewModelProvider(requireParent()).get(VidaWandCraftingViewModel.class);
        initWidget();
    }

    public void initWidget(){
        this.container = new ScrolledContainer<>(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        this.container.setCacheable(false);
        this.container.setExtraSpacing(10);

        VidaWandSlot slotMessage$0 = new VidaWandSlot(0, 0, getWidth() - 20, 50, Position.CORE);
        VidaWandSlot slotMessage$1 = new VidaWandSlot(0, 0, getWidth() - 20, 50, Position.TOP);
        VidaWandSlot slotMessage$2 = new VidaWandSlot(0, 0, getWidth() - 20, 50, Position.CENTER);
        VidaWandSlot slotMessage$3 = new VidaWandSlot(0, 0, getWidth() - 20, 50, Position.BOTTOM);


        GridLayout gridLayout = new GridLayout(this.getX(), this.getY());
        gridLayout.rowSpacing(5);
        gridLayout.addChild(slotMessage$0,0, 0, gridLayout.defaultCellSetting().alignVerticallyMiddle());
        gridLayout.addChild(slotMessage$1,1, 0, gridLayout.defaultCellSetting().alignVerticallyMiddle());
        gridLayout.addChild(slotMessage$2,2, 0, gridLayout.defaultCellSetting().alignVerticallyMiddle());
        gridLayout.addChild(slotMessage$3,3, 0, gridLayout.defaultCellSetting().alignVerticallyMiddle());


        gridLayout.arrangeElements();

        FrameLayout.alignInRectangle(gridLayout, getX(), getY(), getWidth(), gridLayout.getHeight() + 10, 0.5f, 0.5f);


        this.container.add(slotMessage$0);
        this.container.add(slotMessage$1);
        this.container.add(slotMessage$2);
        this.container.add(slotMessage$3);

        this.viewModel.needUpdate.observe(newValue -> {
            gridLayout.arrangeElements();
            container.notifyComponentToFresh();
            slotMessage$0.setChanged();
            slotMessage$1.setChanged();
            slotMessage$2.setChanged();
            slotMessage$3.setChanged();
        });

    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.container.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public List<GuiEventListener> children() {
        return List.of(container);
    }
}
