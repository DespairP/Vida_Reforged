package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.phys.Vec3;
import teamHTBP.vidaReforged.server.blockEntities.GlowingLightBlockEntity;

import static com.mojang.blaze3d.platform.GlConst.*;
import static org.lwjgl.opengl.GL30C.glBindFramebuffer;

public class GlowingLightBlockEntityRenderer implements BlockEntityRenderer<GlowingLightBlockEntity> {
    @Override
    public void render(GlowingLightBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        //int hdrFBO;
        //int[] colorBuffers = new int[2];
        //GlStateManager._genTextures(colorBuffers);



        pPoseStack.pushPose();

        //RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        //RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        //RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        //RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        pPoseStack.popPose();
    }
}
