package teamHTBP.vidaReforged.client.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.components.VidaWandTooltipComponent;
import teamHTBP.vidaReforged.server.providers.MagicTemplateManager;

import java.util.List;
import java.util.Map;

public class VidaWandClientTooltipComponents implements ClientTooltipComponent {
    private ResourceLocation logo;
    private List<String> magics;
    private int magicAmount;
    public static final int BORDER_SIZE = 2;

    public VidaWandClientTooltipComponents(VidaWandTooltipComponent tooltip){
        this.magics = tooltip.getMagics();
        this.magicAmount = tooltip.getMagics() == null ? 0 : tooltip.getMagics().size();
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        final Map<String,VidaMagic> magicMap = MagicTemplateManager.getMagicByIds(magics);
        int borderCount = 0;
        //render magic
        for(VidaMagic magic : magicMap.values()){
            final PoseStack poseStack = graphics.pose();
            final ResourceLocation iconLocation = magic.icon();
            final int iconSize = 32;

            poseStack.pushPose();
            this.blit(graphics, x + BORDER_SIZE, y + borderCount, new TextureSection(iconLocation, 0 , 0, 32, 32));
            poseStack.popPose();

            borderCount += iconSize + BORDER_SIZE;
        }
    }

    @Override
    public int getHeight() {
        return magicAmount * (32 + BORDER_SIZE  * 2);
    }

    @Override
    public int getWidth(Font font) {
        return BORDER_SIZE * 2 + 32;
    }

    private void blit(GuiGraphics graphics,int x, int y, TextureSection section) {
        graphics.blit(section.location(), x, y, 0, (float)section.minU(), (float)section.minV(), section.w(), section.h(), 32, 32);
    }
}
