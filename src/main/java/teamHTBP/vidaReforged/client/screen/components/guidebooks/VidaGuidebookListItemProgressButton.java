package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.core.common.system.guidebook.DisplayInfo;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageListItem;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaGuidebookListItemProgressButton extends VidaWidget {
    public final VidaPageListItem item;
    public static final ResourceLocation BUTTON_RESOURCE = new ResourceLocation(MOD_ID, "textures/gui/achievements.png");
    public static final TextureSection BUTTON_SECTION = new TextureSection(BUTTON_RESOURCE, 11, 11, 26, 26, 64, 64);
    public static final int PIXEL = 26;
    public static final int ITEM_PIXEL_MARGIN = 5;

    public VidaGuidebookListItemProgressButton(VidaPageListItem item, int width, int height) {
        super(item.getInfo().x - 13, item.getInfo().y - 13, width, height, Component.literal("button"));
        this.item = item;
    }

    public VidaGuidebookListItemProgressButton(VidaPageListItem item) {
        super(item.getInfo().getX(), item.getInfo().getY(), PIXEL, PIXEL, Component.literal(item.toString()));
        this.item = item;
    }



    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        DisplayInfo info = item.getInfo();

        PoseStack poseStack = graphics.pose();
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        poseStack.pushPose();
        if(info != null){
            graphics.blit(
                    BUTTON_RESOURCE,
                     getX() ,  getY(), 0,
                    BUTTON_SECTION.minU(), BUTTON_SECTION.minV(),
                    BUTTON_SECTION.w(), BUTTON_SECTION.h(),
                    BUTTON_SECTION.texWidth(), BUTTON_SECTION.texHeight()
            );
        }
        if(info != null && info.itemIcon != null) {
            graphics.renderItem(item.getInfo().getItemIcon(), getX() + ITEM_PIXEL_MARGIN, getY() + ITEM_PIXEL_MARGIN);
        }
        poseStack.popPose();
        RenderSystem.disableBlend();
    }
}
