package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.core.utils.animation.DestinationAnimator;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.helper.RenderHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GuideBookScrollTextArea extends AbstractWidget implements IGuidebookComponent {
    /**字体*/
    private Font font;
    /**匹配${}格式的正则*/
    private final Pattern pattern;
    /**${}正则*/
    private final String regex;
    /**显示的所有字*/
    private String all;
    /**滚动*/
    private AtomicInteger scroll = new AtomicInteger(0);
    /**滚动条透明度*/
    private FloatRange scrollBarAlpha = new FloatRange(0,0,0.3f);
    /**全局透明度*/
    private DestinationAnimator<Float> globalAlphaAnim;

    public GuideBookScrollTextArea(String key, int x, int y, int width, int height) {
        super(x, y, width, height, Component.translatable("text"));
        this.font = Minecraft.getInstance().font;
        this.regex = "\\$\\{.*?\\}";
        this.pattern = Pattern.compile(regex);
        this.all = Language.getInstance().getOrDefault(key, key);
        this.globalAlphaAnim = DestinationAnimator.of(3,0f,0.95f);
        this.globalAlphaAnim.start();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //动画
        this.globalAlphaAnim.tick(partialTicks);
        float alpha = this.globalAlphaAnim.getValue();
        //渲染
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(alpha, alpha,alpha,alpha);
        renderBg(graphics, mouseX, mouseY, partialTicks);
        renderText(graphics,mouseX,mouseY,partialTicks);
        drawScrollBar(graphics, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.disableBlend();
    }

    /**渲染滚动条*/
    private void drawScrollBar(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        float alpha = scrollBarAlpha.change(isHovered, 0.02f);


        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());
        int iconHeight = Math.max(5, getHeight() - getMaxScrollHeight());
        Matrix4f matrix4f = poseStack.last().pose();
        matrix4f.translate(
                getX() + getWidth(),
                Math.min(getY() - scroll.get(), getY() + getHeight() - 5),
                0
        );
        consumer.vertex(matrix4f, 0, 0, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 0, iconHeight, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 3, iconHeight, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        consumer.vertex(matrix4f, 3, 0, 0).color(0.8f, 0.8f, 0.8f, alpha).endVertex();
        poseStack.popPose();
    }

    /**获取最大能滚动的高度*/
    public int getMaxScrollHeight(){
        String text = this.splitString(this.all).stream().filter(str -> !str.matches(regex)).collect(Collectors.joining());
        int allWordHeight = font.wordWrapHeight(text, getWidth());
        return Math.max(0, allWordHeight - getHeight() + 10);
    }

    /**渲染*/
    protected void renderBg(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        graphics.fillGradient(
                getX(),
                getY(),
                getX() + this.width,
                getY() + this.height,
                0x50000000,
                0x20000000
        );
        poseStack.popPose();
    }

    /**渲染文字*/
    protected void renderText(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack poseStack = graphics.pose();
        MutableComponent component = Component.empty();


        List<String> strList = this.splitString(this.all);
        for (int i = 0; i < strList.size(); i++) {
            Style style = Style.EMPTY;
            for (int j = i; j < strList.size(); j++) {
                String subStr = strList.get(j);
                if (subStr.matches(regex) && !("${}".equals(subStr))) {
                    style = doMatchStyle(style, subStr);
                    continue;
                }
                i = j;
                break;
            }
            component.append(Component.literal(strList.get(i)).withStyle(style));
        }

        poseStack.pushPose();
        RenderSystem.enableBlend();

        RenderHelper.renderScissor(getX() + 3,getY(), getWidth(), getHeight());
        poseStack.translate(10, 10 + scroll.get() , 0);
        graphics.drawWordWrap(font, component, getX(), getY(), getWidth() - 13, 0xFFFFFFFF);

        RenderSystem.disableScissor();
        RenderSystem.disableBlend();
        poseStack.popPose();

    }

    /**
     * 分割字符串
     */
    public List<String> splitString(String str) {

        final String string = str;

        final Matcher matcher = pattern.matcher(string);

        int start = 0;
        int end = 0;
        List<String> renderStrings = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.start() != start) {
                renderStrings.add(string.substring(start, matcher.start()));
            }
            renderStrings.add(matcher.group(0));
            start = matcher.end();
            end = matcher.end();
        }
        renderStrings.add(string.substring(end));

        return renderStrings;
    }

    /**
     * 以${}解析风格类型
     */
    public Style doMatchStyle(Style style, String styleString) {
        try {
            //颜色匹配
            if (styleString.contains("#")) {
                Pattern colorPattern = Pattern.compile("[0-9A-F]+");
                Matcher matcher = colorPattern.matcher(styleString);
                if (!matcher.find()) {
                    return style;
                }
                int colorHex = Integer.parseInt(matcher.group(0), 16);
                style = style.withColor(colorHex);
            }

            //粗体匹配
            if (styleString.contains("bold")) {
                style = style.withBold(true);
            }

            // 斜体匹配
            if (styleString.contains("it")) {
                style = style.withItalic(true);
            }

            //下划线匹配
            if (styleString.contains("u")) {
                style = style.withUnderlined(true);
            }

            //删除
            if (styleString.contains("del")) {
                style = style.withStrikethrough(true);
            }

            return style;
        } catch (Exception ex) {
            return style;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        int nextScroll = this.scroll.get() + (int)scroll * 10;
        int maxScroll = getMaxScrollHeight();
        this.scroll.set(nextScroll);
        if(nextScroll * -1 > maxScroll){
            this.scroll.set(maxScroll * -1);
        }
        if(nextScroll > 0){
            this.scroll.set(0);
        }
        return true;
    }

    public void setNewWord(String key){
        this.all = Language.getInstance().getOrDefault(key, key);
        this.scroll.set(0);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }
}
