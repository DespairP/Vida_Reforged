package teamHTBP.vidaReforged.core.common.vertex;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.math.MatrixUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class VidaItemRenderer extends ItemRenderer {
    public static final VidaItemRenderer ITEM_RENDERER = new VidaItemRenderer(Minecraft.getInstance(), Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getModelManager(), Minecraft.getInstance().getItemColors());
    private ItemColors itemColors;
    private static final ModelResourceLocation TRIDENT_MODEL = ModelResourceLocation.vanilla("trident", "inventory");
    private static final ModelResourceLocation SPYGLASS_MODEL = ModelResourceLocation.vanilla("spyglass", "inventory");

    public VidaItemRenderer(Minecraft minecraft, TextureManager textureManager, ModelManager modelManager, ItemColors itemColors) {
        super(minecraft, textureManager, modelManager, itemColors, new BlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));
        this.itemColors = itemColors;
    }

    public void render(ItemStack p_115144_, ItemDisplayContext p_270188_, boolean p_115146_, PoseStack p_115147_, MultiBufferSource p_115148_, int p_115149_, int p_115150_, BakedModel p_115151_, float alpha) {
        if (!p_115144_.isEmpty()) {
            p_115147_.pushPose();
            boolean flag = p_270188_ == ItemDisplayContext.GUI || p_270188_ == ItemDisplayContext.GROUND || p_270188_ == ItemDisplayContext.FIXED;
            if (flag) {
                if (p_115144_.is(Items.TRIDENT)) {
                    p_115151_ = this.getItemModelShaper().getModelManager().getModel(TRIDENT_MODEL);
                } else if (p_115144_.is(Items.SPYGLASS)) {
                    p_115151_ = this.getItemModelShaper().getModelManager().getModel(SPYGLASS_MODEL);
                }
            }

            p_115151_ = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(p_115147_, p_115151_, p_270188_, p_115146_);
            p_115147_.translate(-0.5F, -0.5F, -0.5F);
            if (!p_115151_.isCustomRenderer() && (!p_115144_.is(Items.TRIDENT) || flag)) {
                boolean flag1;
                if (p_270188_ != ItemDisplayContext.GUI && !p_270188_.firstPerson() && p_115144_.getItem() instanceof BlockItem) {
                    Block block = ((BlockItem)p_115144_.getItem()).getBlock();
                    flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                } else {
                    flag1 = true;
                }
                for (var model : p_115151_.getRenderPasses(p_115144_, flag1)) {
                    for (var rendertype : model.getRenderTypes(p_115144_, flag1)) {
                        VertexConsumer vertexconsumer;
                        if (hasAnimatedTexture(p_115144_) && p_115144_.hasFoil()) {
                            p_115147_.pushPose();
                            PoseStack.Pose posestack$pose = p_115147_.last();
                            if (p_270188_ == ItemDisplayContext.GUI) {
                                MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.5F);
                            } else if (p_270188_.firstPerson()) {
                                MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.75F);
                            }

                            if (flag1) {
                                vertexconsumer = getCompassFoilBufferDirect(p_115148_, rendertype, posestack$pose);
                            } else {
                                vertexconsumer = getCompassFoilBuffer(p_115148_, rendertype, posestack$pose);
                            }

                            p_115147_.popPose();
                        } else if (flag1) {
                            vertexconsumer = getFoilBufferDirect(p_115148_, rendertype, true, p_115144_.hasFoil());
                        } else {
                            vertexconsumer = getFoilBuffer(p_115148_, rendertype, true, p_115144_.hasFoil());
                        }

                        this.renderModelLists(model, p_115144_, p_115149_, p_115150_, p_115147_, vertexconsumer, alpha);
                    }
                }
            } else {
                net.minecraftforge.client.extensions.common.IClientItemExtensions.of(p_115144_).getCustomRenderer().renderByItem(p_115144_, p_270188_, p_115147_, p_115148_, p_115149_, p_115150_);
            }

            p_115147_.popPose();
        }
    }


    public void renderModelLists(BakedModel p_115190_, ItemStack p_115191_, int p_115192_, int p_115193_, PoseStack p_115194_, VertexConsumer p_115195_, float alpha) {
        RandomSource randomsource = RandomSource.create();
        long i = 42L;

        for(Direction direction : Direction.values()) {
            randomsource.setSeed(42L);
            this.renderQuadList(p_115194_, p_115195_, p_115190_.getQuads((BlockState)null, direction, randomsource), p_115191_, p_115192_, p_115193_, alpha);
        }

        randomsource.setSeed(42L);
        this.renderQuadList(p_115194_, p_115195_, p_115190_.getQuads((BlockState)null, (Direction)null, randomsource), p_115191_, p_115192_, p_115193_, alpha);
    }

    public void renderQuadList(PoseStack p_115163_, VertexConsumer p_115164_, List<BakedQuad> p_115165_, ItemStack p_115166_, int p_115167_, int p_115168_, float alpha) {
        boolean flag = !p_115166_.isEmpty();
        PoseStack.Pose posestack$pose = p_115163_.last();

        for (BakedQuad bakedquad : p_115165_) {
            int i = -1;
            if (flag && bakedquad.isTinted()) {
                i = this.itemColors.getColor(p_115166_, bakedquad.getTintIndex());
            }

            float f = (float) (i >> 16 & 255) / 255.0F;
            float f1 = (float) (i >> 8 & 255) / 255.0F;
            float f2 = (float) (i & 255) / 255.0F;
            p_115164_.putBulkData(posestack$pose, bakedquad, f, f1, f2, alpha, p_115167_, p_115168_, true);
        }
    }

    public static VertexConsumer getFoilBufferDirect(MultiBufferSource p_115223_, RenderType p_115224_, boolean p_115225_, boolean p_115226_) {
        return p_115226_ ? VertexMultiConsumer.create(p_115223_.getBuffer(p_115225_ ? RenderType.glintDirect() : RenderType.entityGlintDirect()), p_115223_.getBuffer(p_115224_)) : p_115223_.getBuffer(p_115224_);
    }

    private static boolean hasAnimatedTexture(ItemStack p_286353_) {
        return p_286353_.is(ItemTags.COMPASSES) || p_286353_.is(Items.CLOCK);
    }
}
