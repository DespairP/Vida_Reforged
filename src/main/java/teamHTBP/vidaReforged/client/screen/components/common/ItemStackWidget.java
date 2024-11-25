package teamHTBP.vidaReforged.client.screen.components.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

public class ItemStackWidget extends ImageWidget {
    final ItemStack renderItem;

    public ItemStackWidget(final ItemStack renderItem) {
        super(16, 16, InventoryMenu.BLOCK_ATLAS);
        this.renderItem = renderItem;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.renderFakeItem(renderItem, getX(), getY());
    }
}
