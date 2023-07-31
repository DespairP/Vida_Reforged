package teamHTBP.vidaReforged.client.model.armors.head;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.model.armors.AbstractVidaArmorModel;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class BlackMetalHelmet extends AbstractVidaArmorModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MOD_ID, "black_metal_helmet"), "main");

    public BlackMetalHelmet(ModelPart root) {
        super(root);
        this.biped_head = getPartOrEmpty(root, "bipedHead");
        this.biped_headwear = getPartOrEmpty(root, "bipedHeadwear");
    }
}
