package teamHTBP.vidaReforged.server.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SparkEntity extends Entity implements IEntityAdditionalSpawnData {
    private static final EntityDataAccessor<Integer> MAX_LIFE_TIME = SynchedEntityData.defineId(SparkEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFE_TIME = SynchedEntityData.defineId(SparkEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> ARGB = SynchedEntityData.defineId(SparkEntity.class, EntityDataSerializers.STRING);
    private int maxLifeTime = 1000;
    private ARGBColor color = new ARGBColor(0, 0, 0, 0);
    private BaseParticleType particleType;

    public void initEntity(@NotNull Player player,int maxLifeTime,ARGBColor color) {
        this.maxLifeTime = maxLifeTime;
        this.entityData.set(MAX_LIFE_TIME, this.maxLifeTime);
        this.color = color;
        this.entityData.set(ARGB, String.format("%s;%s;%s;%s", color.a(), color.r(), color.g(), color.b()));
        Vec3 lookAngle = player.getLookAngle();
        setPos(player.getEyePosition().add(lookAngle.scale(2)));
    }

    public SparkEntity(EntityType<SparkEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(MAX_LIFE_TIME, maxLifeTime);
        this.entityData.define(LIFE_TIME, 0);
        this.entityData.define(ARGB,"0;0;0;0");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(MAX_LIFE_TIME, tag.getInt("maxLifeTime"));
        this.entityData.set(LIFE_TIME, tag.getInt("lifeTime"));
        this.entityData.set(ARGB, tag.getString("argb"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("maxLifeTime", this.entityData.get(MAX_LIFE_TIME));
        tag.putInt("lifeTime", this.entityData.get(LIFE_TIME));
        tag.putString("argb", this.entityData.get(ARGB));
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {

    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {

    }

    public void makeParticle(){
        if(!this.level().isClientSide()){
            return;
        }

        if(this.particleType == null){
            List<String> colors = Arrays.stream(this.entityData.get(ARGB).split(";")).collect(Collectors.toList());
            if(colors.size() >= 4){
                this.color = new ARGBColor(Integer.parseInt(colors.get(0)), Integer.parseInt(colors.get(1)), Integer.parseInt(colors.get(2)), Integer.parseInt(colors.get(3)));
            }
            this.particleType = new BaseParticleType(VidaParticleTypeLoader.SPARK_PARTICLE_TYPE.get(), this.color, 1,  this.entityData.get(MAX_LIFE_TIME));
            this.level().addParticle(this.particleType, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0, 0, 0);
        }

    }

    @Override
    public void tick() {
        super.tick();

        if(level().isClientSide){
            makeParticle();
            return;
        }

        int currentLifeTime = this.entityData.get(LIFE_TIME);
        if(currentLifeTime > this.entityData.get(MAX_LIFE_TIME)){
            this.discard();
            return;
        }

        // 每10帧数检测一次
        if(currentLifeTime % 10 == 0) {
            List<ServerPlayer> entitiesNearBy = getEntitiesNearBy();
            entitiesNearBy.forEach(entity -> entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 35, 0)));
        }
        this.entityData.set(LIFE_TIME, this.entityData.get(LIFE_TIME) + 1);
    }

    /**获取碰撞的玩家*/
    public List<ServerPlayer> getEntitiesNearBy(){
        List<ServerPlayer> entities = new ArrayList<>();
        AABB aabb = getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D);
        for(Entity entity : level().getEntities(this, aabb, (entity) -> entity instanceof ServerPlayer)){
            entities.add((ServerPlayer) entity);
        }
        return entities;
    }
}
