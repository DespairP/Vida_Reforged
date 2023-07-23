package teamHTBP.vidaReforged.client.screen.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class GuideBookScrollTextArea extends AbstractWidget {
    Font font;

    Pattern pattern;

    String regex;

    public static ResourceLocation VONWAON = new ResourceLocation(MOD_ID, "quan");

    public GuideBookScrollTextArea(int x, int y, int width, int height) {
        super(x, y, width, height, Component.translatable("text"));
        this.font = Minecraft.getInstance().font;
        this.regex = "\\$\\{.*?\\}";
        this.pattern = Pattern.compile(regex);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        graphics.fillGradient(
                getX(),
                getY(),
                getX() + this.width,
                getY() + this.height,
                -1072689136,
                -804253680
        );
        poseStack.popPose();

        MutableComponent component = Component.empty();
        List<String> strList = this.splitString("你好\n这里是Vida\n${#00FF90}这个\n${#FF7A7A}wow\n酷！元素的mod");
        for(int i = 0; i < strList.size() ; i++){
            Style style = Style.EMPTY.withFont(VONWAON);
            for(int j = i; j < strList.size(); j++){
                String subStr = strList.get(j);
                if(subStr.matches(regex)){
                    style = doMatchStyle(style,subStr);
                    continue;
                }
            i = j;
            break;
        }
        component.append(Component.literal(strList.get(i)).withStyle(style));
    }
        poseStack.pushPose();
        graphics.drawWordWrap(font, component, getX(), getY(), getWidth(), 0xFFFFFFFF);
        poseStack.popPose();
}

    /**分割字符串*/
    public List<String> splitString(String str){

        final String string = str;

        final Matcher matcher = pattern.matcher(string);

        int start = 0;
        int end = 0;
        List<String> renderStrings = new ArrayList<>();
        while (matcher.find()){
            if(matcher.start() != start) {
                renderStrings.add(string.substring(start, matcher.start()));
            }
            renderStrings.add(matcher.group(0));
            start = matcher.end();
            end = matcher.end();
        }
        renderStrings.add(string.substring(end));

        return renderStrings;
    }

    /**以${}解析风格类型*/
    public Style doMatchStyle(Style style,String styleString){
        try{
            //颜色匹配
            if(styleString.contains("#")){
                Pattern colorPattern = Pattern.compile("[0-9A-F]+");
                Matcher matcher = colorPattern.matcher(styleString);
                if(!matcher.find()){
                    return style;
                }
                int colorHex = Integer.parseInt(matcher.group(0), 16);
                style = style.withColor(colorHex);
            }

            //粗体匹配
            if(styleString.contains("bold")){
                style = style.withBold(true);
            }

            // 斜体匹配
            if(styleString.contains("it")){
                style = style.withItalic(true);
            }

            //下划线匹配
            if(styleString.contains("u")){
                style = style.withUnderlined(true);
            }

            return style;
        }catch (Exception ex){
            return style;
        }
    }




    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }
}
