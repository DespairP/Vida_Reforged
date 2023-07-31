package teamHTBP.vidaReforged.client.model.armors;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

import java.util.HashSet;
import java.util.Set;

public class AbstractVidaArmorModel extends HumanoidModel {
    public ModelPart biped_head;
    public ModelPart biped_headwear;
    public ModelPart biped_left_arm;
    public ModelPart biped_right_arm;
    public ModelPart biped_body;
    public ModelPart biped_body_low;
    public ModelPart biped_left_leg;
    public ModelPart biped_right_leg;


    public AbstractVidaArmorModel(ModelPart root) {
        super(root);
    }

    public ModelPart getPartOrEmpty(ModelPart mainPart,String key){
        try {
            return mainPart.getChild(key);
        }catch (Exception exception){
            return null;
        }
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        Set<ModelPart> partSet = new HashSet<>(
                ImmutableList.of(
                        biped_head,
                        biped_headwear
                )
        );
        partSet.remove(null);
        return partSet;
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        Set<ModelPart> partSet = new HashSet<>(
                ImmutableList.of(
                        biped_body,
                        biped_left_arm,
                        biped_right_arm,
                        biped_body_low,
                        biped_left_leg,
                        biped_right_leg
                )
        );
        partSet.remove(null);
        return partSet;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        renderToBufferIfExist(this.biped_head, this.head, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        renderToBufferIfExist(this.biped_headwear, this.head, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        renderToBufferIfExist(this.biped_body, this.body, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        renderToBufferIfExist(this.biped_left_arm, this.leftArm, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        renderToBufferIfExist(this.biped_right_arm, this.rightArm, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        renderToBufferIfExist(this.biped_body_low, null, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        renderToBufferIfExist(this.biped_left_leg, this.leftLeg, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        renderToBufferIfExist(this.biped_right_leg, this.rightLeg, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void renderToBufferIfExist(ModelPart part,ModelPart copiedPart,PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha){
        if(part == null){
            return;
        }
        if(copiedPart != null){
            part.copyFrom(copiedPart);
        }
        part.visible = true;
        part.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }


    @Override
    public void setAllVisible(boolean visible) {
        this.head.visible = false;
        this.hat.visible = false;
        this.rightArm.visible = false;
        this.leftArm.visible = false;
        this.rightLeg.visible = false;
        this.leftLeg.visible = false;
    }
}
