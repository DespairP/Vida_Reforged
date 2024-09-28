package teamHTBP.vidaReforged.server.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticle;

/**
 * @author TT432
 */
public class MagicParticleProjectile extends Projectile {
    // TODO 暂时硬编码，需要在服务端设置后给客户端同步一次
    public MagicParticle particle = MagicParticle.EMPTY;

    public MagicParticleProjectile(EntityType<MagicParticleProjectile> type, Level level) {
        super(type, level);
    }

    public void initMagicParticleProjectile(@NotNull Player player) {
        setOwner(player);
        Vec3 lookAngle = player.getLookAngle();
        setPos(player.getEyePosition().add(lookAngle.scale(2)));
        setDeltaMovement(lookAngle.scale(particle.speed().value()));
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();

        if (particle == null) {
            discard();
            return;
        }

        if (tickCount > particle.maxAge().value()) {
            discard();
            return;
        }

        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) *  (180F / (float) Math.PI)));
            this.setXRot((float) (Mth.atan2(vec3.y, d0) *  (180F / (float) Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);

        if (this.isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW) || this.isInFluidType((fluidType, height) -> this.canFluidExtinguish(fluidType))) {
            this.clearFire();
        }

        Vec3 vec32 = this.position();
        Vec3 vec33 = vec32.add(vec3);
        HitResult hitresult = this.level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitresult.getType() != HitResult.Type.MISS) {
            vec33 = hitresult.getLocation();
        }

        while (!this.isRemoved()) {
            EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
            if (entityhitresult != null) {
                hitresult = entityhitresult;
            }

            if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hitresult).getEntity();
                Entity entity1 = this.getOwner();
                if (entity instanceof Player p1 && entity1 instanceof Player p2 && !p2.canHarmPlayer(p1)) {
                    hitresult = null;
                    entityhitresult = null;
                }
            }

            if (hitresult != null && hitresult.getType() != HitResult.Type.MISS) {
                if (net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult))
                    break;
                this.onHit(hitresult);
                this.hasImpulse = true;
            }

            if (entityhitresult == null) {
                break;
            }

            hitresult = null;
        }

        vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;

        double d7 = this.getX() + d5;
        double d2 = this.getY() + d6;
        double d3 = this.getZ() + d1;
        double d4 = vec3.horizontalDistance();

        this.setYRot((float) (Mth.atan2(d5, d1) *  (180F / (float) Math.PI)));

        this.setXRot((float) (Mth.atan2(d6, d4) *  (180F / (float) Math.PI)));
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));

        if (this.isInWater()) {
            for (int j = 0; j < 4; ++j) {
                this.level().addParticle(ParticleTypes.BUBBLE, d7 - d5 * 0.25D, d2 - d6 * 0.25D, d3 - d1 * 0.25D, d5, d6, d1);
            }
        }

        this.setPos(d7, d2, d3);
        this.checkInsideBlocks();

        if (this.level().isClientSide) {
            this.makeParticle(2);
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.updateInWaterStateAndDoFluidPushing();
    }

    protected void onHitEntity(EntityHitResult p_36757_) {
        super.onHitEntity(p_36757_);
        Entity entity = p_36757_.getEntity();

        DamageSource damagesource = damageSources().magic();

        if (entity.hurt(damagesource, Mth.ceil(Mth.clamp(particle.damage().value(), 0.0D, Integer.MAX_VALUE)))) {
        }

        this.discard();
    }

    public boolean isAttackable() {
        return false;
    }

    protected float getEyeHeight(Pose p_36752_, EntityDimensions p_36753_) {
        return 0.13F;
    }

    private void makeParticle(int amount) {
        // TODO colorB
        if (amount > 0) {
            double d0 = (double) (particle.colorA() >> 16 & 255) / 255.0D;
            double d1 = (double) (particle.colorA() >> 8 & 255) / 255.0D;
            double d2 = (double) (particle.colorA() >> 0 & 255) / 255.0D;

            for (int j = 0; j < amount; ++j) {
                this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
            }
        }
    }

    protected EntityHitResult findHitEntity(Vec3 p_36758_, Vec3 p_36759_) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, p_36758_, p_36759_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }
}
