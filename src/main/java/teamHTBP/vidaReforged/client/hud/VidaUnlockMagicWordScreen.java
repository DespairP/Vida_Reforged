package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class VidaUnlockMagicWordScreen extends GuiGraphics implements IVidaScreen {
    public static LinkedList<String> magicWords = new LinkedList<>();
    private static FloatRange alphaRange = new FloatRange(0,0,0.7f);
    private static FloatRange textRange = new FloatRange(0,0,0.7f);
    private static FloatRange lengthRange = new FloatRange(64, 64, 100);
    private float x;
    private float y;
    private static final int HEIGHT = 32;
    private static final int MAX_WIDTH = 100;
    private static final VidaGuiHelper.TickHelper tickHelper = new VidaGuiHelper.TickHelper();
    private static final int MAX_LENGTH = 3;

    public VidaUnlockMagicWordScreen(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
    }

    @Override
    public void render(GuiGraphics graphics, float partialTicks) {
        if(magicWords.size() <= 0){
            lengthRange = new FloatRange(32,32,guiWidth() * 0.3f);
            alphaRange = new FloatRange(0,0, 0.7f);
            alphaRange.set(0f);
            lengthRange.set(0f);
            textRange.set(0f);
            tickHelper.reset();
            return;
        }

        PoseStack poseStack = graphics.pose();

        tickHelper.tick(partialTicks);
        renderPopup(poseStack, partialTicks);
        renderText(poseStack, partialTicks);
        renderFadeOut();
        renderOver();
    }

    public void renderPopup(PoseStack poseStack, float partialTicks){
        final float height = HEIGHT;
        if(getTickLength() <= 10){
            alphaRange.increase(tickHelper.getTickPercent(0.1f));
            lengthRange.increase(tickHelper.getTickPercent((guiWidth() * 0.3f - 32f))/ 6f);
        }
        final float alpha = alphaRange.get();
        final float width = lengthRange.get();


        this.x = (guiWidth() - width) / 2;
        this.y = 16;

        final float iconY = (height - 16) / 2 + y;
        final float iconX = x + (height - 16) / 2;

        MagicWord word = MagicWordManager.getMagicWord(magicWords.get(0));
        if(word == null){
            return;
        }

        // 背景
        poseStack.pushPose();
        ARGBColor color = new ARGBColor((int) (255 * alphaRange.get()), 255, 255, 255);
        fillGradient(
                (int) x, (int) y,
                (int) (x + width), (int) (y + height),
                color.argb(),
                color.argb()
        );
        poseStack.popPose();

        // 背景边框
        poseStack.pushPose();
        VertexConsumer buffer = bufferSource().getBuffer(RenderType.gui());
        Matrix4f matrix4f = poseStack.last().pose();
        matrix4f.translate(x - 1, y - 1,0);
        float s = 1.4f;
        float a = Math.min(0.2f, alphaRange.get());
        float w = width + 1;
        float h = height + 1;

        //上
        buffer.vertex( matrix4f, 0, 0, 0).color(0,0,0,a).endVertex();
        buffer.vertex( matrix4f, s, s, 0).color(0,0,0,a).endVertex();
        buffer.vertex( matrix4f, w - s, s, 0).color(0,0,0, a).endVertex();
        buffer.vertex( matrix4f, w , 0, 0).color(1,0,0, a).endVertex();

        //左
        buffer.vertex( matrix4f, 0, 0, 0).color(0,0,0,a).endVertex();
        buffer.vertex( matrix4f, 0, h, 0 ).color(0,0,0,a).endVertex();
        buffer.vertex( matrix4f, s, h - s, 0 ).color(0,0,0, a).endVertex();
        buffer.vertex( matrix4f, s, s, 0).color(0,0,0, 0).endVertex();

        //下
        buffer.vertex( matrix4f, s, h - s, 0 ).color(0,0,0, a).endVertex();
        buffer.vertex( matrix4f, 0, h, 0 ).color(0,0,0, 0).endVertex();
        buffer.vertex( matrix4f, w, h, 0 ).color(1,0,0, 0).endVertex();
        buffer.vertex( matrix4f, w - s, h -s, 0 ).color(0,0,0, a).endVertex();

        //右
        buffer.vertex( matrix4f, w - s, s, 0 ).color(0,0,0, a).endVertex();
        buffer.vertex( matrix4f, w - s, h - s, 0 ).color(0,0,0, a).endVertex();
        buffer.vertex( matrix4f, w, h, 0 ).color(0,0,0, a).endVertex();
        buffer.vertex( matrix4f, w, 0, 0 ).color(0,0,0, a).endVertex();

        poseStack.popPose();

        // 图标
        poseStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1, 1, 1, alpha);
        Matrix4f matrix4f2 = poseStack.last().pose();
        matrix4f2.translate(x, y,0);

        blit(
                word.icon(),
                (int)iconX, (int)iconY, 0,
                0, 0,
                16, 16,
                16, 16
        );

        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    public void renderText(PoseStack poseStack, float partialTicks){
        if(getTickLength() <= 8){
            return;
        }
        if(getTickLength() <= 70){
            textRange.increase(tickHelper.getTickPercent(0.1f));
        }

        poseStack.pushPose();
        RenderSystem.enableBlend();
        ARGBColor color = new ARGBColor((int) (255 * textRange.get()), 8, 8, 8);
        Component title = Component.literal("已解锁词条");
        drawString(mc.font, title, (int) (x + 32), (int) (y + 6), color.fontColor());
        poseStack.popPose();

        MutableComponent magicWordComponent = Component.empty();
        HashSet<String> magicWordUnique = new HashSet<>();
        for (int i = 0; i < magicWords.size() ; i ++) {
            String wordId = magicWords.get(i);
            if(magicWordUnique.contains(wordId) || magicWordUnique.size() >= MAX_LENGTH){
                magicWordUnique.add(wordId);
                continue;
            }
            magicWordUnique.add(wordId);
            MagicWord word = MagicWordManager.getMagicWord(wordId);
            if(word != null){
                magicWordComponent.append(
                        Component.translatable(String.format("%s.%s", word.namePrefix(), word.name()))
                ).append(" ");
            }
        }

        if(magicWordUnique.size() > MAX_LENGTH){
            magicWordComponent.append(Component.literal("+ more"));
        }

        poseStack.pushPose();
        RenderSystem.enableBlend();
        color = new ARGBColor((int) (255 * textRange.get()), 120, 120, 120);
        drawString(mc.font, magicWordComponent, (int) (x + 32), (int) (y + 16), color.fontColor());
        poseStack.popPose();
    }

    public void renderFadeOut(){
        if(getTickLength() >= 70){
            alphaRange.decrease(tickHelper.getTickPercent(0.02f));
            textRange.decrease(tickHelper.getTickPercent(0.02f));
        }
    }

    public void renderOver(){
        if(getTickLength() >= 120) {
            magicWords.clear();
            tickHelper.reset();
        }
    }

    public int getTickLength(){
        return (int) (tickHelper.currentTick - tickHelper.startTick);
    }


}
