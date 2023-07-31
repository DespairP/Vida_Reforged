package teamHTBP.vidaReforged.client.model.armors.head;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.model.armors.AbstractVidaArmorModel;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaBasedHelmet extends AbstractVidaArmorModel {
    public static final ModelLayerLocation APPRENTICE_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MOD_ID, "apprentice_helmet"), "main");

    public VidaBasedHelmet(ModelPart root) {
        super(root);
        this.biped_head = getPartOrEmpty(root, "bipedHead");
        this.biped_headwear = getPartOrEmpty(root, "bipedHeadwear");
    }
}