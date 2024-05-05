package teamHTBP.vidaReforged.client.model.armors.chestplate;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.model.armors.AbstractVidaArmorModel;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.client.events.registries.LayerRegistryHandler.registerLayer;

public class VidaBasedChestPlate extends AbstractVidaArmorModel {

    public static final ModelLayerLocation APPRENTICE_LAYER_LOCATION = registerLayer(new ResourceLocation(MOD_ID, "apprentice_chestplate"), "main");


    public VidaBasedChestPlate(ModelPart root) {
        super(root);
        this.biped_body = getPartOrEmpty(root,"bipedBody");
        this.biped_left_arm = getPartOrEmpty(root,"bipedLeftArm");
        this.biped_right_arm = getPartOrEmpty(root, "bipedRightArm");
    }
}
