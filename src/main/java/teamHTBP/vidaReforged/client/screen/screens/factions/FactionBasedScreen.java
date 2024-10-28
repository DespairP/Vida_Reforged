package teamHTBP.vidaReforged.client.screen.screens.factions;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import teamHTBP.vidaReforged.client.screen.screens.common.VidaContainerScreen;
import teamHTBP.vidaReforged.client.screen.screens.wandCrafting.VidaFragmentController;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.menu.AbstractFactionMenu;

import java.util.ArrayList;
import java.util.List;

public class FactionBasedScreen<T extends AbstractFactionMenu> extends VidaContainerScreen<T> {
    private boolean isShowPlayerInventory = false;
    private GridLayout gridLayout;
    private FactionSection leftSection;
    private VidaFragmentController controller;


    protected FactionBasedScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public void added() {
        super.added();
        this.addFactionSection();
    }

    @Override
    protected void init() {
        super.init();
        // resize
        this.leftSection.setWidth(vw(30));
        this.leftSection.setHeight(vh(30));
        // 添加
        this.gridLayout = new GridLayout();
        this.gridLayout.addChild(this.leftSection, 0, 0);
    }

    protected void addFactionSection(){
        this.leftSection = new FactionSection(0, 0, vw(30), vh(100));
    }

    public int vw(float percent){
        return (int) (this.width * percent);
    }

    public int vh(float percent){
        return (int) (this.height * percent);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {}


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
