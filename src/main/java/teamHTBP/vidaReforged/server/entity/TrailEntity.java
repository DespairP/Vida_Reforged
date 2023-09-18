package teamHTBP.vidaReforged.server.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.joml.Vector3d;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.particles.options.BaseBezierParticleType;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;
import teamHTBP.vidaReforged.core.utils.math.Bezier3Curve;

import java.util.ArrayList;
import java.util.List;

public class TrailEntity extends Entity implements IEntityAdditionalSpawnData {
    private static final EntityDataAccessor<BaseBezierParticleType> PARTICLE = SynchedEntityData.defineId(TrailEntity.class, EntityDataSerializer.BEZIER_PARTICLE);
    private static final EntityDataAccessor<Integer> MAX_LIFE_TIME = SynchedEntityData.defineId(TrailEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFE_TIME = SynchedEntityData.defineId(TrailEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Bezier3Curve> BEZ = SynchedEntityData.defineId(TrailEntity.class, EntityDataSerializer.CURVE);
    private int maxLifeTime = 200;

    public void initParticle(Player player, Bezier3Curve curve){
        this.maxLifeTime = 200;
        this.entityData.set(MAX_LIFE_TIME, this.maxLifeTime);
        setPos(curve.pos0.x, curve.pos0.y, curve.pos0.z);

        Direction direction = player.getDirection();
        this.entityData.set(PARTICLE, new BaseBezierParticleType(1, 0, 0, 0, 1, 10, new ArrayList<Vector3d>()));

        this.entityData.set(BEZ,curve);
    }

    public TrailEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(PARTICLE,new BaseBezierParticleType());
        this.entityData.define(MAX_LIFE_TIME, maxLifeTime);
        this.entityData.define(LIFE_TIME, 0);
        this.entityData.define(BEZ,new Bezier3Curve(null, null, null, null));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(MAX_LIFE_TIME, tag.getInt("maxLifeTime"));
        this.entityData.set(LIFE_TIME, tag.getInt("lifeTime"));
        this.entityData.set(PARTICLE, BaseBezierParticleType.fromTag(tag));
        this.entityData.set(BEZ, JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).fromJson(tag.getString("bez"), Bezier3Curve.class));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("maxLifeTime", this.entityData.get(MAX_LIFE_TIME));
        tag.putInt("lifeTime", this.entityData.get(LIFE_TIME));
        this.entityData.get(PARTICLE).toTag(tag);
        tag.putString("bez", JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).toJson(this.entityData.get(BEZ)));
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
        if(level().isClientSide){
            this.level().addParticle(this.entityData.get(PARTICLE), getX(), getY(), getZ(),0,0,0);
            return;
        }
        int currentLifeTime = this.entityData.get(LIFE_TIME);
        if(currentLifeTime > this.entityData.get(MAX_LIFE_TIME)){
            this.discard();
            return;
        }
        this.entityData.set(LIFE_TIME, this.entityData.get(LIFE_TIME) + 1);
        update();
    }

    protected Vector3d getTail() {
        return new Vector3d(this.xo, this.yo, this.zo);
    }

    public void update() {
        List<Vector3d> tails = this.entityData.get(PARTICLE).getTails();

        if (this.entityData.get(LIFE_TIME) % 1 == 0) {
            tails.add(getTail());
            while (tails.size() > 40) {
                tails.remove(0);
            }
        }

        var t = (Minecraft.getInstance().getFrameTime() + this.entityData.get(LIFE_TIME)) / this.entityData.get(MAX_LIFE_TIME);
        Vector3d point = this.entityData.get(BEZ).getPoint(t);
        setPos(point.x, point.y, point.z);


        if (this.entityData.get(MAX_LIFE_TIME) - this.entityData.get(LIFE_TIME) < 15) {
            tails.remove(0);
        }

    }
}
