package teamHTBP.vidaReforged.server.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticle;

public class PartyParrotProjecttile extends Projectile {
    private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(PartyParrotProjecttile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_AGE = SynchedEntityData.defineId(PartyParrotProjecttile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(PartyParrotProjecttile.class, EntityDataSerializers.INT);


    public PartyParrotProjecttile(EntityType<PartyParrotProjecttile> type, Level level) {
        super(type, level);
    }

    public void initProjectile(@NotNull Player player, MagicParticle particle) {
        setOwner(player);
        Vec3 lookAngle = player.getLookAngle();
        setPos(player.getEyePosition().x - 0.5F, player.getEyeY() - (double)0.6F, player.getZ() - 0.5f);
        this.shootFromRotation(this, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
        setDeltaMovement(lookAngle.scale(particle.speed().value()));
        this.entityData.set(AGE, 0);
        this.entityData.set(MAX_AGE, ((int) particle.maxAge().value()));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(AGE, 0);
        this.entityData.define(MAX_AGE, 30);
        this.entityData.define(TYPE, random.nextInt(9));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(TYPE,tag.getInt("type"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("type", this.entityData.get(TYPE));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();

        entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float)0);

        this.discard();
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
            this.setXRot((float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI)));
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

        if(level().isClientSide){
            return;
        }
        this.entityData.set(AGE, this.entityData.get(AGE) + 1);
        if(this.entityData.get(AGE) > this.entityData.get(MAX_AGE)){
            this.discard();
            return;
        }
    }

    protected float getGravity() {
        return 0.03F;
    }

    private void makeParticle(int amount) {
        if (amount > 0) {
            double d0 = 1;
            double d1 = 1;
            double d2 = 1;

            for (int j = 0; j < amount; ++j) {
                this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
            }
        }
    }

    protected EntityHitResult findHitEntity(Vec3 p_36758_, Vec3 p_36759_) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, p_36758_, p_36759_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    public int getTypeOfParrot(){
        return this.entityData.get(TYPE);
    }
}
