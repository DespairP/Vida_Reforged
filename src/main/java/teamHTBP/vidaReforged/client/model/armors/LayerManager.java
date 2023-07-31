package teamHTBP.vidaReforged.client.model.armors;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class LayerManager {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bipedHead = partdefinition.addOrReplaceChild("bipedHead", CubeListBuilder.create().texOffs(96, 112).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.51F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bipedHeadwear = partdefinition.addOrReplaceChild("bipedHeadwear", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.755F))
                .texOffs(64, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.755F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bipedBody = partdefinition.addOrReplaceChild("bipedBody", CubeListBuilder.create().texOffs(104, 112).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.3F))
                .texOffs(64, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 13.0F, 4.0F, new CubeDeformation(0.45F))
                .texOffs(96, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 13.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bipedRightArm = partdefinition.addOrReplaceChild("bipedRightArm", CubeListBuilder.create().texOffs(112, 112).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.29F))
                .texOffs(64, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.44F))
                .texOffs(64, 64).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.59F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition cube_r1 = bipedRightArm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(101, 46).addBox(-3.0F, -12.0F, -3.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-2.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition cube_r2 = bipedRightArm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(101, 46).addBox(-2.0F, -3.5F, -3.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 8.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition bipedRightArm_r1 = bipedRightArm.addOrReplaceChild("bipedRightArm_r1", CubeListBuilder.create().texOffs(80, 64).addBox(-3.75F, -1.25F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.74F))
                .texOffs(80, 48).addBox(-3.75F, -1.25F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.59F)), PartPose.offsetAndRotation(0.5F, -1.0F, 0.0F, 0.0F, 0.0F, -0.0262F));

        PartDefinition bipedLeftArm = partdefinition.addOrReplaceChild("bipedLeftArm", CubeListBuilder.create().texOffs(112, 112).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.29F)).mirror(false)
                .texOffs(64, 48).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.44F)).mirror(false)
                .texOffs(64, 64).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.59F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition cube_r3 = bipedLeftArm.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(101, 46).mirror().addBox(1.0F, -3.5F, -3.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition cube_r4 = bipedLeftArm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(101, 46).mirror().addBox(2.0F, -12.0F, -3.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(2.0F, 8.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition bipedLeftArm_r1 = bipedLeftArm.addOrReplaceChild("bipedLeftArm_r1", CubeListBuilder.create().texOffs(80, 64).mirror().addBox(-0.25F, -1.25F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.74F)).mirror(false)
                .texOffs(80, 48).mirror().addBox(-0.25F, -1.25F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.59F)).mirror(false), PartPose.offsetAndRotation(-0.5F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0262F));

        PartDefinition bipedRightLeg = partdefinition.addOrReplaceChild("bipedRightLeg", CubeListBuilder.create().texOffs(112, 112).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.29F))
                .texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.44F))
                .texOffs(16, 48).addBox(-2.25F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.59F))
                .texOffs(32, 48).addBox(-2.25F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.74F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition bipedLeftLeg = partdefinition.addOrReplaceChild("bipedLeftLeg", CubeListBuilder.create().texOffs(112, 112).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.29F)).mirror(false)
                .texOffs(0, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.44F)).mirror(false)
                .texOffs(16, 48).mirror().addBox(-1.75F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.59F)).mirror(false)
                .texOffs(32, 48).mirror().addBox(-1.75F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.74F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
