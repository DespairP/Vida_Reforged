package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.components.VidaWandTooltipComponent;
import teamHTBP.vidaReforged.server.providers.VidaMagicManager;

import java.util.List;
import java.util.Map;

/**
 * 渲染生命法杖的tooltip
 * */
public class VidaWandClientTooltipScreen implements ClientTooltipComponent {
    private final List<ResourceLocation> magics;
    private final Map<VidaElement, Double> manas;
    private final int magicAmount;
    private final double maxMana;
    public static final int BORDER_SIZE = 2;
    public static final int MANA_BAR_SIZE = 64;
    private final static ResourceLocation BAR_LOCATION = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/inventory_magic_bar.png");
    private final static TextureSection BAR_SECTION = new TextureSection(BAR_LOCATION, 0, 9, 62, 8, 256, 256);
    private final static TextureSection PROGRESS_SECTION = new TextureSection(BAR_LOCATION, 7, 3, 48, 2, 256, 256);


    public VidaWandClientTooltipScreen(VidaWandTooltipComponent tooltip){
        this.magics = tooltip.getMagics();
        this.magicAmount = tooltip.getMagics() == null ? 0 : tooltip.getMagics().size();
        this.manas = tooltip.getMana();
        this.maxMana = tooltip.getMaxMana();
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        renderManaBar(font, x, y, graphics);
        renderMagics(font, x , y + 8 + BORDER_SIZE * 2, graphics);
    }

    public void renderManaBar(Font font, int x, int y,GuiGraphics graphics){
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        graphics.blit(
                BAR_SECTION.location(),
                x, y + 2, 0,
                BAR_SECTION.minU(), BAR_SECTION.minV(),
                BAR_SECTION.width(), BAR_SECTION.height(),
                256, 256
        );
        poseStack.popPose();


        poseStack.pushPose();
        int offsetX = 8;
        int offsetY = 4;
        for(VidaElement element : VidaElement.values()){
            double maxMana = this.maxMana;
            if(maxMana <= 0){
                break;
            }
            RenderSystem.enableBlend();
            // 元素对应颜色
            ARGBColor color = element.baseColor.toARGB();
            RenderSystem.setShaderColor(
                    color.r() / 255.0f,
                    color.g() / 255.0f,
                    color.b() / 255.0f,
                    0.5f
            );
            int manaWidth = (int) (manas.getOrDefault(element, 0.0) * 48.0 / maxMana);
            graphics.blit(
                    PROGRESS_SECTION.location(),
                    x + offsetX, y + 2 + offsetY, 0,
                    PROGRESS_SECTION.minU() + offsetX - 8, PROGRESS_SECTION.minV(),
                    manaWidth, PROGRESS_SECTION.height(),
                    256, 256
            );

            offsetX += manaWidth;

            RenderSystem.setShaderColor(1,1,1,1);
            RenderSystem.disableBlend();
        }
        poseStack.popPose();
    }

    /** 渲染魔法 */
    public void renderMagics(Font font, int x, int y, GuiGraphics graphics){
        final Map<ResourceLocation,VidaMagic> magicMap = VidaMagicManager.getMagicByIds(magics);
        int borderCount = 0;
        //render magic
        for(VidaMagic magic : magicMap.values()){
            final PoseStack poseStack = graphics.pose();
            final TextureSection iconTexture = magic.icon();
            final int iconWidth = iconTexture.w();

            poseStack.pushPose();
            this.blit(graphics, x + BORDER_SIZE, y + borderCount, iconTexture);
            poseStack.popPose();

            borderCount += iconWidth + BORDER_SIZE;
        }
    }

    @Override
    public int getHeight() {
        return magicAmount * (32 + BORDER_SIZE  * 2) + 8 + BORDER_SIZE * 2;
    }

    @Override
    public int getWidth(Font font) {
        return 64 + BORDER_SIZE * 2;
    }

    private void blit(GuiGraphics graphics, int x, int y, TextureSection section) {
        graphics.blit(section.location(), x, y, 0, (float)section.minU(), (float)section.minV(), section.w(), section.h(), (int) section.texWidth(), (int) section.texHeight());
    }
}
