package teamHTBP.vidaReforged.client.screen.screens.factions;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import teamHTBP.vidaReforged.client.screen.screens.common.VidaContainerScreen;
import teamHTBP.vidaReforged.client.screen.screens.wandCrafting.VidaFragmentController;
import teamHTBP.vidaReforged.core.common.ui.layouts.GridLayoutMutation;
import teamHTBP.vidaReforged.server.menu.AbstractFactionMenu;

import java.util.List;

public abstract class FactionBasedScreen<T extends AbstractFactionMenu> extends VidaContainerScreen<T> {
    private boolean isShowPlayerInventory = false;
    private GridLayoutMutation gridLayout;
    private GridLayoutMutation leftGridLayout;

    private FactionBasedSection leftEntitySection;
    private FactionBasedSection leftExperienceSection;
    private FactionBasedSection leftOtherSection;
    private FactionBasedSection rightSection;
    private ScreenRectangle leftSectionRect;
    private VidaFragmentController controller;

    protected FactionBasedScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public void added() {
        super.added();
        this.leftExperienceSection = this.addExperienceFactionSection();
        this.leftOtherSection = this.addOtherFactionSection();
        this.leftEntitySection = this.addEntityFactionSection();
        this.rightSection = this.addDetailedFactionSection();
        this.gridLayout = new GridLayoutMutation();
        this.leftGridLayout = new GridLayoutMutation();
        this.leftEntitySection.onAdded();
    }

    public void doLeftSectionResize(){
        this.leftEntitySection.setWidth(vw(35));
        this.leftEntitySection.setHeight(vh(40));

        this.leftExperienceSection.setWidth(vw(35));
        this.leftExperienceSection.setHeight(vh(30));

        this.leftOtherSection.setWidth(vw(35));
        this.leftOtherSection.setHeight(vh(30));
    }

    public void doRightSectionResize(){
        this.rightSection.setWidth(vw(65));
        this.rightSection.setHeight(vh(100));
    }

    @Override
    protected void init() {
        super.init();
        // 大小重置
        doLeftSectionResize();
        doRightSectionResize();
        leftEntitySection.init();
        rightSection.init();
        // 布局
        leftGridLayout.clear();
        gridLayout.clear();
        leftGridLayout.addChild(leftEntitySection,0, 0, gridLayout.defaultCellSetting().alignHorizontallyLeft());
        leftGridLayout.addChild(leftExperienceSection,1, 0, gridLayout.defaultCellSetting().alignHorizontallyLeft());
        leftGridLayout.addChild(leftOtherSection,2, 0, gridLayout.defaultCellSetting().alignHorizontallyLeft());
        gridLayout.addChild(leftGridLayout,0, 0, gridLayout.defaultCellSetting());
        gridLayout.addChild(rightSection, 0, 1, gridLayout.defaultCellSetting().alignHorizontallyRight());
        // 添加，调整
        leftGridLayout.arrangeElements();
        gridLayout.arrangeElements();
    }

    /**左边栏目显示*/
    protected abstract FactionBasedSection addEntityFactionSection();
    protected abstract FactionBasedSection addExperienceFactionSection();
    protected abstract FactionBasedSection addOtherFactionSection();
    /**右边栏目显示*/
    protected abstract FactionBasedSection addDetailedFactionSection();

    public int vw(float percent){
        return (int) (this.width * percent / 100.0);
    }

    public int vh(float percent){
        return (int) (this.height * percent / 100.0);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {}

    protected void renderBackground(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {}

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
        this.renderBackground(graphics, partialTicks, mouseX, mouseY);
        this.leftEntitySection.render(graphics, mouseX, mouseY, partialTicks);
        this.leftOtherSection.render(graphics, mouseX, mouseY, partialTicks);
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
        poseStack.pushPose();
        poseStack.translate((float) left, (float) top, 0.0F);

        // 设置正在悬浮的格子为空
        this.hoveredSlot = null;

        for (int i = 0; i < this.menu.slots.size(); ++i) {
            Slot slot = this.getMenu().slots.get(i);
            if(!isShowPlayerInventory && playerInventorySlots.contains(slot)){
                continue;
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
        poseStack.popPose();

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int key) {
        if(isShowPlayerInventory){
            return super.mouseClicked(mouseX, mouseY, key);
        }
        return mouseClickedListener(mouseX, mouseY, key);
    }

    /**开关玩家背包栏目*/
    public void setShowPlayerInventory(boolean showPlayerInventory) {
        this.isShowPlayerInventory = showPlayerInventory;
    }
}
