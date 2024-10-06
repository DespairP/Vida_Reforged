package teamHTBP.vidaReforged.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.screen.screens.common.VidaContainerScreen;
import teamHTBP.vidaReforged.server.menu.VividChestBlockMenu;

public class VividChestScreen extends VidaContainerScreen<VividChestBlockMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/vivid_chest.png");
    private final int containerRows;
    public VividChestScreen(VividChestBlockMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.containerRows = 3;
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        this.renderBg(graphics, partialTicks, mouseX, mouseY);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderLabels(graphics, mouseX, mouseY);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        p_281635_.drawString(this.font, this.title, i + 6, j + 6, 0xFFFFFFFF, false);
        p_281635_.drawString(this.font, this.playerInventoryTitle, i + 6, j + 72, 0xFFFFFFFF, false);
    }

    protected void renderBg(GuiGraphics graphics, float partialTicks, int p_282603_, int p_282158_) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(CONTAINER_BACKGROUND, i, j, 0, 0, this.imageWidth, this.imageHeight);
        //graphics.blit(CONTAINER_BACKGROUND, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}
