package teamHTBP.vidaReforged.client.screen.components.magicWords;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaViewMagicWordViewModel;
import teamHTBP.vidaReforged.core.common.ui.component.IDataObserver;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import java.util.ArrayList;
import java.util.Optional;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;


@Deprecated(forRemoval = true)
public class MagicWordSingleWidget extends AbstractWidget {
    public static final int HEIGHT = 20;
    public static final int WIDTH = 85;
    public static ResourceLocation QUESTION_MARK = new ResourceLocation(MOD_ID, "textures/icons/magic_word/question_mark.png");
    public static ResourceLocation DINKFONT = new ResourceLocation(MOD_ID, "dinkie");
    public static Integer ICON_PIXEL = 16;
    public final MagicWord magicWord;
    public FloatRange upperBorderPoint = new FloatRange(0,0, getWidth());
    public FloatRange downBorderPoint = new FloatRange(0,0, getWidth());
    public FloatRange selectedAlpha = new FloatRange(0,0, 1);
    private boolean isSelected = false;
    private boolean isUnlocked = false;
    private VidaViewMagicWordViewModel model;

    private float scroll = 0f;

    public MagicWordSingleWidget(VidaViewMagicWordViewModel model, int x, int y, int width, MagicWord word) {
        super(x, y, width, HEIGHT, Component.translatable("word"));
        this.model = model;
        this.magicWord = word;
        init();
    }

    public void init(){
        IDataObserver<String> observer = this::checkIfSelected;
        this.model.selectedMagicWord.observeForever(observer);
        this.checkIfSelected(this.model.selectedMagicWord.getValue());
        if(magicWord != null){
            this.isUnlocked = Optional.ofNullable(this.model.playerMagicWords.getValue()).orElse(new ArrayList<>()).contains(magicWord.name());
        }
    }

    public void checkIfSelected(String value){
        if(magicWord == null || value == null){
            setSelected(false);
            return;
        }
        boolean isSelected = value != null && value.equals(this.magicWord.name());
        setSelected(isSelected);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        upperBorderPoint.change(isHovered, 3f);
        downBorderPoint.change(isHovered, 2.85f);

        // 绘制选择的背景
        renderSelectedBg(graphics, mouseX, mouseY, partialTicks);

        // 绘制背景
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer buffer = graphics.bufferSource().getBuffer(RenderType.gui());
        matrix4f.translate(getX(), getY(), 0);
        ARGBColor color = new ARGBColor(255,0,0,0);
        float r = color.r() / 255.0f;
        float g = color.g() / 255.0f;
        float b = color.b() / 255.0f;

        float up = upperBorderPoint.get();
        float dn = downBorderPoint.get();

        // 绘制
        buffer.vertex( matrix4f, 0 + up, 0, 0).color(r, g, b, 0.5f).endVertex();
        buffer.vertex( matrix4f, 0 + dn, HEIGHT, 0).color(r, g, b, 0.5f).endVertex();
        buffer.vertex( matrix4f, getWidth(), HEIGHT, 0).color(r, g, b, 0.5f).endVertex();
        buffer.vertex( matrix4f, getWidth() , 0, 0).color(r, g, b, 0.5f).endVertex();

        buffer.vertex( matrix4f, 0, 0, 0).color(1, 1, 1, 0.5f).endVertex();
        buffer.vertex( matrix4f, 0, HEIGHT, 0).color(1, 1, 1, 0.5f).endVertex();
        buffer.vertex( matrix4f, dn, HEIGHT, 0).color(1, 1, 1, 0.2f).endVertex();
        buffer.vertex( matrix4f, up , 0, 0).color(1, 1, 1, 0.2f).endVertex();

        poseStack.popPose();


        if(magicWord == null){
            return;
        }

        String magicNameKey = isUnlocked ? String.format("%s.%s", magicWord.namePrefix(), magicWord.name()) : "???";
        ResourceLocation iconLocation = isUnlocked ? magicWord.icon() : QUESTION_MARK;

        // 绘制名字
        poseStack.pushPose();
        Minecraft mc = Minecraft.getInstance();
        String name = Language.getInstance().getOrDefault(magicNameKey, magicNameKey);
        name = name.substring(0,Math.min(name.length(), 10));
        Component testComponent = Component.literal(name).withStyle((style) -> {
            return style.withFont(DINKFONT).withBold(false);
        });
        graphics.drawString(mc.font, testComponent,getX() + 28, getY() + 8, 0xFFFFFFFF);
        poseStack.popPose();

        // 绘制图标
        TextureSection section = new TextureSection(iconLocation,0,0,16,16, ICON_PIXEL, ICON_PIXEL);
        final int iconX = getX() + 4;
        final int iconY = getY() + ((HEIGHT - ICON_PIXEL) / 2) ;

        poseStack.pushPose();
        graphics.blit(
                section.location(),
                iconX, iconY, 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                ICON_PIXEL, ICON_PIXEL
        );
        poseStack.popPose();

    }


    public void renderSelectedBg(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        float alpha = this.selectedAlpha.change(this.isSelected,0.05f);
        if(alpha <= 0.03){
            return;
        }

        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer buffer = graphics.bufferSource().getBuffer(RenderType.gui());
        matrix4f.translate(getX(), getY(), 0);
        ARGBColor fromColor = ARGBColor.of(171, 236, 214);
        float fromR = fromColor.r() / 255.0f;
        float fromG = fromColor.g() / 255.0f;
        float fromB = fromColor.b() / 255.0f;

        ARGBColor toColor = ARGBColor.of(251, 237, 150);
        float toR = toColor.r() / 255.0f;
        float toG = toColor.g() / 255.0f;
        float toB = toColor.b() / 255.0f;

        buffer.vertex(matrix4f, 0, 0, 0).color(toR, toG, toB, alpha).endVertex();
        buffer.vertex(matrix4f, 0, HEIGHT, 0).color(fromR, fromG, fromB, alpha).endVertex();
        buffer.vertex(matrix4f, getWidth(), HEIGHT, 0).color(toR, toG, toB, alpha).endVertex();
        buffer.vertex(matrix4f, getWidth(), 0, 0).color(fromR, fromG, fromB, alpha).endVertex();


        poseStack.popPose();
    }

    @Override
    public int getY() {
        return (int) (super.getY() + scroll);
    }

    public void setScroll(float scroll) {
        this.scroll = scroll;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    @Override
    public void onClick(double x, double y) {
        this.model.setSelectWord(magicWord.name());
    }
}
