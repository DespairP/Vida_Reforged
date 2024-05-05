package teamHTBP.vidaReforged.client.model.armors.leggings;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.model.armors.AbstractVidaArmorModel;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.client.events.registries.LayerRegistryHandler.registerLayer;

public class VidaBasedLeggings  extends AbstractVidaArmorModel {
    public static final ModelLayerLocation APPRENTICE_LAYER_LOCATION = registerLayer(new ResourceLocation(MOD_ID, "apprentice_leggings"), "main");

    public VidaBasedLeggings(ModelPart root) {
        super(root);
        this.biped_right_leg = getPartOrEmpty(root,"bipedRightLeg");
        this.biped_left_leg = getPartOrEmpty(root,"bipedLeftLeg");
    }
}