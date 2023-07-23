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
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.component.IDataObserver;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class MagicWordWidget extends AbstractWidget {
    public static final int HEIGHT = 20;
    public static final int WIDTH = 85;
    public static final int ICON_PIXEL = 16;
    public final MagicWord magicWord;
    public static ResourceLocation DINKFONT = new ResourceLocation(MOD_ID, "dinkie");
    public static ResourceLocation QUESTION_MARK = new ResourceLocation(MOD_ID, "textures/icons/magic_word/question_mark.png");
    public FloatRange upperBorderPoint = new FloatRange(0,0, WIDTH);
    public FloatRange downBorderPoint = new FloatRange(0,0, WIDTH);
    public FloatRange selectedAlpha = new FloatRange(0,0, 1);
    private MagicWordListWidget container;
    private VidaElement element;
    private VidaMagicWordViewModel model;
    private boolean isSelected = false;
    private boolean isUnlocked = false;

    public MagicWordWidget(VidaMagicWordViewModel model,MagicWordListWidget parent, int x, int y, MagicWord word) {
        super(x, y, WIDTH, HEIGHT, Component.translatable("magic word"));
        this.magicWord = word;
        this.model = model;
        this.container = parent;
        this.init();
    }

    public void init(){
        IDataObserver<Map<VidaElement,String>> observer = this::checkIfSelected;
        this.model.selectedMagicWord.observe(observer);
        this.checkIfSelected(this.model.selectedMagicWord.getValue());
        if(magicWord != null){
            this.isUnlocked = Optional.ofNullable(this.model.playerMagicWords.getValue()).orElse(new ArrayList<>()).contains(magicWord.name());
        }
    }

    public void checkIfSelected(Map<VidaElement,String> value){
        if(magicWord == null || value == null || magicWord.element() == null){
            setSelected(false);
            return;
        }
        String selectedMagicId = value.get(magicWord.element());
        if(selectedMagicId == null){
            setSelected(false);
            return;
        }
        setSelected(selectedMagicId.equals(magicWord.name()));
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.isHovered = mouseX >= this.getX() &&
                mouseY >= this.getY() &&
                mouseX < this.getX() + this.width &&
                mouseY < this.getY() + this.height;

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
        buffer.vertex( matrix4f, WIDTH, HEIGHT, 0).color(r, g, b, 0.5f).endVertex();
        buffer.vertex( matrix4f, WIDTH , 0, 0).color(r, g, b, 0.5f).endVertex();

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
        TextureSection section = new TextureSection(iconLocation,0,0,16,16);
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
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer buffer = graphics.bufferSource().getBuffer(RenderType.gui());
        matrix4f.translate(getX(), getY(), 0);
        ARGBColor fromColor = ARGBColor.of(125, 226, 252);
        float fromR = fromColor.r() / 255.0f;
        float fromG = fromColor.g() / 255.0f;
        float fromB = fromColor.b() / 255.0f;

        ARGBColor toColor = ARGBColor.of(185, 182, 229);
        float toR = toColor.r() / 255.0f;
        float toG = toColor.g() / 255.0f;
        float toB = toColor.b() / 255.0f;

        buffer.vertex(matrix4f, 0, 0, 0).color(toR, toG, toB, alpha).endVertex();
        buffer.vertex(matrix4f, 0, HEIGHT, 0).color(fromR, fromG, fromB, alpha).endVertex();
        buffer.vertex(matrix4f, WIDTH, HEIGHT, 0).color(toR, toG, toB, alpha).endVertex();
        buffer.vertex(matrix4f, WIDTH, 0, 0).color(fromR, fromG, fromB, alpha).endVertex();


        poseStack.popPose();
    }

    public int getScroll(){
        return this.container == null ? 0 : this.container.getScroll();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    public boolean isSelectable(){
        return this.isUnlocked;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY() + this.getScroll();
    }

    @Override
    public void onClick(double x, double y) {
        if(!this.isSelectable()){
            return;
        }
        this.model.setSelectWord(magicWord.element(),magicWord.name());
    }


}
