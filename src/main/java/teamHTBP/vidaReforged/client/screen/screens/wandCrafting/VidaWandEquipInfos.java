package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.client.screen.components.common.ScrolledContainer;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.core.common.item.Position;

import java.util.List;

public class VidaWandEquipInfos extends VidaWidget {
    public ScrolledContainer<VidaWidget> container;
    GridLayout gridLayout;
    VidaWandSlot slotMessage$0 = new VidaWandSlot(0, 0, getWidth() - 20, 0, ItemStack.EMPTY);
    VidaWandSlot slotMessage$1 = new VidaWandSlot(0, 0, getWidth() - 20, 0, ItemStack.EMPTY);
    VidaWandSlot slotMessage$2 = new VidaWandSlot(0, 0, getWidth() - 20, 0, ItemStack.EMPTY);
    VidaWandSlot slotMessage$3 = new VidaWandSlot(0, 0, getWidth() - 20, 0, ItemStack.EMPTY);

    public VidaWandEquipInfos(int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal(""));
        initWidget();
    }

    @Override
    public void onAdded() {

    }

    /**父组件刷新槽位数据*/
    public void setSlot(Position position, ItemStack stack){
        switch (position){
            case CORE -> {slotMessage$0.setSlotStack(stack);}
            case TOP -> {slotMessage$1.setSlotStack(stack);}
            case CENTER -> {slotMessage$2.setSlotStack(stack);}
            case BOTTOM -> {slotMessage$3.setSlotStack(stack);}
        }
    }

    private void initWidget(){
        this.container = new ScrolledContainer<>(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        this.container.setCacheable(false);
        this.container.setExtraSpacing(0);

        gridLayout = new GridLayout(this.getX(), this.getY());
        gridLayout.rowSpacing(5);
        gridLayout.addChild(slotMessage$0,0, 0, gridLayout.defaultCellSetting().alignVerticallyMiddle());
        gridLayout.addChild(slotMessage$1,1, 0, gridLayout.defaultCellSetting().alignVerticallyMiddle());
        gridLayout.addChild(slotMessage$2,2, 0, gridLayout.defaultCellSetting().alignVerticallyMiddle());
        gridLayout.addChild(slotMessage$3,3, 0, gridLayout.defaultCellSetting().alignVerticallyMiddle());

        gridLayout.arrangeElements();

        this.container.add(slotMessage$0);
        this.container.add(slotMessage$1);
        this.container.add(slotMessage$2);
        this.container.add(slotMessage$3);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        resize();
    }


    @Override
    public void setY(int y) {
        super.setY(y);
        resize();
    }

    /**当组件尺寸被重置后需要重新调整子组件的位置和宽度*/
    protected void resize(){
        slotMessage$0.setWidth(getWidth() - 20);
        slotMessage$1.setWidth(getWidth() - 20);
        slotMessage$2.setWidth(getWidth() - 20);
        slotMessage$3.setWidth(getWidth() - 20);

        gridLayout.setX(getX());
        gridLayout.setY(getY());

        gridLayout.arrangeElements();

        this.container.setX(getX());
        this.container.setY(getY());
        this.container.setWidth(this.getWidth());
        this.container.setHeight(this.getHeight());
    }

    /**确保子组件都在界面中*/
    protected void adjustChildPosition(){
        FrameLayout.alignInRectangle(gridLayout, getX(), getY(), getWidth(), gridLayout.getHeight() + 10, 0.5f, 0.5f);
    }


    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.adjustChildPosition();
        this.container.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public List<GuiEventListener> children() {
        return List.of(container);
    }
}
