package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.client.events.Shaders;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VidaUnlockScreen extends GuiGraphics implements IVidaScreen {
    private static int tick = 0;

    public static LinkedList<String> magicWords = new LinkedList<>();

    private static FloatRange alphaRange = new FloatRange(0,0,0.7f);
    private static FloatRange textRange = new FloatRange(0,0,0.7f);

    private static FloatRange lengthRange = new FloatRange(64, 64, 100);

    private float x;
    private float y;

    private static final int HEIGHT = 32;
    private static final int MAX_WIDTH = 100;

    public VidaUnlockScreen(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
    }

    @Override
    public void render(PoseStack poseStack) {
        if(magicWords.size() <= 0){
            tick = 0;
            lengthRange = new FloatRange(32,32,guiWidth() * 1f / 3);
            alphaRange = new FloatRange(0,0, 0.7f);
            alphaRange.set(0f);
            lengthRange.set(0f);
            textRange.set(0f);
            return;
        }
        renderPopup(poseStack);
        renderText(poseStack);
        renderFadeOut();
        renderOver();
        tick++;
    }

    public void renderPopup(PoseStack poseStack){
        final float height = HEIGHT;
        final float alpha = alphaRange.increase(0.01f);
        final float width = lengthRange.increase(2f);

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

    public void renderText(PoseStack poseStack){
        if(tick <= 70){
            return;
        }
        float textAlpha = textRange.increase(0.02f);

        poseStack.pushPose();
        RenderSystem.enableBlend();
        ARGBColor color = new ARGBColor((int) (255 * textRange.get()), 8, 8, 8);
        Component title = Component.literal("已解锁词条");
        drawString(mc.font, title, (int) (x + 32), (int) (y + 6), color.argb());
        poseStack.popPose();

        MutableComponent magicWordComponent = Component.empty();
        for(String wordId : magicWords){
            MagicWord word = MagicWordManager.getMagicWord(wordId);
            if(word != null){
                magicWordComponent.append(
                        Component.translatable(String.format("%s.%s", word.namePrefix(), word.name()))
                ).append(" ");
            }
        }

        poseStack.pushPose();
        RenderSystem.enableBlend();
        color = new ARGBColor((int) (255 * textRange.get()), 120, 120, 120);
        drawString(mc.font, magicWordComponent, (int) (x + 32), (int) (y + 16), color.argb());
        poseStack.popPose();
    }

    public void renderFadeOut(){
        if(tick >= 700){
            alphaRange.decrease(0.02f);
            textRange.decrease(0.1f);
        }
    }

    public void renderOver(){
        if(tick >= 780) {
            magicWords.clear();
        }
    }


}
