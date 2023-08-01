package teamHTBP.vidaReforged.client.model.blockModel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class InjectTableModel extends Model {
    public static final ModelLayerLocation APPRENTICE_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MOD_ID, "inject_table"), "main");

    private final ModelPart all;

    public InjectTableModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.all = root.getChild("group");
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition group = partdefinition.addOrReplaceChild("group", CubeListBuilder.create().texOffs(4, 5).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(poseStack,vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
