package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VidaGuidebookText extends VidaWidget implements IVidaGuidebookComponent {
    /**字体*/
    private Font font;
    /**匹配${}格式的正则*/
    private final Pattern pattern;
    /**${}正则*/
    private final String regex;
    /**显示的所有字*/
    private String all;
    public VidaGuidebookText(String key, int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal("text"));
        this.font = Minecraft.getInstance().font;
        this.regex = "\\$\\{.*?\\}";
        this.pattern = Pattern.compile(regex);
        this.all = Language.getInstance().getOrDefault(key, key);
    }

    public VidaGuidebookText(Component key, int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal("text"));
        this.font = Minecraft.getInstance().font;
        this.regex = "\\$\\{.*?\\}";
        this.pattern = Pattern.compile(regex);
        this.all = key.getString();
    }


    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //渲染
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(alpha, alpha,alpha,alpha);
        renderBg(graphics, mouseX, mouseY, partialTicks);
        renderText(graphics,mouseX,mouseY,partialTicks);
        //drawScrollBar(graphics, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.disableBlend();
    }



    /**渲染*/
    protected void renderBg(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
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

        poseStack.translate(-0.2F, -0.2f, 0.03F);
        graphics.drawWordWrap(font, component, getX(), getY(), getWidth(), 0xFFFFFFFF);

        RenderSystem.disableScissor();
        RenderSystem.disableBlend();
        poseStack.popPose();

    }

    /**
     * 分割字符串
     */
    public List<String> splitString(String str) {
        final String value = str;
        final Matcher matcher = pattern.matcher(value);

        int start = 0;
        int end = 0;
        List<String> renderStrings = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.start() != start) {
                renderStrings.add(value.substring(start, matcher.start()));
            }
            renderStrings.add(matcher.group(0));
            start = matcher.end();
            end = matcher.end();
        }
        renderStrings.add(value.substring(end));

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
        return true;
    }

    public void setNewWord(String key){
        this.all = Language.getInstance().getOrDefault(key, key);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    @Override
    public void init() {

    }
}
