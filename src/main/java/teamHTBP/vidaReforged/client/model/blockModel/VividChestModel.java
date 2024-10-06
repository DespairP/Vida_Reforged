
package teamHTBP.vidaReforged.client.model.blockModel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class VividChestModel<T extends BlockEntity> extends BlockEntityWithoutLevelRenderer {

    private final Supplier<T> blockEntitySupplier;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public VividChestModel(BlockEntityRenderDispatcher renderDispatcher, EntityModelSet modelSet, Supplier<T> blockEntitySupplier) {
        super(renderDispatcher, modelSet);
        this.blockEntitySupplier = blockEntitySupplier;
        this.blockEntityRenderDispatcher = renderDispatcher;
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        this.blockEntityRenderDispatcher.renderItem(this.blockEntitySupplier.get(), poseStack, bufferSource, combinedLightIn, combinedOverlayIn);
    }
}
