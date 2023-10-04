package teamHTBP.vidaReforged.client.screen.screens.common;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Vector2i;
import teamHTBP.vidaReforged.client.screen.components.prism.PrismButton;
import teamHTBP.vidaReforged.client.screen.components.prism.PrismResultButton;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.blockEntities.PrismBlockEntity;
import teamHTBP.vidaReforged.server.menu.PrismMenu;
import teamHTBP.vidaReforged.server.packets.PrismPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class PrismScreen extends AbstractContainerScreen<PrismMenu> {

    private final Inventory playerInventory;
    PrismButton button$1;
    PrismButton button$2;

    PrismResultButton resultButton;
    ResourceLocation location = new ResourceLocation(MOD_ID, "textures/gui/prism_table_gui.png");
    TextureSection mirrorSection = new TextureSection(location,48,0,48,48);
    TextureSection inventorySection = new TextureSection(location,0,54,176,90);
    TextureSection inventorySlotSection = new TextureSection(location,125,13,22,22);
    Vector2i mirrorPos = new Vector2i(0, 0);

    ResourceLocation fireLocation = new ResourceLocation(MOD_ID, "textures/gui/prism_table_gui_fire.png");

    int index = 0;

    int maxIndex = 60;

    TextureSection fireSection = new TextureSection(fireLocation,0,0,180 / 20, 71 / 3);

    PrismBlockEntity entity;

    public PrismScreen(PrismMenu menu, Inventory playerInventory,Component component) {
        super(menu, playerInventory, Component.translatable("prism"));
        this.playerInventory = playerInventory;
    }

    @Override
    protected void init() {
        super.init();
        this.mirrorPos = new Vector2i(
                (this.width - mirrorSection.w()) / 2,
                (this.height - mirrorSection.h()) / 3
        );
        this.button$1 = new PrismButton(30, 270, new PrismButton.Point(mirrorPos.x + 35, mirrorPos.y + 35), 1);
        this.button$2 = new PrismButton(30, 140, new PrismButton.Point(mirrorPos.x + 35, mirrorPos.y + 35), 2);
        this.resultButton = new PrismResultButton(() ->{
            VidaPacketManager.sendToServer(new PrismPacket(
                    this.menu.getPos(),
                    this.button$1.getDegree(),
                    this.button$2.getDegree()
            ));
        }, mirrorPos.x + mirrorSection.w() / 2 - 8,mirrorPos.y + mirrorSection.h() + 20);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        if(this.resultButton.isHovered()){
            graphics.renderTooltip(this.font, Component.translatable("tootip.vida_reforged.prism_screen_notice"), x, y);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.leftPos = (this.width - 176) / 2;
        this.topPos = (int)((this.height - 54) * 2.6f / 3);
        this.entity = (PrismBlockEntity) this.playerInventory.player.getCommandSenderWorld().getBlockEntity(this.menu.getPos());
        this.renderBackground(graphics);
        this.renderMirror(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderFire(graphics, mouseX, mouseY, partialTicks);
        this.button$1.render(graphics, mouseX, mouseY, partialTicks);
        this.button$2.render(graphics, mouseX, mouseY, partialTicks);
        this.resultButton.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics,mouseX,mouseY);
        this.renderLabels(graphics, mouseX, mouseY);
    }

    public void renderMirror(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        graphics.blit(
                mirrorSection.location(),
                mirrorPos.x, mirrorPos.y, 0,
                mirrorSection.minU(), mirrorSection.minV(),
                mirrorSection.w(), mirrorSection.w(),
                256, 256
        );

        graphics.blit(
                inventorySection.location(),
                this.leftPos - 8, this.topPos - 8, 0,
                inventorySection.minU(), inventorySection.minV(),
                inventorySection.w(), inventorySection.h(),
                256, 256
        );

        graphics.blit(
                inventorySlotSection.location(),
                this.leftPos - 3 + 110, this.topPos - 3 - 30, 0,
                125, 13,
                inventorySlotSection.w(), inventorySlotSection.h(),
                256, 256
        );

        graphics.blit(
                inventorySlotSection.location(),
                this.leftPos - 3 + 50, this.topPos - 3 - 30, 0,
                125, 13,
                inventorySlotSection.w(), inventorySlotSection.h(),
                256, 256
        );

        poseStack.popPose();
    }

    public void renderFire(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if(!entity.isGenerateFire()){
            return;
        }

        this.index = (this.index + 1) % 60;
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();

        RenderSystem.enableBlend();
        int offset1 = (int) Math.abs(this.button$1.getDegree() - entity.getRotateRad0());
        int offset2 = (int) Math.abs(this.button$2.getDegree() - entity.getRotateRad1());

        RenderSystem.setShaderColor(
                1,
                1 - ((float) Math.sqrt(offset1 * offset1 + offset2 * offset2)) / 40.0f,
                1,
                Math.max(0, (30 - offset2) / 30.0f)
        );

        graphics.blit(
                fireSection.location(),
                mirrorPos.x + 20, mirrorPos.y + 14, 0,
                fireSection.minU() + (fireSection.w() * (index % 20)), fireSection.minU() + ((fireSection.h() + 1) * (int)(index / 20)),
                fireSection.w(), fireSection.h(),
                180, 71
        );

        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
        boolean dragged = this.getFocused() != null && this.isDragging() && mouseButton == 0 ? this.getFocused().mouseDragged(mouseX, mouseY, mouseButton, dragX, dragY) : false;
        return dragged || super.mouseDragged(mouseX, mouseY, mouseButton, dragX, dragY);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> listeners = (List<GuiEventListener>) super.children();
        listeners.add((GuiEventListener) button$1);
        listeners.add((GuiEventListener) button$2);
        listeners.add((GuiEventListener) resultButton);
        return listeners;
    }
}
