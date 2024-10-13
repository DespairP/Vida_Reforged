package teamHTBP.vidaReforged.server.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

public abstract class ShootableEntity extends Entity implements IEntityAdditionalSpawnData, TraceableEntity {
    /**时长*/
    protected int age = 0;
    /**最大时长*/
    protected int maxAge = 100;
    /**发射者*/
    protected LivingEntity owner;
    /**轨迹记录*/
    public static final EntityDataAccessor<List<Vector3d>> TRAILS = SynchedEntityData.defineId(ShootableEntity.class, VidaEntityDataSerializer.VEC3D_SERIALIZER.get());
    private static final Logger LOGGER = LogManager.getLogger();
    protected RandomSource rand;


    public ShootableEntity(EntityType<?> entityType, Level level, int maxAge) {
        super(entityType, level);
        this.rand = RandomSource.create();
        this.age = 0;
        this.maxAge = maxAge;
    }

    public void setOwner(LivingEntity entity){
        if(entity != null){
            this.owner = entity;
        }
    }

    protected void init(LivingEntity player){
        double x = player.getX();
        double y = player.getEyeY() - 0.5F;
        double z = player.getZ();

        setPos(x, y, z);
        setOwner(player);

        double theta = player.getViewYRot(0), alpha = player.getXRot();
        float delX = -Mth.sin((float) (theta * ((float)Math.PI / 180F))) * Mth.cos((float) (alpha * ((float)Math.PI / 180F)));
        float delY = -Mth.sin((float) ((alpha + 0) * ((float)Math.PI / 180F)));
        float delZ = Mth.cos((float) (theta * ((float)Math.PI / 180F))) * Mth.cos((float) (alpha * ((float)Math.PI / 180F)));

        this.setDeltaMovement(delX, delY, delZ);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TRAILS, new ArrayList<>());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        CompoundTag vecTags = tag.getCompound("trail");
        List<Vector3d> trails = VidaEntityDataSerializer.VEC_CODEC.parse(NbtOps.INSTANCE, vecTags).get().left().orElse(new ArrayList<>());
        this.entityData.set(TRAILS, trails, true);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.put("trails", VidaEntityDataSerializer.VEC_CODEC.encodeStart(NbtOps.INSTANCE, this.entityData.get(TRAILS)).getOrThrow(true, LOGGER::error));
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {

    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {

    }

    @Override
    public void tick() {
        super.tick();

        List<Vector3d> tails = new ArrayList<>(this.entityData.get(TRAILS));
        tails.add(getTail());
        this.entityData.set(TRAILS, tails, true);

        while (tails.size() > 15) {
            tails.remove(0);
        }

        age += 1;
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        Vec3 pos = this.position();
        Vec3 posDelta = pos.add(getDeltaMovement());

        if (age >= maxAge && age > 0 || maxAge == 0) {
            this.discard();
        } else {
            update();
        }

        if(level().isClientSide){
            doSpawnParticles();
            return;
        }

        HitResult hitresult = this.level().clip(new ClipContext(pos, posDelta, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

        EntityHitResult entityhitresult = this.findHitEntity(pos, posDelta);
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

        if(hitresult != null){
            onHit(hitresult);
        }
    }

    protected void onHit(HitResult result){
        HitResult.Type hitresult$type = result.getType();
        if (hitresult$type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)result);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, result.getLocation(), GameEvent.Context.of(this, (BlockState)null));
        } else if (hitresult$type == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult)result;
            this.onHitBlock(blockhitresult);
            BlockPos blockpos = blockhitresult.getBlockPos();
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level().getBlockState(blockpos)));
        }
    }
    protected abstract void onHitEntity(EntityHitResult entityHitResult);

    protected abstract void onHitBlock(BlockHitResult blockHitResult);

    protected EntityHitResult findHitEntity(Vec3 pos, Vec3 posInDelta) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, pos, posInDelta, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), entity -> true);
    }

    protected Vector3d getTail() {
        return new Vector3d(this.xo, this.yo, this.zo);
    }

    public void removeTrail(int index){
        List<Vector3d> lstPoints = this.entityData.get(TRAILS);
        lstPoints.remove(index);
        this.entityData.set(TRAILS, lstPoints, true);
    }

    public void update() {
        if (age > 0) {
            Vec3 point = getPosition(0).add(getDeltaMovement());
            setPos(point.x, point.y, point.z);
        }
    }


    public void doSpawnParticles() {
        ParticleOptions options = getParticle();
        if(options == null){
            return;
        }
        double deltaX = getX() - xo;
        double deltaY = getY() - yo;
        double deltaZ = getZ() - zo;
        double dist = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 6);
        for (double j = 0; j < dist; j++) {
            double coeff = j / dist;
            level().addParticle(
                    getParticle(),
                    (float) (xo + deltaX * coeff),
                    (float) (yo + deltaY * coeff) + 0.1, (float)
                            (zo + deltaZ * coeff),
                    0.0125f * (random.nextFloat() - 0.5f),
                    0.0125f * (random.nextFloat() - 0.5f),
                    0.0125f * (random.nextFloat() - 0.5f));
        }
    }

    public abstract ParticleOptions getParticle();

    @Nullable
    @Override
    public Entity getOwner() {
        return owner;
    }

}
