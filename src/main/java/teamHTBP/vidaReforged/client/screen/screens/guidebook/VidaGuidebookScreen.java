package teamHTBP.vidaReforged.client.screen.screens.guidebook;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.client.screen.screens.common.VidaContainerScreen;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaScreenEventChannelViewModel;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.server.menu.VidaGuidebookMenu;

import java.util.LinkedList;
import java.util.List;

/**
 * Vida指导书总Screen
 * TODO
 *
 * DetailPage:详细介绍页面
 * */
public class VidaGuidebookScreen extends VidaContainerScreen<VidaGuidebookMenu> {
    /**显示堆栈*/
    LinkedList<VidaWidget> pageStack = new LinkedList<>();
    /**侧边工具栏*/
    List<VidaWidget> sideComponents = new LinkedList<>();
    /**弹出信息框*/
    VidaWidget toast;
    /***/
    VidaScreenEventChannelViewModel guidebookModel;

    public VidaGuidebookScreen() {
        super(null, null, Component.literal("vida_guidebook"));
    }

    @Override
    public void added() {
        super.added();
        guidebookModel = new ViewModelProvider(this).get(VidaScreenEventChannelViewModel.class);
        // 处理关闭

        // 处理侧边组件

        // 处理toast
    }

    public void handleOpen(){

    }

    public void handleClose(){
        pageStack.pop();
    }


    @Override
    protected void renderBg(GuiGraphics graphics, float p_97788_, int p_97789_, int p_97790_) {

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        VidaWidget page = pageStack.getLast();
        page.render(graphics, mouseX, mouseY, partialTicks);
    }
}
