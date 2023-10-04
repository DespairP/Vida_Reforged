package teamHTBP.vidaReforged.client.screen.components.magicWords;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.GuideBookScrollTextArea;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaViewMagicWordViewModel;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.VidaColor;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class MagicWordShowSection extends AbstractWidget {
    VidaViewMagicWordViewModel viewModel;
    GuideBookScrollTextArea textArea;
    public static ResourceLocation QUESTION_MARK = new ResourceLocation(MOD_ID, "textures/icons/magic_word/question_mark.png");
    public int topHeight = 32;

    public MagicWord magicWord = null;
    public MagicWordShowSection(VidaViewMagicWordViewModel viewModel, int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.viewModel = viewModel;
        init();
    }

    private void init() {
        this.topHeight = (int)(height * 1.0f / 4);

        textArea = new GuideBookScrollTextArea("description.word.vida_reforged.select", getX(), getY() + this.topHeight, width, height - topHeight);
        viewModel.selectedMagicWord.observe(newValue -> {
            this.magicWord = MagicWordManager.getMagicWord(newValue);
            boolean isUnlocked = isUnlocked();
            textArea.setNewWord(this.magicWord == null ? "" : (!isUnlocked ? "description.word.vida_reforged.unlocked" :this.magicWord.description()));
        });
        this.magicWord = MagicWordManager.getMagicWord(this.viewModel.selectedMagicWord.getValue());
        textArea.setNewWord(this.magicWord == null ? "description.word.vida_reforged.select" : this.magicWord.description());
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        textArea.render(graphics, mouseX, mouseY, partialTicks);
        renderWordImage(graphics);
        renderWordText(graphics);
    }

    protected void renderBackground(GuiGraphics graphics){
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        graphics.fillGradient(getX(), getY(), getX() + width, getY() + topHeight,0xDD000000,0xC0000000);
        poseStack.popPose();
    }

    public boolean isUnlocked(){
        return Optional.ofNullable(this.viewModel.playerMagicWords.getValue()).orElse(new ArrayList<>()).contains(magicWord.name());
    }

    protected void renderWordImage(GuiGraphics graphics){
        PoseStack poseStack = graphics.pose();

        if(this.magicWord == null){
            return;
        }
        poseStack.pushPose();
        final boolean isUnlocked = isUnlocked();
        ResourceLocation iconLocation = isUnlocked ? magicWord.icon() : QUESTION_MARK;
        TextureSection section = new TextureSection(iconLocation,0,0,16,16);

        final float factor = 1.5f;

        final int iconX = (int)((getX() + 16) * 1.0 / factor);
        final int iconY = (int)((getY() + (topHeight - 16) / 2) * 1.0 /factor);

        poseStack.pushPose();
        poseStack.scale(factor, factor, factor);
        graphics.blit(
                section.location(),
                iconX, iconY, 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                16, 16
        );
        poseStack.popPose();
    }

    protected void renderWordText(GuiGraphics graphics){
        PoseStack poseStack = graphics.pose();


        final int textY = (int)((getY() + (topHeight - 9) / 2));
        poseStack.pushPose();
        final boolean isUnlocked = this.magicWord != null && isUnlocked();
        String magicNameKey = isUnlocked ? String.format("%s.%s", magicWord.namePrefix(), magicWord.name()) : "???";
        graphics.drawString(Minecraft.getInstance().font, Component.translatable(magicNameKey).withStyle(style -> style.withBold(true)), getX() + 50, textY + 8, 0xFFFFFFFF);
        String elementNameKey = isUnlocked ? String.format("element.vida_reforged.%s", magicWord.element().toString().toLowerCase(Locale.ROOT)) : "???";
        VidaColor color = isUnlocked ? magicWord.element().baseColor : ARGBColor.of(100,100,100);
        graphics.drawString(Minecraft.getInstance().font, Component.translatable(elementNameKey).withStyle(style -> style.withColor(color.argb())), getX() + 50, textY - 4, color.argb());
        poseStack.popPose();
    }

    public GuiEventListener getChildren(){
        return this.textArea;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }
}
