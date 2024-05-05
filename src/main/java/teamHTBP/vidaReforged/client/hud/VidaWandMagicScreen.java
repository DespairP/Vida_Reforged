package teamHTBP.vidaReforged.client.hud;

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
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.helper.VidaInventoryHelper;
import teamHTBP.vidaReforged.server.capabilities.VidaMagicToolCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.providers.VidaMagicManager;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**渲染持有魔法*/
public class VidaWandMagicScreen extends AbstractVidaHUDScreen implements IVidaScreen {
    ItemStack currentRenderedItem = ItemStack.EMPTY;
    int currentMagicIndex = -1;
    /**现在物品拥有的魔法*/
    LinkedList<ResourceLocation> magics = new LinkedList<>();
    /***/
    SecondOrderDynamics offsetY = new SecondOrderDynamics(0.5f, 1, 0, new Vector3f(0, 0, 0));
    /***/
    LinkedList<VidaMagicSlot> slots = new LinkedList<>();
    public static int SLOT_WIDTH = 100;
    public static int SLOT_HEIGHT = 30;

    public VidaWandMagicScreen(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
    }

    public void reset(ItemStack stack){
        this.currentRenderedItem = stack;
        this.magics.clear();
        this.magics.add(VidaMagicToolCapability.UNCHOSEN_MAGIC);

        if(!this.currentRenderedItem.isEmpty()){
            currentRenderedItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER).ifPresent(
                    cap -> {
                        this.currentMagicIndex = cap.getCurrentMagicIndex();
                        this.magics.addAll(cap.getAvailableMagics());
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
                    magicIds.add(VidaMagicToolCapability.UNCHOSEN_MAGIC);
                    magicIds.addAll(cap.getAvailableMagics());
                    if(magics.size() != magicIds.size()){
                        isSame.set(false);
                        return;
                    }

                    for(int index = 0; index < cap.getAvailableMagics().size() + 1; index++){
                        if(!magics.get(index).equals(magicIds.get(index))){
                            isSame.set(false);
                            return;
                        }
                    }
                }
        );

        return isSame.get();
    }

    public void buildMagicList(){
        this.slots.clear();
        for(int i = 0; i < this.magics.size(); i++){
            ResourceLocation magicId = this.magics.get(i);
            VidaMagicSlot slot = new VidaMagicSlot(0, i * SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT, magicId, i);
            slots.add(slot);
        }
    }

    public void refreshInfo(){
        currentRenderedItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER).ifPresent(
                cap -> {
                    this.currentMagicIndex = cap.getCurrentMagicIndex();
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

        slots.forEach(slot -> slot.renderWidget(graphics, 0, 0, partialTicks));

        poseStack.popPose();
        poseStack.popPose();
        RenderSystem.disableScissor();
        RenderSystem.disableBlend();
    }


    public class VidaMagicSlot extends VidaWidget {
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

}
