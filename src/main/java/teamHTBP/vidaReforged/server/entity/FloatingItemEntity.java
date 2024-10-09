package teamHTBP.vidaReforged.server.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

/** 带有光晕的物品 */
public class FloatingItemEntity extends Entity{
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(FloatingItemEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Vector3f> DATA_VEC_TO_POS = SynchedEntityData.defineId(FloatingItemEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<ARGBColor> DATA_OVERRIDE_COLOR = SynchedEntityData.defineId(FloatingItemEntity.class, VidaEntityDataSerializer.COLOR);
    private static final int MAX_AGE = 3600;
    private int age;
    private int pickupDelay;
    public final float bobOffs;

    protected FloatingItemEntity(EntityType<? extends FloatingItemEntity> entityType, Level level){
        super(entityType, level);
        this.age = 0;
        this.bobOffs = 0;
    }

    public FloatingItemEntity(Level level, @NonNull Vec3 fromPos, @NonNull Vector3d deltaPos) {
        this(VidaEntityLoader.FLOATING_ITEM_ENTITY.get(), level);
        setPos(fromPos.x, fromPos.y, fromPos.z);
        setDeltaMovement(deltaPos.x, deltaPos.y, deltaPos.z);
        setDefaultPickUpDelay();
    }

    protected FloatingItemEntity(EntityType<? extends FloatingItemEntity> entityType, Level level, @NonNull Vec3 fromPos, @NonNull Vector3d deltaPos) {
        this(entityType, level);
        setPos(fromPos.x, fromPos.y, fromPos.z);
        setDeltaMovement(deltaPos.x, deltaPos.y, deltaPos.z);
        setDefaultPickUpDelay();
    }

    public FloatingItemEntity init(ItemStack itemStack, Vec3 toPos, ARGBColor particleColor) {
        setToPos(toPos);
        setItem(itemStack);
        if(particleColor != null){
            setParticleColor(particleColor);
        }
        return this;
    }


    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ITEM, ItemStack.EMPTY);
        this.getEntityData().define(DATA_VEC_TO_POS, new Vector3f());
        this.getEntityData().define(DATA_OVERRIDE_COLOR, new ARGBColor(255, 190, 0, 255));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.age = tag.getShort("Age");
        CompoundTag compoundtag = tag.getCompound("Item");
        this.setItem(ItemStack.of(compoundtag));
        if (tag.contains("PickupDelay")) {
            this.pickupDelay = tag.getShort("PickupDelay");
        }
        if (this.getItem().isEmpty()) {
            this.discard();
        }
        ListTag vecListtag = tag.getList("ToPos", 6);
        setToPos(new Vec3(vecListtag.getDouble(0), vecListtag.getDouble(1), vecListtag.getDouble(2)));
        ListTag colorListtag = (ListTag) tag.get("Color");
        setParticleColor(new ARGBColor(colorListtag.getInt(0), colorListtag.getInt(1), colorListtag.getInt(2), colorListtag.getInt(3)));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putShort("Age", (short)this.age);
        if (!this.getItem().isEmpty()) {
            tag.put("Item", this.getItem().save(new CompoundTag()));
        }
        Vec3 toPos = getToPos();
        tag.put("ToPos", this.newDoubleList(toPos.x, toPos.y, toPos.z));
        tag.putShort("PickupDelay", (short)this.pickupDelay);
        ARGBColor color = getParticleColor();
        tag.put("Color", this.newIntList(color.a(), color.r(), color.g(), color.b()));
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (DATA_ITEM.equals(accessor)) {
            this.getItem().setEntityRepresentation(this);
        }
    }

    @Override
    public void tick() {
        if(this.age < MAX_AGE){
            if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
                --this.pickupDelay;
            }
            if(getToPos() != null){
                float velocity = 0.1f;
                Vec3 desiredMotion = getToPos().subtract(position()).normalize().multiply(velocity, velocity, velocity);
                float easing = 0.1f;
                float xMotion = (float) Mth.lerp(easing, getDeltaMovement().x, desiredMotion.x);
                float yMotion = (float) Mth.lerp(easing, getDeltaMovement().y, desiredMotion.y);
                float zMotion = (float) Mth.lerp(easing, getDeltaMovement().z, desiredMotion.z);
                Vec3 resultingMotion = new Vec3(xMotion, yMotion, zMotion);
                if(getToPos().distanceToSqr(position()) <= 0.001f){
                    setDeltaMovement(0, 0, 0);
                }else{
                    setDeltaMovement(resultingMotion);
                }
            }
            ++ this.age;
        }
        // move
        Vec3 movement = getDeltaMovement();
        double distance = movement.horizontalDistance();

        final float xRot = lerpRotation(this.xRotO, (float) (Mth.atan2(movement.y, distance) * (double) (180F / (float) Math.PI)));
        final float yRot = lerpRotation(this.yRotO, (float) (Mth.atan2(movement.x, movement.z) * (double) (180F / (float) Math.PI)));
        setXRot(xRot);
        setYRot(yRot);
        move(MoverType.SELF, movement);

        super.tick();

        // 粒子效果
        if(this.level().isClientSide){
            spawnParticle();
        }

        // 检查是否可以移除
        if (!this.level().isClientSide && this.age >= MAX_AGE) {
            this.discard();
        }
        if (getItem().isEmpty() && !this.isRemoved()) {
            this.discard();
        }
    }

    private static float lerpRotation(float rot, float rotTo) {
        while(rotTo - rot < -180.0F) {
            rot -= 360.0F;
        }

        while(rotTo - rot >= 180.0F) {
            rot += 360.0F;
        }

        return Mth.lerp(0.2F, rot, rotTo);
    }

    /**粒子*/
    protected void spawnParticle() {
        double deltaX = getX() - xo;
        double deltaY = getY() - yo;
        double deltaZ = getZ() - zo;
        double dist = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 3);
        if(dist <= 0){
            level().addParticle(
                    getParticle(),
                    xo ,
                    yo ,
                    zo ,
                    0.05f * (random.nextFloat() - 0.5f),
                    0.05f * (random.nextFloat() - 0.5f),
                    0.05f * (random.nextFloat() - 0.5f));
        }
        for (double spawnPoint = 0; spawnPoint < dist; spawnPoint++) {
            double distFactor = spawnPoint / dist;
            level().addParticle(
                    getParticle(),
                    xo + deltaX * distFactor,
                    yo + deltaY * distFactor + 0.1,
                    zo + deltaZ * distFactor,
                    0.015f * (random.nextFloat() - 0.5f),
                    0.015f * (random.nextFloat() - 0.5f),
                    0.015f * (random.nextFloat() - 0.5f));
        }
    }

    public ParticleOptions getParticle(){
        return new BaseParticleType(VidaParticleTypeLoader.ORB_PARTICLE.get(), getParticleColor(), new Vector3f(), 0.4F, 50);
    }

    public void playerTouch(Player player) {
        if (!this.level().isClientSide) {
            if (this.pickupDelay > 0) return;
            ItemStack itemstack = this.getItem();
            Item item = itemstack.getItem();
            ItemStack copy = itemstack.copy();

            if (player.getInventory().add(itemstack)) {
                int remainCount = copy.getCount() - itemstack.getCount();
                copy.setCount(remainCount);
                player.take(this, remainCount);
                if (itemstack.isEmpty()) {
                    this.discard();
                    itemstack.setCount(remainCount);
                }
                player.awardStat(Stats.ITEM_PICKED_UP.get(item), remainCount);
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_ENTITY.trigger((ServerPlayer)player, getItem(), this);
                }
                Level level = player.level();
                level.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        }
    }

    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM);
    }

    public void setItem(ItemStack stack) {
        this.getEntityData().set(DATA_ITEM, stack);
    }

    public Component getName() {
        Component component = this.getCustomName();
        return (Component)(component != null ? component : Component.translatable(this.getItem().getDescriptionId()));
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }

    public boolean isAttackable() {
        return false;
    }

    public float getSpin(float p_32009_) {
        return ((float)this.getAge() + p_32009_) / 20.0F + this.bobOffs;
    }

    public void setDefaultPickUpDelay() {
        this.pickupDelay = 10;
    }
    public void setNoPickUpDelay() {
        this.pickupDelay = 0;
    }


    public int getAge() {
        return age;
    }

    public Vec3 getToPos() {
        Vector3f vector3f = this.entityData.get(DATA_VEC_TO_POS);
        return new Vec3(vector3f);
    }

    public void setToPos(Vec3 vec3){
        this.entityData.set(DATA_VEC_TO_POS, vec3.toVector3f());
    }

    public ARGBColor getParticleColor() {
        return this.entityData.get(DATA_OVERRIDE_COLOR);
    }

    public void setParticleColor(ARGBColor color){
        this.entityData.set(DATA_OVERRIDE_COLOR, color);
    }

    protected ListTag newIntList(int... p_20066_) {
        ListTag listtag = new ListTag();

        for(int f : p_20066_) {
            listtag.add(IntTag.valueOf(f));
        }

        return listtag;
    }
}
