package teamHTBP.vidaReforged.client.screen.screens.guidebook;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.common.ComponentsContainer;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static teamHTBP.vidaReforged.client.screen.screens.guidebook.VidaGuidebookListScreen.*;

/**
 * proxy，负责容器渲染的代理类，guidebook会把书本容器内渲染逻辑交给这个类处理
 * TODO: 过于复杂
 * */
public class VidaGuidebookPageContainer extends VidaWidget {
    /**背景*/
    ComponentsContainer foreground;
    /**透明度*/
    public FloatRange alpha = new FloatRange(0, 0, 1);
    /**内部透明度*/
    public FloatRange innerAlpha = new FloatRange(0, 0, 1);
    /**现在正在显示的页面*/
    protected VidaGuidebookPage currentPage;
    /**上次显示的页面*/
    protected VidaGuidebookPage prevPage;
    /**页面栈*/
    private LinkedList<VidaGuidebookPage> page = new LinkedList<>();


    public VidaGuidebookPageContainer(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        this.foreground = new ComponentsContainer(x, y, width, height, this::renderBackground, this::rearrange);
    }

    /**设置当前新页*/
    public void setPage(VidaGuidebookPage pageToSet){
        if(pageToSet == null){
            return;
        }
        // 正在显示的信息被隐藏
        if(currentPage != null) {
            currentPage.onHide();
        }
        this.prevPage = this.currentPage;
        // 设置新的页面
        this.currentPage = pageToSet;
        this.page.push(pageToSet);
        pageToSet.init();
    }

    /**移除当前页*/
    public void removePage(){
        if (page.size() <= 1){
            return;
        }
        VidaGuidebookPage page = this.page.pop();
        page.onRemove();
        page.visible = false;
        this.currentPage = this.page.getLast();
        this.currentPage.setVisible(true);
        this.prevPage = null;
    }


    public void rearrange(List<VidaWidget> widgets){
        GridLayout layout = new GridLayout();
        layout.spacing(20);
        for (int i = 0; i < widgets.size(); i++) {
            layout.addChild(widgets.get(i),  i, 0);
        }
        layout.arrangeElements();
    }


    /**
     * 渲染书页背景
     * @param graphics 渲染
     * @param x x
     * @param y y
     * @param mouseX 鼠标x
     * @param mouseY 鼠标y
     * @param partialTicks 帧
     */
    public void renderBackground(GuiGraphics graphics, int x, int y, int mouseX, int mouseY, float partialTicks){
        this.alpha.increase(0.1f * Minecraft.getInstance().getDeltaFrameTime());
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1, 1, 1, alpha.get());
        //
        graphics.blit(
                GUIDEBOOK_RESOURCE,
                (int)x, (int)y, 0,
                GUIDEBOOK_SECTION.minU(), GUIDEBOOK_SECTION.minV(),
                GUIDEBOOK_SECTION.w(), GUIDEBOOK_SECTION.h(),
                SECTION_SIZE, SECTION_SIZE
        );
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }



    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {//=
        // 渲染
        if(this.foreground != null) {
            this.foreground.setX(getX());
            this.foreground.setY(getY());
            this.foreground.render(graphics, mouseX, mouseY, partialTicks);
        }

        if(this.currentPage != null){
            this.currentPage.setX(getX() + 10);
            this.currentPage.setY(getY() + 10);
            this.currentPage.render(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public Collection<? extends GuiEventListener> children() {
        return currentPage.children();
    }
}
