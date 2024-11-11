package teamHTBP.vidaReforged.client.screen.components.fraction;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;

public class LivingEntityDisplayComponent extends VidaWidget {
    LivingEntity entity;
    public static final int DEFAULT_WIDTH = 49;
    public static final int DEFAULT_HEIGHT = 70;

    public LivingEntityDisplayComponent(int x, int y, int width, int height, ResourceLocation id) {
        super(x, y, width, height, Component.literal(""), id);
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if(entity != null){
            InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, getX(), getY(),30, mouseX, mouseY, entity);
        }
    }
}
