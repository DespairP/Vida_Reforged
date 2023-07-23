package teamHTBP.vidaReforged.client.screen.components.magicWords;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class MagicSelectedWordWidget extends AbstractWidget {
    VidaElement element = VidaElement.EMPTY;

    public final static ResourceLocation EMPTY_WORD_LOCATION = new ResourceLocation(MOD_ID, "textures/gui/magic_word_crafting.png");

    public final static TextureSection section = new TextureSection(EMPTY_WORD_LOCATION,48,8,16,16);

    FloatRange range = new FloatRange(0.7f, 0.7f, 1);

    private String selectWordId;

    private VidaMagicWordViewModel model;

    public MagicSelectedWordWidget(VidaMagicWordViewModel model, int x, int y, VidaElement element) {
        super(x, y, 16, 16, Component.translatable("selected_magic_word"));
        this.element = element;
        this.model = model;
        this.init();
    }

    public void init(){
        this.model.selectedMagicWord.observe(newValue -> {
            this.selectWordId = this.model.selectedMagicWord.getValue().getOrDefault(element,"");
        });
        this.selectWordId = this.model.selectedMagicWord.getValue().getOrDefault(element,"");
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        float alpha = range.change(isHovered,0.02f);

        RenderSystem.enableBlend();

        PoseStack poseStack = graphics.pose();
        graphics.setColor(alpha,alpha,alpha,alpha);
        poseStack.pushPose();
        graphics.blit(
                section.location(),
                getX(), getY(), 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                256, 256
        );
        poseStack.popPose();
        RenderSystem.disableBlend();

        MagicWord word = MagicWordManager.getMagicWord(selectWordId);
        if(word == null){
            return;

        }
        // 绘制图标
        TextureSection section = new TextureSection(word.icon(),0,0,16,16);
        final int iconX = getX();
        final int iconY = getY();

        poseStack.pushPose();
        graphics.blit(
                section.location(),
                iconX, iconY, 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                16, 16
        );
        poseStack.popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    @Override
    public void onClick(double p_93634_, double p_93635_) {
        this.model.selectedFilterElement.setValue(element);
    }
}
