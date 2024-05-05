package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.core.common.system.guidebook.DisplayInfo;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaScreenEvent;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageListItem;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaGuidebookListItemButton extends VidaWidget {
    /**通用高度和宽度*/
    public static final int HEIGHT = 20;
    public static final int WIDTH = 85;
    /**锁住状态的图标*/
    public final static ResourceLocation QUESTION_MARK = new ResourceLocation(MOD_ID, "textures/icons/magic_word/question_mark.png");
    /**词条id*/
    protected String magicWordId = "";
    /**词条是否为锁住状态*/
    protected boolean isLocked = false;
    /**渲染*/
    private VidaPageListItem item;
    /**动画*/
    public FloatRange upperBorderPoint = new FloatRange(0,0, width);
    public FloatRange downBorderPoint = new FloatRange(0,0, width);
    public FloatRange selectedAlpha = new FloatRange(0,0, 1);
    /**监听器*/
    private ClickListener click;
    /***/
    GridLayout layout = new GridLayout();
    StringWidget title = new StringWidget(Component.literal(""), mc.font);
    StringWidget description = new StringWidget(Component.literal("work..."), mc.font);
    public static final ResourceLocation TEST = new ResourceLocation(MOD_ID, "hedvig_letters_sans_regular");



    public VidaGuidebookListItemButton(int x, int y, int width, int height, Component component, VidaPageListItem item, ClickListener listener) {
        super(x, y, width, height, component);
        this.item = item;
        this.layout = new GridLayout(getX(), getY());
        this.layout.addChild(title, 0, 0, this.layout.defaultCellSetting().alignHorizontallyLeft());
        this.click = listener;
    }

    @Override
    public void setOffset(int offsetX, int offsetY) {
        super.setOffset(offsetX, offsetY);
        this.layout.setX(getX() + 25);
        this.layout.setY(getY() + 6);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // 动画
        upperBorderPoint.change(isHovered, width / 4.5f * mc.getDeltaFrameTime());
        downBorderPoint.change(isHovered, width / 4.6f * mc.getDeltaFrameTime());

        // 绘制背景
        renderHoverEffect(graphics, partialTicks);

        // 绘制词条文字
        renderItem(graphics, mouseX, mouseY ,partialTicks);
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
        buffer.vertex( matrix4f, 0, 0, 0).color(1, 1, 1, 0.5f).endVertex();
        buffer.vertex( matrix4f, 0, getHeight(), 0).color(1, 1, 1, 0.5f).endVertex();
        buffer.vertex( matrix4f, dn, getHeight(), 0).color(1, 1, 1, 0.2f).endVertex();
        buffer.vertex( matrix4f, up , 0, 0).color(1, 1, 1, 0.2f).endVertex();

        poseStack.popPose();
    }


    public void renderItem(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        PoseStack poseStack = graphics.pose();

        if(item == null || item.getInfo() == null){
            return;
        }

        DisplayInfo info = item.getInfo();

        String nameKey = !isLocked ? String.format("%s.%s:%s", "guidebook.list_item", item.getId().getNamespace(), item.getId().getPath()) : "???";
        ResourceLocation iconLocation = !isLocked ? info.texIcon : QUESTION_MARK;

        // 绘制名字
        poseStack.pushPose();
        String name = Language.getInstance().getOrDefault(nameKey, nameKey);
        name = name.substring(0,Math.min(name.length(), 40));
        Component titleKey = Component.literal(name).withStyle((style) -> {
            return style.withBold(false).withFont(Minecraft.UNIFORM_FONT);
        });
        title.setMessage(titleKey);
        title.setWidth(mc.font.width(titleKey.getVisualOrderText()));
        layout.arrangeElements();
        title.renderWidget(graphics, mouseX, mouseY, partialTicks);
        poseStack.popPose();

        // 绘制图标
        final int iconX = getX() + 8;
        final int iconY = getY() + ((getHeight() - info.getTexIconSize()) / 2) ;
        final float factor = 1F / 0.8F;
        if(info.getItemIcon() != null && !info.getItemIcon().isEmpty()){
            poseStack.pushPose();
            poseStack.scale(0.8F, 0.8F, 0.8F);
            graphics.renderItem(info.getItemIcon(), (int)(iconX * factor), (int)((getY() + (getHeight() - 12) / 2) * factor));
            poseStack.popPose();
            return;
        }
        TextureSection section = new TextureSection(iconLocation,0,0, info.getTexIconSize(), info.getTexIconSize(), info.getTexIconSize(), info.getTexIconSize());


        poseStack.pushPose();
        graphics.blit(
                section.location(),
                iconX, iconY, 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                info.getTexIconSize(), info.getTexIconSize()
        );
        poseStack.popPose();
    }

    @Override
    public void onClick(double x, double y) {
        if(this.click != null && this.item.getEvent() != null && this.item.getEvent().isPresent()){
            click.onClick(item.getEvent().get(), item.getId());
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.HINT,  Component.literal("list_item." + item.getId().toString()));
    }

    public interface ClickListener{
        public void onClick(VidaScreenEvent event, ResourceLocation id);
    }
}
