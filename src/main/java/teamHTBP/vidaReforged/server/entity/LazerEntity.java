package teamHTBP.vidaReforged.server.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
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
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

import java.util.ArrayList;
import java.util.List;

/**
 * 激光光线
 * */
public abstract class LazerEntity extends Entity implements IEntityAdditionalSpawnData, TraceableEntity {
    private static final EntityDataAccessor<Integer> MAX_LIFE_TIME = SynchedEntityData.defineId(LazerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CURRENT_LIFE_TIME = SynchedEntityData.defineId(LazerEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<List<Vector3d>> TRAILS = SynchedEntityData.defineId(LazerEntity.class, VidaEntityDataSerializer.VEC3D_SERIALIZER.get());
    private int maxLifeTime = 100;
    private static final Logger LOGGER = LogManager.getLogger();
    @javax.annotation.Nullable
    private Entity cachedOwner;


    public LazerEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void setOwner(LivingEntity entity){
        if(entity != null){
            this.cachedOwner = entity;
        }
    }

    public void init(Player player){
        double x = player.getX(), y = player.getEyeY() - 0.5F, z = player.getZ();
        setPos(x, y, z);
        setOwner(player);

        double theta = player.getViewYRot(0), alpha = player.getXRot();
        float delX = -Mth.sin((float) (theta * ((float)Math.PI / 180F))) * Mth.cos((float) (alpha * ((float)Math.PI / 180F)));
        float delY = -Mth.sin((float) ((alpha + 0) * ((float)Math.PI / 180F)));
        float delZ = Mth.cos((float) (theta * ((float)Math.PI / 180F))) * Mth.cos((float) (alpha * ((float)Math.PI / 180F)));

        this.setDeltaMovement(delX, delY, delZ);

        this.entityData.set(MAX_LIFE_TIME, maxLifeTime);
        this.entityData.set(CURRENT_LIFE_TIME, 0);
        this.entityData.set(TRAILS, new ArrayList<>());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(MAX_LIFE_TIME, maxLifeTime);
        this.entityData.define(CURRENT_LIFE_TIME, 0);
        this.entityData.define(TRAILS, new ArrayList<>());

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        CompoundTag vecTags = tag.getCompound("trail");
        List<Vector3d> trails = VidaEntityDataSerializer.VEC_CODEC.parse(NbtOps.INSTANCE, vecTags).get().left().orElse(new ArrayList<>());
        this.entityData.set(MAX_LIFE_TIME, tag.getInt("maxLifeTime"));
        this.entityData.set(CURRENT_LIFE_TIME, tag.getInt("lifeTime"));
        this.entityData.set(TRAILS, trails, true);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("maxLifeTime", this.entityData.get(MAX_LIFE_TIME));
        tag.putInt("lifeTime", this.entityData.get(CURRENT_LIFE_TIME));
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

        int age = this.getLifetime() + 1;
        setLifetime(age);

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        Vec3 pos = this.position();
        Vec3 posDelta = pos.add(getDeltaMovement());
        
        if (age >= getMaxLifeTime() && getMaxLifeTime() > 0 || getMaxLifeTime() == 0) {
            this.discard();
        } else {
            update();
        }

        if(level().isClientSide){
            playParticles();
            return;
        }

        HitResult hitresult = this.level().clip(new ClipContext(pos, posDelta, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        while (!this.isRemoved()) {
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

            if (entityhitresult == null) {
                break;
            }

            hitresult = null;
        }

        onHit(hitresult);
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

    public void setLifetime(int lifeTime){
        this.entityData.set(CURRENT_LIFE_TIME, lifeTime, true);
    }

    public int getLifetime(){
        return this.entityData.get(CURRENT_LIFE_TIME);
    }

    public int getMaxLifeTime(){
        return this.entityData.get(MAX_LIFE_TIME);
    }

    public void removeTrail(int index){
        List<Vector3d> lstPoints = this.entityData.get(TRAILS);
        lstPoints.remove(index);
        this.entityData.set(TRAILS, lstPoints, true);
    }

    public void update() {
        if (getLifetime() > 0) {
            Vec3 point = getPosition(0).add(getDeltaMovement());
            setPos(point.x, point.y, point.z);
        }
    }


    public void playParticles() {
        double deltaX = getX() - xo;
        double deltaY = getY() - yo;
        double deltaZ = getZ() - zo;
        double dist = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 6);
        for (double j = 0; j < dist; j++) {
            double coeff = j / dist;
            level().addParticle(
                    new BaseParticleType(VidaParticleTypeLoader.CUBE_PARTICLE_TYPE.get(), new ARGBColor(255, 190, 0, 255), new Vector3f(), 1, 100),
                    (float) (xo + deltaX * coeff),
                    (float) (yo + deltaY * coeff) + 0.1, (float)
                            (zo + deltaZ * coeff),
                    0.0125f * (random.nextFloat() - 0.5f),
                    0.0125f * (random.nextFloat() - 0.5f),
                    0.0125f * (random.nextFloat() - 0.5f));
        }
    }



    @Nullable
    @Override
    public Entity getOwner() {
        return cachedOwner;
    }
    
    
}
