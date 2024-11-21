package teamHTBP.vidaReforged.client.screen.components.magicWords;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.hud.IVidaNodes;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.Locale;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class MagicSelectedWordWidget extends VidaWidget implements IVidaNodes {
    /**所属元素*/
    private final VidaElement element;
    /**空词条*/
    public final static ResourceLocation EMPTY_WORD_LOCATION = new ResourceLocation(MOD_ID, "textures/gui/magic_word_crafting.png");
    /**词条框*/
    public final static TextureSection SECTION = new TextureSection(EMPTY_WORD_LOCATION,48,8,16,16, 256, 256);
    /**选中的Id*/
    private String selectWordId;
    /**监听器*/
    private ClickListener listener = (element) -> {};


    public MagicSelectedWordWidget(int x, int y, VidaElement element, ClickListener listener) {
        super(x, y, 16, 16, Component.literal("selected_magic_word"), new ResourceLocation(MOD_ID, "select_magic_word_" + element.toString().toLowerCase(Locale.US)));
        this.element = element;
        this.listener = listener;
    }

    public void setSelectWordId(String selectWordId) {
        this.selectWordId = selectWordId;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        float alpha = isHovered ? 1f : 0.7f;

        RenderSystem.enableBlend();

        PoseStack poseStack = graphics.pose();
        graphics.setColor(alpha,alpha,alpha,alpha);
        poseStack.pushPose();
        graphics.blit(
                SECTION.location(),
                getX(), getY(), 0,
                SECTION.minU(), SECTION.minV(),
                SECTION.w(), SECTION.h(),
                256, 256
        );
        poseStack.popPose();
        RenderSystem.disableBlend();

        MagicWord word = MagicWordManager.getMagicWord(selectWordId);
        if(word == null){
            return;
        }
        // 绘制图标
        TextureSection section = new TextureSection(word.icon(),0,0,16,16, 16, 16);
        final int iconX = getX();
        final int iconY = getY();

        poseStack.pushPose();
        graphics.blit(
                section.location(),
                iconX, iconY, 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                16, 16
        );
        poseStack.popPose();
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if(this.listener != null){
            this.listener.onClick(element);
        }
    }

    public interface ClickListener{
        public void onClick(VidaElement element);
    }
}
