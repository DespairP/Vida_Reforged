package teamHTBP.vidaReforged.client.screen.components.prism;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class PrismResultButton extends AbstractWidget {

    ResourceLocation location = new ResourceLocation(MOD_ID, "textures/gui/prism_table_gui.png");
    private TextureSection buttonSection$1 = new TextureSection(location, 0, 144, 16, 16);
    private TextureSection buttonSection$2 = new TextureSection(location,17,144,16,16);

    private final Runnable clickEvent;

    public PrismResultButton(Runnable clickEvent,int x, int y) {
        super(x, y, 16, 16, Component.translatable("craft"));
        this.clickEvent = clickEvent;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack poseStack = graphics.pose();

        TextureSection buttonSection = this.isHovered ? this.buttonSection$1 : this.buttonSection$2;

        poseStack.pushPose();
        graphics.blit(
                buttonSection.location(),
                getX(), getY(), 0,
                buttonSection.minU(), buttonSection.minV(),
                buttonSection.w(), buttonSection.w(),
                256, 256
        );

        poseStack.popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    @Override
    public void onClick(double p_93634_, double p_93635_) {
        if(clickEvent != null){
            this.clickEvent.run();
        }
    }
}
