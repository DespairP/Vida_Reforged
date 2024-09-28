package teamHTBP.vidaReforged.server.mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.server.entity.VidaEntityDataSerializer;

public class GlowLight extends Mob {
    private static final EntityDataAccessor<Integer> MAX_LIFE_TIME = SynchedEntityData.defineId(GlowLight.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CURRENT_LIFE_TIME = SynchedEntityData.defineId(GlowLight.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ARGBColor> COLOR = SynchedEntityData.defineId(GlowLight.class, VidaEntityDataSerializer.COLOR);
    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(GlowLight.class, EntityDataSerializers.FLOAT);
    private BlockPos target = BlockPos.ZERO;
    private int targetChangeCooldown = 0;

    public GlowLight(EntityType<? extends Mob> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.FLYING_SPEED, 1);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MAX_LIFE_TIME, 100);
        this.entityData.define(CURRENT_LIFE_TIME, 0);
        this.entityData.define(COLOR, ARGBColor.BLACK);
        this.entityData.define(SIZE, 0.5f);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(MAX_LIFE_TIME, tag.getInt("maxLifeTime"));
        this.entityData.set(CURRENT_LIFE_TIME, tag.getInt("lifeTime"));
        this.entityData.set(COLOR, ARGBColor.argb(tag.getInt("argb")));
        this.entityData.set(SIZE, tag.getFloat("size"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("maxLifeTime", this.entityData.get(MAX_LIFE_TIME));
        tag.putInt("lifeTime", this.entityData.get(CURRENT_LIFE_TIME));
        tag.putInt("argb", this.entityData.get(COLOR).argb());
        tag.putFloat("size",  this.entityData.get(SIZE));
    }

    public void setLifetime(int lifeTime){
        this.entityData.set(CURRENT_LIFE_TIME, lifeTime, true);
    }

    public int getLifetime(){
        return this.entityData.get(CURRENT_LIFE_TIME);
    }

    private void setMaxLifeTime(int maxLifeTime){
        this.entityData.set(MAX_LIFE_TIME, maxLifeTime);
    }

    public int getMaxLifeTime(){
        return this.entityData.get(MAX_LIFE_TIME);
    }

    public void setSize(float size){ this.entityData.set(SIZE, size); }

    public float getSize() { return this.entityData.get(SIZE); }

    public void setCol(ARGBColor color){ this.entityData.set(COLOR, color); }

    public ARGBColor getColor(){ return this.entityData.get(COLOR); }

    @Override
    public void tick() {
        super.tick();

        int age = this.getLifetime() + 1;
        setLifetime(age);

        if (age >= getMaxLifeTime()) {
            this.discard();
        }

        if (!this.level().isClientSide && !this.isRemoved()) {
            this.targetChangeCooldown -= (this.position().distanceToSqr(xo, yo, zo) < 0.0125) ? 10 : 1;

            if ((target.getX() == 0 && target.getY() == 0 && target.getZ() == 0) || this.position().distanceToSqr(target.getCenter()) < 9 || targetChangeCooldown <= 0) {
                selectBlockTarget(level());
            }

            Vec3 targetVector = new Vec3(this.target.getX() - getX(), this.target.getY() - getY(), this.target.getZ() - getZ());
            double length = targetVector.length();
            targetVector = targetVector.scale(0.1 / length);
            Vec3 delMov = getDeltaMovement();
            Vec3 newDelMov = new Vec3(
                    (0.9) * delMov.x + (0.1) * targetVector.x,
                    (0.9) * delMov.y + (0.1) * targetVector.y,
                    (0.9) * delMov.z + (0.1) * targetVector.z
            );
            //
            setDeltaMovement(newDelMov);
            if (!this.blockPosition().equals(this.target)) {
                this.move(MoverType.SELF, this.getDeltaMovement());
            }
            this.setSpeed((float) (random.nextFloat() * 0.95F));
        }
    }


    private void selectBlockTarget(Level level) {
        int groundLevel = getBlockY() - 20;
        for (int i = 0; i < 20; i++) {
            BlockPos posDelta = new BlockPos(getBlockX(), getBlockY() - i, getBlockZ());
            if (!level.getBlockState(posDelta).isValidSpawn(level, posDelta, VidaMobsLoader.GLOW_LIGHT.get())) {
                groundLevel = posDelta.getY();
            }
        }

        //
        int xTarget = (int) (this.getX() + random.nextGaussian() * 30);
        int yTarget = (int) Math.min(Math.max((this.getY() + random.nextGaussian() * 10) , groundLevel + random.nextGaussian() * 5), groundLevel + random.nextGaussian() + 10);
        int zTarget = (int) (this.getZ() + random.nextGaussian() * 30);

        target = new BlockPos(xTarget, yTarget, zTarget);
        targetChangeCooldown = random.nextInt() % 100;
    }

    public static boolean checkGlowSpawnRules(EntityType<GlowLight> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && isDarkEnoughToSpawn(level, pos, rand);
    }


    public static boolean isDarkEnoughToSpawn(ServerLevelAccessor level, BlockPos pos, RandomSource rand) {
        if (level.getBrightness(LightLayer.SKY, pos) > rand.nextInt(32)) {
            return false;
        } else {
            DimensionType dimensiontype = level.dimensionType();
            int i = dimensiontype.monsterSpawnBlockLightLimit() + 5;
            if (level.getBrightness(LightLayer.BLOCK, pos) > i) {
                return false;
            }
            return true;
        }
    }


    protected void checkFallDamage(double p_27419_, boolean p_27420_, BlockState p_27421_, BlockPos p_27422_) {
    }

    @Override
    public boolean isSuppressingBounce() {
        return true;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance diff, MobSpawnType spawnType, @Nullable SpawnGroupData group, @Nullable CompoundTag tag) {
        this.setMaxLifeTime(200 + random.nextInt(3000));
        this.setSize((float) (0.1F + random.nextInt(20) * 0.02F ));
        VidaElement element = VidaElement.randomValue();
        this.setCol(element.baseColor.toARGB());
        this.setSilent(true);
        this.setNoGravity(true);

        return super.finalizeSpawn(level, diff, spawnType, group, tag);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public boolean isAttackable() {
        return false;
    }
}
