package teamHTBP.vidaReforged.client.model.armors.leggings;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public class AbstractVidaLeggings extends HumanoidModel {
    public ModelPart body_low;

    public AbstractVidaLeggings(ModelPart root) {
        super(root);

        body_low = root.getChild("body_low");
    }


    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.body_low.visible = true;
        this.body_low.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }


    @Override
    public void setAllVisible(boolean visible) {
        this.head.visible = false;
        this.hat.visible = false;
        this.rightArm.visible = false;
        this.leftArm.visible = false;
        this.rightLeg.visible = true;
        this.leftLeg.visible = true;
        this.body_low.visible = true;
    }
}
