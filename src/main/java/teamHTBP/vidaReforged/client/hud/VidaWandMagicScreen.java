package teamHTBP.vidaReforged.client.hud;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.screen.components.common.ImageMutableWidget;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.helper.VidaInventoryHelper;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.providers.VidaMagicManager;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**渲染持有魔法*/
public class VidaWandMagicScreen extends AbstractVidaHUDScreen implements IVidaScreen {
    ItemStack currentRenderedItem = ItemStack.EMPTY;
    int currentMagicIndex = -1;
    /**现在物品拥有的魔法*/
    LinkedList<ResourceLocation> itemMagics = new LinkedList<>();
    /**元素列表偏移值X*/
    SecondOrderDynamics offsetX = new SecondOrderDynamics(0.5f, 1, 1, new Vector3f(0, 0, 0));
    /**法术列表偏移值Y*/
    SecondOrderDynamics offsetY = new SecondOrderDynamics(0.5f, 1, 1, new Vector3f(0, 0, 0));
    /**法术列表*/
    LinkedList<VidaMagicSlot> availableRenderMagics = new LinkedList<>();
    /**法术显示的大小*/
    public final static int SLOT_WIDTH = 100;
    public final static int SLOT_HEIGHT = 30;
    /***/
    public VidaElement currentElement;
    LinkedList<VidaElementSlot> itemElements = new LinkedList<>();
    List<VidaElement> elements = ImmutableList.of(VidaElement.EMPTY, VidaElement.GOLD, VidaElement.WOOD, VidaElement.AQUA, VidaElement.FIRE, VidaElement.EARTH);


    public VidaWandMagicScreen(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
        itemElements.addAll(List.of(
                new VidaElementSlot(0, 0, 32, 32, Component.empty(), VidaElement.EMPTY),
                new VidaElementSlot(40, 0, 32, 32, Component.empty(), VidaElement.GOLD),
                new VidaElementSlot(80, 0, 32, 32, Component.empty(), VidaElement.WOOD),
                new VidaElementSlot(120, 0, 32, 32, Component.empty(), VidaElement.AQUA),
                new VidaElementSlot(160, 0, 32, 32, Component.empty(), VidaElement.FIRE),
                new VidaElementSlot(200, 0, 32, 32, Component.empty(), VidaElement.EARTH)
        ));
    }

    /**刷新*/
    public void reset(ItemStack stack){
        this.currentRenderedItem = stack;
        this.itemMagics.clear();
        this.itemMagics.add(VidaMagic.MAGIC_UNKNOWN);

        if(!this.currentRenderedItem.isEmpty()){
            currentRenderedItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER).ifPresent(
                    cap -> {
                        this.currentMagicIndex = cap.getCurrentMagicIndex();
                        this.itemMagics.addAll(cap.getAvailableMagics());
                        this.buildMagicList();
                    }
            );
        }
    }

    /**判断是否改变*/
    public boolean validateChanged(ItemStack holder){
        AtomicBoolean isSame = new AtomicBoolean(true);
        holder.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER).ifPresent(
                cap -> {
                    List<ResourceLocation> magicIds = new LinkedList<>();
                    magicIds.add(VidaMagic.MAGIC_UNKNOWN);
                    magicIds.addAll(cap.getAvailableMagics());
                    if(itemMagics.size() != magicIds.size()){
                        isSame.set(false);
                        return;
                    }

                    for(int index = 0; index < cap.getAvailableMagics().size() + 1; index++){
                        if(!itemMagics.get(index).equals(magicIds.get(index))){
                            isSame.set(false);
                            return;
                        }
                    }
                }
        );

        return isSame.get();
    }

    public void buildMagicList(){
        this.availableRenderMagics.clear();
        for(int i = 0; i < this.itemMagics.size(); i++){
            ResourceLocation magicId = this.itemMagics.get(i);
            VidaMagicSlot slot = new VidaMagicSlot(0, i * SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT, magicId, i);
            availableRenderMagics.add(slot);
        }
    }

    public void refreshInfo(){
        currentRenderedItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER).ifPresent(
                cap -> {
                    this.currentMagicIndex = cap.getCurrentMagicIndex();
                    this.currentElement = cap.getCurrentElementOverride();
                }
        );
}

    @Override
    public void render(GuiGraphics graphics, float partialTicks) {
        ItemStack holdInItem = VidaInventoryHelper.getHandInItemByClient(InteractionHand.MAIN_HAND);
        if(!holdInItem.is(item -> VidaItemLoader.VIDA_WAND.get().equals(item.get()))){
            reset(ItemStack.EMPTY);
            return;
        }

        // changed to
        if(!validateChanged(holdInItem)){
            reset(holdInItem);
        }

        this.currentRenderedItem = holdInItem;
        refreshInfo();

        renderMagic(graphics, partialTicks);
        renderElement(graphics, partialTicks);
    }

    public void renderMagic(GuiGraphics graphics, float partialTicks){
        //
        int bottomLeftX = 0;
        int bottomLeftY = mc.getWindow().getGuiScaledHeight();
        int paddingLeft = 8;
        int paddingBottom = 32;

        PoseStack poseStack = graphics.pose();

        Vector3f vec = offsetY.update(mc.getDeltaFrameTime() * 0.4f, new Vector3f(0, (currentMagicIndex + 1) * SLOT_HEIGHT, 0), null);

        RenderSystem.enableBlend();
        VidaGuiHelper.renderScissor(bottomLeftX, bottomLeftY - (int)(paddingBottom * 1.2), SLOT_WIDTH, (int)(SLOT_HEIGHT * 1.2));


        poseStack.pushPose();
        poseStack.translate(bottomLeftX + paddingLeft, bottomLeftY - paddingBottom, 0);
        poseStack.translate(0, -vec.y, 0);
        poseStack.pushPose();

        availableRenderMagics.forEach(slot -> slot.renderWidget(graphics, 0, 0, partialTicks));

        poseStack.popPose();
        poseStack.popPose();
        RenderSystem.disableScissor();
        RenderSystem.disableBlend();
    }


    public void renderElement(GuiGraphics graphics, float partialTicks){
        //
        int bottomLeftX = 0;
        int bottomLeftY = mc.getWindow().getGuiScaledHeight();
        int paddingLeft = 32;
        int paddingBottom = 20;

        //获取缩放因子
        final float factor = 1.0f / 0.5f;

        PoseStack poseStack = graphics.pose();

        Vector3f vec = offsetX.update(mc.getDeltaFrameTime() * 0.4f, new Vector3f(0, elements.indexOf(currentElement) * 40, 0), null);

        RenderSystem.enableBlend();
        VidaGuiHelper.renderScissor(bottomLeftX + paddingLeft, bottomLeftY - paddingBottom, 20, 20);


        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);

        poseStack.translate((bottomLeftX + paddingLeft) * factor, (bottomLeftY - paddingBottom) * factor, 0);
        poseStack.translate(-vec.y, 0, 0);
        poseStack.pushPose();

        itemElements.forEach(slot -> slot.renderWidget(graphics, 0, 0, partialTicks));

        poseStack.popPose();
        poseStack.popPose();
        RenderSystem.disableScissor();
        RenderSystem.disableBlend();
    }


    /**法术列表中的一项法术*/
    public static class VidaMagicSlot extends VidaWidget {
        private int index = 0;
        private VidaMagic magic;
        private static final TextureSection NON_EXIST = new TextureSection(new ResourceLocation(VidaReforged.MOD_ID, "textures/icons/skills_002.png"), 0, 0, 24, 24, 384, 384);

        public VidaMagicSlot(int x, int y, int width, int height, ResourceLocation magic, int index) {
            super(x, y, width, height, Component.literal(magic.toString()));
            this.index = index;
            this.magic = VidaMagicManager.getMagicByMagicId(magic);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            if(magic == null){
                VidaGuiHelper.blitWithTexture(graphics, getX(), getY(), 0, NON_EXIST);
                return;
            }
            VidaGuiHelper.blitWithTexture(graphics, getX(), getY(), 0, magic.icon());
        }
    }


    /**元素*/
    public static class VidaElementSlot extends VidaWidget{
        ImageMutableWidget widget;

        public VidaElementSlot(int x, int y, int width, int height, Component component, VidaElement element) {
            super(x, y, width, height, component);
            this.widget = new ImageMutableWidget(x, y ,width, height, component, element.getIcon());
            this.widget.setRev(32);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            this.widget.renderWidget(graphics, mouseX, mouseY, partialTicks);
        }
    }
}
