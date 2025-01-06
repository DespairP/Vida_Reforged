package teamHTBP.vidaReforged.client.screen.components.faction;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;

public class TradeSelectButton extends VidaWidget {
    private static final Vector3f ZERO = new Vector3f();
    private static final Vector3f HOVERED = new Vector3f(16);
    SecondOrderDynamics hoverWidth = new SecondOrderDynamics(1, 1, 0.5f, TradeSelectButton.ZERO);
    ItemStack itemStack;

    public TradeSelectButton(int x, int y, int width, int height, Component component, ResourceLocation id, ItemStack stack) {
        super(x, y, width, height, component, id);
        this.itemStack = stack;
    }

    @Override
    public int getWidth() {
        return (int) (this.width + hoverWidth.currentPos().x);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + getWidth() && mouseY < this.getY() + getWidth();
        hoverWidth.update(partialTicks * 0.5f, isHovered ? HOVERED : ZERO, null);
        graphics.renderFakeItem(itemStack, getX() + (getWidth() - 16) / 2, getY());
        //VidaGuiHelper.fillGradient(graphics, getX(), getY(), getX() + getWidth(), getY() + getHeight(), 100f, 0xffffffff, 0xffffffff);
    }
}
