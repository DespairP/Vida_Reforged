package teamHTBP.vidaReforged.client.screen.screens.common;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.joml.Vector2i;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.screen.components.common.SherdResearchContainer;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.menu.SherdResearchTableMenu;
import teamHTBP.vidaReforged.server.menu.slots.FobiddenSlot;
import teamHTBP.vidaReforged.server.menu.slots.SherdSlot;

import java.util.ArrayList;
import java.util.List;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class SherdResearchScreen extends VidaContainerScreen<SherdResearchTableMenu>{
    public static final ResourceLocation LOCATION = new ResourceLocation(MOD_ID, "textures/gui/sherd_research.png");
    public static final TextureSection SHERD_INVENTORY_SECTION = new TextureSection(LOCATION, 16, 121, 176, 90, 256, 256);
    public static final TextureSection SHERD_UNLOCK_SECTION = new TextureSection(LOCATION, 0, 0, 28, 32, 256, 256);
    public static final TextureSection SHERD_SLOT_SECTION = new TextureSection(LOCATION, 0, 32, 28, 32, 256, 256);
    protected SherdResearchContainer researchSection;
    private Vector2i researchSectionPos = new Vector2i(0);
    private FloatRange alpha = new FloatRange(0, 0, 0.6F);

    public SherdResearchScreen(SherdResearchTableMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - SHERD_INVENTORY_SECTION.w()) / 2;
        this.topPos = this.height - SHERD_INVENTORY_SECTION.h();
        this.researchSectionPos = new Vector2i((this.width - 120) / 2, this.topPos + menu.getUnlockSlot().y - 6);
        this.researchSection = new SherdResearchContainer(researchSectionPos.x(), researchSectionPos.y(), 120, 120, Component.literal(""), this.menu.getSherdSlot());
    }

    @Override
    public void added() {
        super.added();
    }

    @Override
    protected void renderBg(GuiGraphics p_283065_, float p_97788_, int p_97789_, int p_97790_) {}

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        this.researchSection.render(graphics, mouseX, mouseY, partialTicks);
        this.renderSherdBackground(graphics, mouseX, mouseY, partialTicks);
        VidaGuiHelper.blitWithTexture(graphics, getGuiLeft(), getGuiTop() - 8, 0, SHERD_INVENTORY_SECTION);
        VidaGuiHelper.blitWithTexture(graphics, getGuiLeft() + menu.getUnlockSlot().x - 6, getGuiTop() + menu.getUnlockSlot().y - 8, 0, SHERD_UNLOCK_SECTION);
        VidaGuiHelper.blitWithTexture(graphics, getGuiLeft() + menu.getSherdSlot().x - 6, getGuiTop() + menu.getSherdSlot().y - 8, 0, SHERD_SLOT_SECTION);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    public void renderSherdBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        Slot slot = this.menu.getSherdSlot();
        if(slot == null || !slot.hasItem()){
            alpha.set(0F);
            return;
        }
        if(researchSection.isHovered()){
            alpha.decrease(0.001F);
        } else{
            alpha.increase(0.001F);
        }
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(8, 8, 8);
        poseStack.scale(4, 4, 4);
        poseStack.translate(-8, -8, -8);

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1,1, 1, alpha.get());
        graphics.renderFakeItem(slot.getItem(), (researchSectionPos.x() + (120 - 16) / 2)/ 4, (researchSectionPos.y() +(120 - 16) / 2) / 4);
        graphics.flush();
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    @Override
    protected void containerTick() {
        this.researchSection.tick();
    }

    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> children = new ArrayList<>();
        children.add(researchSection);
        return children;
    }
}
