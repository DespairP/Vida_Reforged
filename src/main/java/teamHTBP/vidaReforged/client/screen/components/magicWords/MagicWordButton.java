package teamHTBP.vidaReforged.client.screen.components.magicWords;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.screen.StyleSheet;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
/**
 * 显示词条的按钮
 * */
public class MagicWordButton extends VidaWidget {
    /**通用高度和宽度*/
    public static final int HEIGHT = 20;
    public static final int WIDTH = 85;
    /**词条大小*/
    private final static int ICON_SIZE = MagicWord.getIconSize();
    public final static String COMPONENT_MARK = "gui.vida_reforged.name.magic_word";
    /**锁住状态的图标*/
    public final static ResourceLocation QUESTION_MARK = new ResourceLocation(MOD_ID, "textures/icons/magic_word/question_mark.png");
    /**字体路径*/
    @StyleSheet
    public static ResourceLocation DINKFONT = new ResourceLocation(MOD_ID, "dinkie");
    /**词条id*/
    protected String magicWordId = "";
    /**词条是否为锁住状态*/
    protected boolean isLocked = false;
    /**词条id对应的词条*/
    private MagicWord magicWord;
    /**动画*/
    public FloatRange upperBorderPoint = new FloatRange(0,0, width);
    public FloatRange downBorderPoint = new FloatRange(0,0, width);
    public FloatRange selectedAlpha = new FloatRange(0,0, 1);
    /**是否是选中状态*/
    private boolean selected = false;
    /**选中背景色*/
    @StyleSheet
    private ARGBColor fromColor = ARGBColor.of(125, 226, 252);
    @StyleSheet
    private ARGBColor toColor = ARGBColor.of(185, 182, 229);
    /**监听器*/
    private ClickListener click;


    /**
     * @param initX 初始化X位置
     * @param initY 初始化Y位置
     * @param width 组件宽度
     * @param height 组件高度
     * @param magicWordId 词条Id
     */
    public MagicWordButton(int initX, int initY, int width, int height, String magicWordId, ResourceLocation id) {
        super(initX, initY, width, height, Component.translatable(COMPONENT_MARK), id);
        this.magicWord = MagicWordManager.getMagicWord(magicWordId);
        this.magicWordId = magicWord.name();
    }

    /**重新设置词条id*/
    public void setMagicWordId(String magicWordId) {
        this.magicWordId = magicWordId;
        this.magicWord = MagicWordManager.getMagicWord(magicWordId);
    }

    /**设置是否可用*/
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    /**渲染按钮被选中背景*/
    public void renderSelectedBg(GuiGraphics graphics, float partialTicks){
        float alpha = this.selectedAlpha.change(this.selected, 0.3f * mc.getDeltaFrameTime());

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();

        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer buffer = graphics.bufferSource().getBuffer(RenderType.gui());
        matrix4f.translate(getX(), getY(), 0);

        float fromR = fromColor.r() / 255.0f;
        float fromG = fromColor.g() / 255.0f;
        float fromB = fromColor.b() / 255.0f;

        float toR = toColor.r() / 255.0f;
        float toG = toColor.g() / 255.0f;
        float toB = toColor.b() / 255.0f;

        buffer.vertex(matrix4f, 0, 0, 0).color(toR, toG, toB, alpha).endVertex();
        buffer.vertex(matrix4f, 0, getHeight(), 0).color(fromR, fromG, fromB, alpha).endVertex();
        buffer.vertex(matrix4f, getWidth(), getHeight(), 0).color(toR, toG, toB, alpha).endVertex();
        buffer.vertex(matrix4f, getWidth(), 0, 0).color(fromR, fromG, fromB, alpha).endVertex();

        poseStack.popPose();
    }


    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // 动画
        upperBorderPoint.change(isHovered, width / 4.5f * mc.getDeltaFrameTime());
        downBorderPoint.change(isHovered, width / 4.6f * mc.getDeltaFrameTime());

        // 绘制选择的背景
        renderSelectedBg(graphics, partialTicks);

        // 绘制背景
        renderHoverEffect(graphics, partialTicks);

        // 绘制词条文字
        renderMagicWord(graphics, partialTicks);
    }

    /**显示悬浮特效*/
    protected void renderHoverEffect(GuiGraphics graphics, float partialTicks){
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
        buffer.vertex( matrix4f, 0 + dn, getHeight(), 0).color(r, g, b, 0.5f).endVertex();
        buffer.vertex( matrix4f, getWidth(), getHeight(), 0).color(r, g, b, 0.5f).endVertex();
        buffer.vertex( matrix4f, getWidth() , 0, 0).color(r, g, b, 0.5f).endVertex();

        buffer.vertex( matrix4f, 0, 0, 0).color(1, 1, 1, 0.5f).endVertex();
        buffer.vertex( matrix4f, 0, getHeight(), 0).color(1, 1, 1, 0.5f).endVertex();
        buffer.vertex( matrix4f, dn, getHeight(), 0).color(1, 1, 1, 0.2f).endVertex();
        buffer.vertex( matrix4f, up , 0, 0).color(1, 1, 1, 0.2f).endVertex();

        poseStack.popPose();
    }


    public void renderMagicWord(GuiGraphics graphics, float partialTicks){
        PoseStack poseStack = graphics.pose();


        if(magicWord == null){
            return;
        }

        String magicNameKey = !isLocked ? String.format("%s.%s", magicWord.namePrefix(), magicWord.name()) : "???";
        ResourceLocation iconLocation = !isLocked ? magicWord.icon() : QUESTION_MARK;

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
        TextureSection section = new TextureSection(iconLocation,0,0,16,16, ICON_SIZE, ICON_SIZE);
        final int iconX = getX() + 4;
        final int iconY = getY() + ((HEIGHT - ICON_SIZE) / 2) ;

        poseStack.pushPose();
        graphics.blit(
                section.location(),
                iconX, iconY, 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                ICON_SIZE, ICON_SIZE
        );
        poseStack.popPose();
    }


    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.HINT,  Component.translatable(COMPONENT_MARK));
    }

    public void setOnClickListener(ClickListener click) {
        this.click = click;
    }

    @Override
    public void onClick(double x, double y) {
        if(!this.isSelectable()){
            return;
        }
        if(click != null){
            click.onclick(magicWord.element(), magicWordId);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(!visible){
            this.selectedAlpha.set(0);
        }
    }

    private boolean isSelectable() {
        return !isLocked;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getMagicWordId(){
        return magicWordId;
    }

    @Override
    public boolean mouseScrolled(double p_94734_, double p_94735_, double p_94736_) {
        return false;
    }

    public interface ClickListener{
        public void onclick(VidaElement element, String magicId);
    }
}
