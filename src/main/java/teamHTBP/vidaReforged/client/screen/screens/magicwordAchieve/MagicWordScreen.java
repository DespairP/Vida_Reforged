package teamHTBP.vidaReforged.client.screen.screens.magicwordAchieve;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.client.screen.components.magicWords.MagicWordShowSection;
import teamHTBP.vidaReforged.client.screen.screens.common.VidaContainerScreen;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaViewMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.screen.StyleSheet;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.server.menu.MagicWordViewMenu;

import java.util.ArrayList;
import java.util.List;

public class MagicWordScreen extends VidaContainerScreen<MagicWordViewMenu> {
    /**透明度*/
    @StyleSheet
    FloatRange alpha = new FloatRange(0, 0, 0.75f);
    /**ViewModel*/
    VidaViewMagicWordViewModel model;
    /**子组件*/
    MagicWordSingleListWidget singleList;
    MagicWordShowSection textArea;

    public MagicWordScreen(MagicWordViewMenu menu, Inventory playerInventory, Component component) {
        super(menu, playerInventory, component);
    }


    @Override
    public void added() {
        super.added();
        this.model = new ViewModelProvider(this).get(VidaViewMagicWordViewModel.class);
        this.model.playerMagicWords.setValue(menu.getPlayerMagicWords());
    }

    @Override
    protected void init() {
        super.init();

        int width = (int) (this.width * 0.8f / 3);
        int height = (int) (this.height - 28);
        int x = (int) (this.width * 0.2 / 3);
        int y = (int)((this.height - height) / 2);


        int textWidth = (int) (this.width * 1.8f / 3.0f);
        int textX = this.width - textWidth - 10;

        singleList = new MagicWordSingleListWidget(x, y, width, height);
        try {
            textArea = new MagicWordShowSection(textX, y, textWidth, height);
        }catch (Exception exception){
            LOGGER.error(exception);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1,1,1, alpha.get());

        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        singleList.render(graphics, mouseX, mouseY, partialTicks);
        textArea.render(graphics, mouseX, mouseY, partialTicks);

        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.disableBlend();
    }

    @Override
    protected void renderBg(GuiGraphics p_283065_, float p_97788_, int p_97789_, int p_97790_) {

    }

    @Override
    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {

    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        alpha.increase(0.02f);
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        ARGBColor color = ARGBColor.of(0.75F, 0f, 0f, 0f);
        graphics.fillGradient(0,0,width, height, color.argb(), color.argb());
        poseStack.popPose();
    }

    @Override
    protected void clearWidgets() {
        this.model.selectedMagicWord.clearObservers(this);
        this.model.playerMagicWords.clearObservers(this);
        super.clearWidgets();
    }

    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> listeners = new ArrayList<>();
        listeners.addAll(this.singleList.children());
        listeners.add(this.singleList);
        listeners.addAll(this.textArea.children());
        return listeners;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
        boolean isItemStackDragged = super.mouseDragged(mouseX, mouseY, mouseButton, dragX, dragY);
        return this.getFocused() != null && this.isDragging() && mouseButton == 0 ? this.getFocused().mouseDragged(mouseX, mouseY, mouseButton, dragX, dragY) : false;
    }

}
