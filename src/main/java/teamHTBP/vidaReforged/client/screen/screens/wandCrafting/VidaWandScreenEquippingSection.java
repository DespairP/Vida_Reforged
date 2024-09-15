package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaWandCraftingViewModel;
import teamHTBP.vidaReforged.core.common.ui.VidaLifecycleSection;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;

import java.util.ArrayList;
import java.util.Collection;

/**饰品装配区域*/
public class VidaWandScreenEquippingSection extends VidaLifecycleSection {
    /***/
    protected VidaWandEquipInfos infoSlots;
    protected VidaWandCraftingViewModel viewModel;
    private SecondOrderDynamics offset = new SecondOrderDynamics(1, 1, 0, new Vector3f(0,0,0));

    public VidaWandScreenEquippingSection(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        this.infoSlots = new VidaWandEquipInfos(getX(), getY(), width * 2 / 3, height);
        this.viewModel = new ViewModelProvider(requireParent()).get(VidaWandCraftingViewModel.class);
    }

    @Override
    public void onInit() {
        this.infoSlots.setVisible(visible);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        this.infoSlots.setX(x);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        this.infoSlots.setY(y);
    }

    @Override
    public void setHeight(int value) {
        super.setHeight(value);
        this.infoSlots.setHeight(height);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        this.infoSlots.setWidth(width * 2 / 3);
    }



    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        this.infoSlots.setVisible(visible);
        if(visible){
            this.offset = new SecondOrderDynamics(1, 1, 0, new Vector3f(16));
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.pose().pushPose();
        Vector3f offset = this.offset.update(this.mc.getDeltaFrameTime() * 0.4f, new Vector3f(0), null);
        graphics.pose().translate(offset.x, 0, 0);
        this.infoSlots.render(graphics, mouseX, mouseY, partialTicks);
        graphics.pose().popPose();
        viewModel.slots.getValue().forEach(((position, slot) -> this.infoSlots.setSlot(position, slot.getItem())));
    }

    @Override
    public Collection<? extends GuiEventListener> children() {
        return new ArrayList<>(this.infoSlots.children());
    }
}
