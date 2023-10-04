package teamHTBP.vidaReforged.client.screen.screens.magicwordAchieve;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import teamHTBP.vidaReforged.client.screen.components.magicWords.MagicWordShowSection;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaViewMagicWordViewModel;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.server.menu.MagicWordViewMenu;

import java.util.ArrayList;
import java.util.List;

public class MagicWordScreen extends AbstractContainerScreen<MagicWordViewMenu> {
    FloatRange alphaRange = new FloatRange(0, 0, 0.75f);
    VidaViewMagicWordViewModel model;
    MagicWordSingleListWidget singleList;
    MagicWordShowSection textArea;

    public MagicWordScreen(MagicWordViewMenu menu, Inventory playerInventory, Component component) {
        super(menu, Minecraft.getInstance().player.getInventory(), Component.literal("view magic word"));
        model = new VidaViewMagicWordViewModel();
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

        singleList = new MagicWordSingleListWidget(model, x, y, width, height);
        textArea = new MagicWordShowSection(model, this.width - textWidth - 10, y, textWidth, height);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1,1,1,alphaRange.get());

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
        alphaRange.increase(0.02f);
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        ARGBColor color = ARGBColor.of(0.75F, 0f, 0f, 0f);
        graphics.fillGradient(0,0,width, height, color.argb(), color.argb());
        poseStack.popPose();
    }

    @Override
    protected void clearWidgets() {
        this.model.selectedMagicWord.clearObservers();
        this.model.playerMagicWords.clearObservers();
        super.clearWidgets();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double partialTicks) {
        List<GuiEventListener> listeners = new ArrayList<>();

        for(GuiEventListener guieventlistener : this.children()) {
            if (guieventlistener.isMouseOver(mouseX, mouseY)) {
                listeners.add(guieventlistener);
            }
        }

        boolean isScrolled = listeners.stream().reduce(false, (total, listener) -> listener.mouseScrolled(mouseX, mouseY, partialTicks), Boolean::logicalOr);

        return isScrolled;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> listeners = new ArrayList<>();
        listeners.addAll(this.singleList.getChildren());
        listeners.add(this.singleList);
        listeners.add(this.textArea.getChildren());
        return listeners;
    }
}
