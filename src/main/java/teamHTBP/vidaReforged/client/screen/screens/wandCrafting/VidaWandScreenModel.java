package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.renderer.RenderTypeHandler;
import teamHTBP.vidaReforged.client.events.registries.LayerRegistryHandler;
import teamHTBP.vidaReforged.client.model.itemModel.VidaWandModel;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaWandCraftingViewModel;
import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.server.items.VidaWandEquipment;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandEquipmentSlot;

import java.util.HashMap;
import java.util.Map;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

/**显示法杖模型*/
@OnlyIn(Dist.CLIENT)
public class VidaWandScreenModel extends VidaWidget {
    private static final ResourceLocation VIDA_WAND_MODEL = new ResourceLocation(MOD_ID, "textures/armor/vida_wand_model.png");
    /**旋转角度*/
    private int rotateY = 0;
    /**法杖渲染大小*/
    public static final int WAND_SIZE = 16;
    public static final int WAND_FACTOR = 4;

    public SecondOrderDynamics modelWandSize;
    public float targetFactor = 0;
    private Map<Position, VidaWandEquipmentSlot> equipmentSlots = new HashMap<>();

    public VidaWandScreenModel(int x, int y, int width, int height, float factor, Component component) {
        super(x, y, width, height, component);
        this.targetFactor = factor;
        this.modelWandSize = new SecondOrderDynamics(1F, 0.7f, 0, new Vector3f(0));
        VidaWandCraftingViewModel viewModel = new ViewModelProvider(requireParent()).get(VidaWandCraftingViewModel.class);
        equipmentSlots = viewModel.slots.getValue();
        viewModel.slots.observe(requireParent(), map -> {equipmentSlots = new HashMap<>(); equipmentSlots.putAll(map);});
    }


    public void resize(int width, int height){
        float newFactor = width * this.targetFactor / this.width;
        this.width = width;
        this.height = height;
        this.targetFactor = newFactor;
    }

    public void refresh(){
        this.modelWandSize.update(mc.getDeltaFrameTime() * 0.2F, new Vector3f(targetFactor), null);
    }


    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //
        refresh();
        // 渲染法杖模型
        renderModel(graphics, mouseX, mouseY);
    }


    /**渲染模型*/
    public void renderModel(GuiGraphics graphics, int mouseX, int mouseY){
        RenderSystem.enableBlend();
        // 3d光设置
        Lighting.setupForFlatItems();
        float factor = modelWandSize.currentPos().x;

        final PoseStack pPoseStack = graphics.pose();
        RenderSystem.setShaderTexture(0, VIDA_WAND_MODEL);

        pPoseStack.pushPose();
        pPoseStack.translate(16, WAND_SIZE * factor, 0);
        pPoseStack.translate(getX() + factor  * 4, getY(),30f);
        pPoseStack.scale(WAND_SIZE * factor, WAND_SIZE * factor, WAND_SIZE * factor);

        // 按X轴旋转
        pPoseStack.mulPose(Axis.XP.rotationDegrees(20));
        // 再按Y轴转95度
        pPoseStack.mulPose(Axis.YP.rotationDegrees(95 + rotateY));

        // 渲染模型
        VidaWandModel model = LayerRegistryHandler.getModelSupplier(VidaWandModel.LAYER_LOCATION, VidaWandModel.class).get();
        this.equipmentSlots.forEach(((position, slot) -> {
            int packedLight = 14 << 4 | 14 << 20;

            ItemStack slotItem = slot.getItem();
            if(!slotItem.isEmpty() && slotItem.getItem() instanceof VidaWandEquipment equipment){
                ResourceLocation texture = equipment.getAttribute().getModelTexture();;
                VidaWandModel equipmentModel = LayerRegistryHandler.getModelSupplier(LayerRegistryHandler.getLayerByResourceLocation(equipment.getAttribute().getModelLayerLocation()), VidaWandModel.class).get();
                equipmentModel.renderPartToBuffer(position, pPoseStack, graphics.bufferSource().getBuffer(RenderTypeHandler.ENTITY_GLOW_WAND.apply(texture)), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                return;
            }

            model.renderPartToBuffer(position, pPoseStack, graphics.bufferSource().getBuffer(RenderType.entityTranslucent(VIDA_WAND_MODEL)), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        }));

        // 还原光影光
        RenderSystem.setShaderColor(1, 1, 1, 1);
        pPoseStack.popPose();
        RenderSystem.disableScissor();
        RenderSystem.disableBlend();
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
        if (this.isValidClickButton(mouseButton) && isMouseOver(mouseX, mouseY)) {
            this.onDrag(mouseX, mouseY, dragX, dragY);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        rotateY = (int) ((rotateY + dragX) % 361);
    }


}
