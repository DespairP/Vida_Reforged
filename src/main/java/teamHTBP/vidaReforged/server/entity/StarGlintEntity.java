package teamHTBP.vidaReforged.server.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.particles.VidaParticleAttributes;
import teamHTBP.vidaReforged.core.api.VidaElement;

import java.util.ArrayList;

public class StarGlintEntity extends ShootableEntity{
    private static final EntityDataAccessor<VidaElement> ELEMENT = SynchedEntityData.defineId(StarGlintEntity.class, VidaEntityDataSerializer.ELEMENT);


    public StarGlintEntity(EntityType<?> entityType, Level level) {
        super(entityType, level, 100);
    }

    public void init(LivingEntity player, int maxAge, VidaElement element) {
        super.init(player);
        this.maxAge = maxAge;
        this.entityData.set(ELEMENT, element);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ELEMENT, VidaElement.EMPTY);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(ELEMENT, VidaElement.of(tag.getString("Element")));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Element",  this.entityData.get(ELEMENT).name());
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (!this.level().isClientSide) {
            entity.hurt(this.damageSources().mobProjectile(this, (LivingEntity) this.owner), 1.0F);
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        this.discard();
    }

    @Override
    public void doSpawnParticles() {
        double deltaX = getX() - xo;
        double deltaY = getY() - yo;
        double deltaZ = getZ() - zo;
        double dist = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 2);
        for (double j = 0; j < dist; j++) {
            double coeff = j / dist;
            level().addParticle(
                    getParticle(),
                    (float) (xo  + deltaX * coeff),
                    (float) (yo + deltaY * coeff) + 0.1,
                    (float) (zo + deltaZ * coeff),
                    0.15F * (random.nextFloat() - 0.5f),
                    0.05F * (random.nextFloat() - 0.5f),
                    0.15F * (random.nextFloat() - 0.5f));
        }
    }

    @Override
    public ParticleOptions getParticle() {
        return new BaseParticleType(
                VidaParticleTypeLoader.STAR_PARTICLE,
                new VidaParticleAttributes(30, rand.nextFloat() * 0.8f, getElement().getBaseColor().toARGB(), null, BlockPos.ZERO.getCenter().toVector3f())
        );
    }

    public VidaElement getElement() {
        return this.entityData.get(ELEMENT);
    }
}
