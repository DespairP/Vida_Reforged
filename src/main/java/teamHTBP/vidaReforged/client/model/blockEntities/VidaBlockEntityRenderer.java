package teamHTBP.vidaReforged.client.model.blockEntities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class VidaBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private BlockEntityRendererProvider.Context context;
    protected Minecraft mc;

    public VidaBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.mc = Minecraft.getInstance();
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull T blockEntity){
        return BlockEntityRenderer.super.shouldRenderOffScreen(blockEntity);
    }

    @Override
    public boolean shouldRender(T blockEntity, Vec3 viewPoint) {
        return BlockEntityRenderer.super.shouldRender(blockEntity, viewPoint);
    }

    @Override
    public abstract void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn);
}
