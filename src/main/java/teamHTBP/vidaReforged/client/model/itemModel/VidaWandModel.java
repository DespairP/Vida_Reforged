package teamHTBP.vidaReforged.client.model.itemModel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.common.item.Position;

import java.util.HashMap;
import java.util.Map;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.client.events.registries.LayerRegistryHandler.registerLayer;

@OnlyIn(Dist.CLIENT)
public class VidaWandModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = registerLayer(new ResourceLocation(MOD_ID, "vida_wand_model"), "main");
    public static final ModelLayerLocation TEST_LAYER_LOCATION = registerLayer(new ResourceLocation(MOD_ID, "vida_wand_model_test"), "main");
    public static final ModelLayerLocation TEST_CORE_LAYER_LOCATION = registerLayer(new ResourceLocation(MOD_ID, "vida_wand_model_core_test"), "main");
    public static final ModelLayerLocation STYLE_001_TOP_LAYER_LOCATION = registerLayer(new ResourceLocation(MOD_ID, "wand_style_001_model_top"), "main");
    public static final ModelLayerLocation STYLE_001_CORE_LAYER_LOCATION = registerLayer(new ResourceLocation(MOD_ID, "wand_style_001_model_core"), "main");
    public static final ModelLayerLocation STYLE_001_CENTER_LAYER_LOCATION = registerLayer(new ResourceLocation(MOD_ID, "wand_style_001_model_center"), "main");
    public static final ModelLayerLocation STYLE_001_BOTTOM_LAYER_LOCATION = registerLayer(new ResourceLocation(MOD_ID, "wand_style_001_model_bottom"), "main");
    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(MOD_ID, "textures/armor/vida_wand_model.png");
    private final ModelPart bone;
    private final ModelPart top;
    private final ModelPart center;
    private final ModelPart bottom;
    private final ModelPart core;

    public VidaWandModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.bone = root.getChild("bone");
        this.center = bone.getChild("center");
        this.bottom = bone.getChild("bottom");
        this.core = bone.getChild("core");
        this.top = bone.getChild("top");
    }


    public static MeshDefinition createBodyLayer(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition bottom = bone.addOrReplaceChild("bottom", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.875F, -1.35F, -0.1309F, 0.0F, 0.0F));
        PartDefinition top = bone.addOrReplaceChild("top", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.0F, -0.1F, -0.2618F, 0.0F, 0.0F));
        PartDefinition core = bone.addOrReplaceChild("core", CubeListBuilder.create(), PartPose.offset(0.0F, -8.65F, -0.25F));
        PartDefinition center = bone.addOrReplaceChild("center", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));

        return meshdefinition;
    }

    public static LayerDefinition createWandBaseLayer(){
        MeshDefinition meshdefinition = createBodyLayer();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition center = bone.addOrReplaceChild("center", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));
        PartDefinition cube_r1 = center.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 9).addBox(-1.0F, 0.1265F, -0.0463F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0004F)), PartPose.offsetAndRotation(0.0F, -12.9F, -3.075F, 0.4581F, 0.0F, 0.0F));
        PartDefinition cube_r2 = center.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -18.0F, 0.0F, -0.3665F, 0.0F, 0.0F));

        PartDefinition bottom = bone.addOrReplaceChild("bottom", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.875F, -1.35F, -0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r3 = bottom.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -0.075F, -0.125F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0005F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0785F, 0.0F, 0.0F));
        PartDefinition cube_r4 = bottom.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 1.35F, -0.7854F, 0.0F, 0.0F));

        PartDefinition top = bone.addOrReplaceChild("top", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.0F, -0.1F, -0.2618F, 0.0F, 0.0F));
        PartDefinition cube_r5 = top.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(16, 6).addBox(0.0F, -2.8706F, 0.684F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 3.0F, 0.48F, 0.0F, 0.0F));
        PartDefinition cube_r6 = top.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(16, 6).addBox(0.0F, -3.0F, -1.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 3.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition cube_r7 = top.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(16, 5).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 3.0F, -0.7418F, 0.0F, 0.0F));
        PartDefinition cube_r8 = top.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(16, 19).addBox(-1.0F, 1.475F, 4.325F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.025F, 0.0F, 2.7053F, 0.0F, 0.0F));
        PartDefinition cube_r9 = top.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(16, 11).addBox(-1.0F, -4.775F, 5.725F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, 0.025F, 0.0F, 1.3526F, 0.0F, 0.0F));
        PartDefinition cube_r10 = top.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -6.625F, 2.1F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.025F, 0.0F, 0.1745F, 0.0F, 0.0F));
        PartDefinition cube_r11 = top.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(8, 8).addBox(-1.0F, -1.275F, -0.6F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9199F, 0.0F, 0.0F));
        PartDefinition cube_r12 = top.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 23).addBox(-1.5F, -1.375F, 0.35F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, 0.025F, 0.0F, -1.4399F, 0.0F, 0.0F));
        PartDefinition cube_r13 = top.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(8, 8).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, 0.025F, 0.0F, -1.0036F, 0.0F, 0.0F));

        PartDefinition core = bone.addOrReplaceChild("core", CubeListBuilder.create(), PartPose.offset(0.0F, -8.65F, -0.25F));
        PartDefinition cube_r14 = core.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5061F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }


    public static LayerDefinition createTestLayer(){
        MeshDefinition meshdefinition = createBodyLayer();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition top = bone.addOrReplaceChild("top", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.0F, -0.1F, -0.2618F, 0.0F, 0.0F));
        PartDefinition cube_r1 = top.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(25, 23).addBox(-0.002F, -4.75F, -1.75F, 0.001F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.7453F, 0.0F, 0.0F));
        PartDefinition cube_r2 = top.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(25, 23).addBox(-0.001F, -5.0F, -2.0F, 0.001F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.0036F, 0.0F, 0.0F));
        PartDefinition cube_r3 = top.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -8.5F, -7.525F, 2.0F, 10.0F, 2.0F, new CubeDeformation(-0.002F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3054F, 0.0F, 0.0F));
        PartDefinition cube_r4 = top.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.25F, -6.875F, 2.0F, 10.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6545F, 0.0F, 0.0F));
        PartDefinition cube_r5 = top.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.25F, -1.875F, 2.0F, 6.0F, 2.0F, new CubeDeformation(-0.002F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0036F, 0.0F, 0.0F));
        PartDefinition cube_r6 = top.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.25F, -0.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.003F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.9599F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }


    public static LayerDefinition createTestCoreLayer(){
        MeshDefinition meshdefinition = createBodyLayer();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition core = bone.addOrReplaceChild("core", CubeListBuilder.create(), PartPose.offset(0.0F, -8.65F, -0.25F));
        PartDefinition cube_r1 = core.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5061F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public static LayerDefinition createStyle_001_TopLayer() {
        MeshDefinition meshdefinition = createBodyLayer();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition top = bone.addOrReplaceChild("top", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.0F, -0.1F, -0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r1 = top.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(25, 23).addBox(-0.002F, -4.75F, -1.75F, 0.001F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.7453F, 0.0F, 0.0F));
        PartDefinition cube_r2 = top.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(25, 23).addBox(-0.001F, -5.0F, -2.0F, 0.001F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.0036F, 0.0F, 0.0F));
        PartDefinition cube_r3 = top.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -8.5F, -7.525F, 2.0F, 10.0F, 2.0F, new CubeDeformation(-0.002F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3054F, 0.0F, 0.0F));
        PartDefinition cube_r4 = top.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.25F, -6.875F, 2.0F, 10.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6545F, 0.0F, 0.0F));
        PartDefinition cube_r5 = top.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.25F, -1.875F, 2.0F, 6.0F, 2.0F, new CubeDeformation(-0.002F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0036F, 0.0F, 0.0F));

        PartDefinition cube_r6 = top.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.25F, -0.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.003F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.9599F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public static LayerDefinition createStyle_001_CoreLayer() {
        MeshDefinition meshdefinition = createBodyLayer();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition core = bone.addOrReplaceChild("core", CubeListBuilder.create(), PartPose.offset(0.0F, -8.65F, -0.25F));

        PartDefinition cube_r1 = core.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(4, 17).addBox(-5.0F, 0.0F, -3.5F, 10.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7418F, 0.0F, 0.0F));

        PartDefinition cube_r2 = core.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -0.9F, -0.75F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2443F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public static LayerDefinition createStyle_001_CenterLayer() {
        MeshDefinition meshdefinition = createBodyLayer();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition center = bone.addOrReplaceChild("center", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));

        PartDefinition cube_r1 = center.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 9).addBox(-1.0F, -2.4485F, 0.8287F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0004F)), PartPose.offsetAndRotation(0.0F, -12.9F, -3.075F, 0.24F, 0.0F, 0.0F));

        PartDefinition cube_r2 = center.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -18.0F, 0.0F, -0.8465F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    public static LayerDefinition createStyle_001_BottomLayer() {
        MeshDefinition meshdefinition = createBodyLayer();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition bottom = bone.addOrReplaceChild("bottom", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.875F, -1.35F, -0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r1 = bottom.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -0.075F, -0.125F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0005F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0785F, 0.0F, 0.0F));

        PartDefinition cube_r2 = bottom.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 1.35F, -0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }


    public void renderPartToBuffer(Position position, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        switch (position){
            case TOP -> {
                this.top.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case CORE -> {
                this.core.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case BOTTOM -> {
                this.bottom.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case CENTER -> {
                this.center.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
    }


}
