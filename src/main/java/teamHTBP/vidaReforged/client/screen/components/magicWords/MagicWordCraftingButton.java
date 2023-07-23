package teamHTBP.vidaReforged.client.screen.components.magicWords;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.packets.MagicWordCraftingPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.client.screen.components.magicWords.MagicWordWidget.DINKFONT;

public class MagicWordCraftingButton extends AbstractWidget {
    public static final int WIDTH = 48;

    public static final int HEIGHT = 16;

    public final ResourceLocation location = new ResourceLocation(MOD_ID, "textures/gui/magic_word_crafting.png");

    public final TextureSection section = new TextureSection(location, 32, 112, WIDTH, HEIGHT);

    FloatRange hoverAlpha = new FloatRange(0.4F,0.4F, 0.7F);
    FloatRange disableAlpha = new FloatRange(0.0F,0.0F, 0.3F);

    VidaMagicWordViewModel model;
    public MagicWordCraftingButton(VidaMagicWordViewModel model, int x, int y) {
        super(x, y, WIDTH, HEIGHT, Component.translatable("craft"));
        this.model = model;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        float alpha = 0;
        alpha += hoverAlpha.change(isHovered,0.02f);
        alpha -= disableAlpha.change(model.isCrafting.getValue(), 0.02f);

        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(alpha, alpha, alpha,1);
        graphics.blit(
                section.location(),
                getX(), getY(), 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                256, 256
        );
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1,1);
        poseStack.popPose();

        poseStack.pushPose();
        float scale = 0.8f;
        poseStack.scale(scale,scale,scale);
        Minecraft mc = Minecraft.getInstance();
        Component testComponent = Component.translatable("crafting").withStyle((style) -> {
            return style.withFont(DINKFONT).withBold(false);
        });
        graphics.drawCenteredString(
                mc.font,
                testComponent,
                (int)((getX() + 24) * (1.0f / scale)),
                (int)((getY() + 6) * (1.0f / scale)),
                0xFFFFFF
        );
        poseStack.popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    @Override
    public void onClick(double p_93634_, double p_93635_) {
        if(!this.model.isCrafting.getValue()) {
            VidaPacketManager.sendToServer(new MagicWordCraftingPacket(this.model.blockPos.getValue()));
            model.isCrafting.setValue(true);
        }
    }
}
