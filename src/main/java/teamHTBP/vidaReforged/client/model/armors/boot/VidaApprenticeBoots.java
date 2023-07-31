package teamHTBP.vidaReforged.client.model.armors.boot;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.model.armors.AbstractVidaArmorModel;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaApprenticeBoots extends AbstractVidaArmorModel {
    public static final ModelLayerLocation APPRENTICE_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MOD_ID, "apprentice_boots"), "main");

    public VidaApprenticeBoots(ModelPart root) {
        super(root);
        this.biped_body_low = getPartOrEmpty(root,"bipedBodyLow");
    }
}
