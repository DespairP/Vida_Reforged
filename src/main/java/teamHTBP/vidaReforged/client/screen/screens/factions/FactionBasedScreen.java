package teamHTBP.vidaReforged.client.screen.screens.factions;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import teamHTBP.vidaReforged.client.screen.screens.common.VidaContainerScreen;
import teamHTBP.vidaReforged.client.screen.screens.wandCrafting.VidaFragmentController;
import teamHTBP.vidaReforged.core.common.ui.layouts.GridLayoutMutation;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.menu.AbstractFactionMenu;

import java.util.ArrayList;
import java.util.List;

public abstract class FactionBasedScreen<T extends AbstractFactionMenu> extends VidaContainerScreen<T> {
    private boolean isShowPlayerInventory = false;
    private GridLayoutMutation gridLayout;
    private FactionSection leftSection;
    private FactionSection rightSection;
    private ScreenRectangle leftSectionRect;
    private VidaFragmentController controller;

    protected FactionBasedScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public void added() {
        super.added();
        this.leftSection = this.addEntityFactionSection();
        this.rightSection = this.addDetailedFactionSection();
        this.gridLayout = new GridLayoutMutation();
        this.leftSection.onAdded();

    }

    public void doLeftSectionResize(){
        this.leftSection.setWidth(vw(30));
        this.leftSection.setHeight(vh(100));
    }

    public void doRightSectionResize(){
        this.leftSection.setWidth(vw(60));
        this.leftSection.setHeight(vh(100));
    }

    @Override
    protected void init() {
        super.init();
        // 大小重置
        doLeftSectionResize();
        doRightSectionResize();
        // 布局
        gridLayout.clear();
        gridLayout.addChild(leftSection,0, 0, gridLayout.defaultCellSetting().alignHorizontallyLeft());
        gridLayout.addChild(rightSection, 0, 1, gridLayout.defaultCellSetting().alignHorizontallyRight());
        // 添加，调整
        gridLayout.arrangeElements();
    }

    /**左边栏目显示*/
    protected abstract FactionSection addEntityFactionSection();

    /**右边栏目显示*/
    protected abstract FactionSection addDetailedFactionSection();

    public int vw(float percent){
        return (int) (this.width * percent);
    }

    public int vh(float percent){
        return (int) (this.height * percent);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {}

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.leftSection.render(graphics, mouseX, mouseY, partialTicks);
        this.rightSection.render(graphics, mouseX, mouseY, partialTicks);
    }

    /**条件性渲染物品栏*/
    @Override
    protected void renderSlots(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // 获取开始渲染位置
        int left = this.leftPos;
        int top = this.topPos;

        final List<Slot> playerInventorySlots = menu.playerInventorySlots();

        // 渲染
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        poseStack.translate((float) left, (float) top, 0.0F);

        // 设置正在悬浮的格子为空
        this.hoveredSlot = null;

        for (int i = 0; i < this.menu.slots.size(); ++i) {
            Slot slot = this.getMenu().slots.get(i);
            if(!isShowPlayerInventory && playerInventorySlots.contains(slot)){
                return;
            }
            // 渲染每个格子
            if (slot.isActive()) {
                this.renderSingleSlot(graphics, slot);
            }

            // 设置正在悬浮的格子
            if (this.isHovering(slot, (double) mouseX, (double) mouseY) && slot.isActive()) {
                this.hoveredSlot = slot;
                if (this.hoveredSlot.isHighlightable()) {
                    this.renderSingleSlotHighlight(graphics, slot.x, slot.y, 0, getSlotColor(i));
                }
            }
        }

        poseStack.popPose();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    /**开关玩家背包栏目*/
    public void setShowPlayerInventory(boolean showPlayerInventory) {
        this.isShowPlayerInventory = showPlayerInventory;
    }
}
