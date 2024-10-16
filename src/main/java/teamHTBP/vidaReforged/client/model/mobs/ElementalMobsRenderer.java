package teamHTBP.vidaReforged.client.model.mobs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Spider;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.client.RenderTypeHandler;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.isEntityUpsideDown;

public class ElementalMobsRenderer<M extends Z,Z extends Mob,K extends HierarchicalModel<Z>> extends EntityRenderer<M> {
    private MobRenderer<Z,K> entityRenderer;
    EntityModel<Z> entityModel;

    public ElementalMobsRenderer(EntityRendererProvider.Context context, MobRenderer<Z,K> mainRenderer, EntityModel<Z> copiedModel) {
        super(context);
        this.entityRenderer = mainRenderer;
        this.entityModel = copiedModel;
    }

    @Override
    public void render(M entity, float p1, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        this.entityRenderer.render(entity, p1, partialTicks, poseStack, bufferSource, light);
        poseStack.pushPose();
        this.entityModel.attackTime = entity.getAttackAnim(partialTicks);

        boolean shouldSit = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());
        this.entityModel.riding = shouldSit;
        this.entityModel.young = entity.isBaby();
        float f = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
        float f2 = f1 - f;
        if (shouldSit && entity.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity.getVehicle();
            f = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
            f2 = f1 - f;
            float f3 = Mth.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        if (isEntityUpsideDown(entity)) {
            f6 *= -1.0F;
            f2 *= -1.0F;
        }

        if (entity.hasPose(Pose.SLEEPING)) {
            Direction direction = entity.getBedOrientation();
            if (direction != null) {
                float f4 = entity.getEyeHeight(Pose.STANDING) - 0.1F;
                poseStack.translate((float)(-direction.getStepX()) * f4, 0.0F, (float)(-direction.getStepZ()) * f4);
            }
        }

        float f7 = this.getBob(entity, partialTicks);
        this.setupRotations(entity, poseStack, f7, f, partialTicks);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        //this.scale(entity, poseStack, partialTicks);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && entity.isAlive()) {
            f8 = entity.walkAnimation.speed(partialTicks);
            f5 = entity.walkAnimation.position(partialTicks);
            if (entity.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.entityModel.prepareMobModel(entity, f5, f8, partialTicks);
        this.entityModel.setupAnim(entity, f5, f8, f7, f2, f6);


        this.renderGlint(poseStack, bufferSource, light, entityModel);
        poseStack.popPose();
    }


    protected void setupRotations(M entity, PoseStack poseStack, float partialTicks, float p_115320_, float p_115321_) {
        if (this.isShaking(entity)) {
            p_115320_ += (float)(Math.cos((double)entity.tickCount * 3.25D) * Math.PI * (double)0.4F);
        }

        if (!entity.hasPose(Pose.SLEEPING)) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - p_115320_));
        }

        if (entity.deathTime > 0) {
            float f = ((float)entity.deathTime + p_115321_ - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            poseStack.mulPose(Axis.ZP.rotationDegrees(f * this.getFlipDegrees(entity)));
        } else if (entity.isAutoSpinAttack()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F - entity.getXRot()));
            poseStack.mulPose(Axis.YP.rotationDegrees(((float)entity.tickCount + p_115321_) * -75.0F));
        } else if (entity.hasPose(Pose.SLEEPING)) {
            Direction direction = entity.getBedOrientation();
            float f1 = direction != null ? sleepDirectionToRotation(direction) : p_115320_;
            poseStack.mulPose(Axis.YP.rotationDegrees(f1));
            poseStack.mulPose(Axis.ZP.rotationDegrees(this.getFlipDegrees(entity)));
            poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else if (isEntityUpsideDown(entity)) {
            poseStack.translate(0.0F, entity.getBbHeight() + 0.1F, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }

    protected float getFlipDegrees(M p_116011_) {
        return 180.0F;
    }


    private static float sleepDirectionToRotation(Direction direction) {
        switch (direction) {
            case SOUTH:
                return 90.0F;
            case WEST:
                return 0.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }

    protected boolean isShaking(M p_115304_) {
        return p_115304_.isFullyFrozen();
    }



    protected float getBob(M p_115305_, float p_115306_) {
        return (float)p_115305_.tickCount + p_115306_;
    }

    private void renderGlint(PoseStack p_289673_, MultiBufferSource p_289654_, int p_289649_, net.minecraft.client.model.Model p_289659_) {
        p_289659_.renderToBuffer(p_289673_, p_289654_.getBuffer(RenderTypeHandler.ENTITY_GLINT_FOIL), p_289649_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

    }

    @Override
    public ResourceLocation getTextureLocation(M entity) {
        return entityRenderer.getTextureLocation(entity);
    }

    public static final class ElementSpiderRenderer extends SpiderRenderer<Spider>{
        public ElementSpiderRenderer(EntityRendererProvider.Context context) {
            super(context);
        }

        @Nullable
        @Override
        protected RenderType getRenderType(Spider p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
            ResourceLocation resourcelocation = this.getTextureLocation(p_115322_);
            return RenderType.entityTranslucentEmissive(resourcelocation, false);
        }
    }

}
