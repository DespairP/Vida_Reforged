package teamHTBP.vidaReforged.client.model.itemstackModel;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.events.registries.LayerRegistryHandler;
import teamHTBP.vidaReforged.client.model.itemModel.VidaWandModel;
import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.server.items.VidaWandEquipment;

import java.util.HashMap;
import java.util.Map;

import static teamHTBP.vidaReforged.client.model.itemModel.VidaWandModel.DEFAULT_TEXTURE;


public class VidaWandItemModel extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation VIDA_WAND_MODEL = new ResourceLocation(VidaReforged.MOD_ID, "textures/armor/vida_wand_model.png");
    private static final ResourceLocation VIDA_WAND_MODEL_ITEM = new ResourceLocation(VidaReforged.MOD_ID, "textures/item/vida_wand.png");


    public VidaWandItemModel() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (transformType == ItemDisplayContext.GUI || transformType == ItemDisplayContext.GROUND) {
            renderInGui(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
            return;
        }

        CompoundTag tag = stack.getOrCreateTag();
        ListTag equipmentsTag = (ListTag) tag.get("equipments");
        Map<Position, ItemStack> equipments = new HashMap<>(Map.of(Position.CORE, ItemStack.EMPTY, Position.TOP, ItemStack.EMPTY, Position.CENTER, ItemStack.EMPTY, Position.BOTTOM, ItemStack.EMPTY));

        if(equipmentsTag != null && !equipmentsTag.isEmpty()){
            for(int i = 0; i < equipmentsTag.size(); ++i) {
                CompoundTag compoundtag = equipmentsTag.getCompound(i);
                int j = compoundtag.getByte("Slot") & 255;
                equipments.put(Position.values()[j], ItemStack.of(compoundtag));
            }
        }

        VidaWandModel model = LayerRegistryHandler.getModelSupplier(VidaWandModel.LAYER_LOCATION, VidaWandModel.class).get();
        equipments.forEach(((position, posEquipment) -> {
            if(!posEquipment.isEmpty() && posEquipment.getItem() instanceof VidaWandEquipment equipment){
                ResourceLocation texture = equipment.getAttribute().getModelTexture();;
                VidaWandModel equipmentModel = LayerRegistryHandler.getModelSupplier(LayerRegistryHandler.getLayerByResourceLocation(equipment.getAttribute().getModelLayerLocation()), VidaWandModel.class).get();
                equipmentModel.renderPartToBuffer(position, poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(texture)), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                return;
            }

            model.renderPartToBuffer(position, poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(DEFAULT_TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        }));
    }

    protected void renderInGui(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        poseStack.pushPose();
    }
}
