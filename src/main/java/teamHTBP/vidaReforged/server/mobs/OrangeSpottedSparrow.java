package teamHTBP.vidaReforged.server.mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import teamHTBP.vidaReforged.core.common.mobs.AbstractBeliever;
import teamHTBP.vidaReforged.server.mobs.goal.FlyToGroundGoal;
import teamHTBP.vidaReforged.server.mobs.goal.WaterAvoidingRandomFlyOrWalkStrollGoal;

public class OrangeSpottedSparrow extends Animal implements GeoEntity, FlyingAnimal {
    private final AnimatableInstanceCache geoEntityCache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("move.walk");
    private static final RawAnimation FLY = RawAnimation.begin().thenLoop("move.fly");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("misc.idle");
    protected static final EntityDataAccessor<Boolean> IS_WALKING =
            SynchedEntityData.defineId(OrangeSpottedSparrow.class, EntityDataSerializers.BOOLEAN);

    private PathNavigation groundPathNavigation;
    private PathNavigation flyPathNavigation;

    protected OrangeSpottedSparrow(EntityType<? extends Animal> type, Level world) {
        super(type, world);
        //this.groundPathNavigation = new GroundPathNavigation(this, world);
        //this.flyPathNavigation = createFlyNavigation(world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FlyToGroundGoal(this, 1.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomFlyOrWalkStrollGoal.Walk(this, 0.6D, 0.5F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomFlyOrWalkStrollGoal.Fly(this, 0.6D, 0.5F));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(IS_WALKING, true);
    }



    /**
     * 生物属性
     * */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.FLYING_SPEED, (double)0.4F).add(Attributes.MOVEMENT_SPEED, (double)0.2F);
    }

    protected <E extends GeoAnimatable> PlayState moveAnimController(final AnimationState<E> event) {
        if (!position().equals(new Vec3(xOld, yOld, zOld))){
            if(onGround()){
                return event.setAndContinue(WALK);
            }
            return event.setAndContinue(FLY);
        }
        return PlayState.STOP;
    }

    protected <E extends GeoAnimatable> PlayState idleAnimController(final AnimationState<E> event) {
        if (!event.isMoving()){
            return event.setAndContinue(IDLE);
        }
        return PlayState.STOP;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
                new AnimationController<>(this, "move_controller", 1, this::moveAnimController),
                new AnimationController<>(this, "idle_controller", 0, this::idleAnimController)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoEntityCache;
    }

    public void setWalk(boolean isWalk){
        if(!isWalk){
            this.moveControl = new FlyingMoveControl(this, 10, false);
        } else {
            this.moveControl = new MoveControl(this);
        }
        this.entityData.set(IS_WALKING, isWalk);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance diff, MobSpawnType spawnType, @Nullable SpawnGroupData group, @Nullable CompoundTag tag) {
        setWalk(true);
        return super.finalizeSpawn(level, diff, spawnType, group, tag);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState blockState, BlockPos pos) {
    }



    protected PathNavigation createFlyNavigation(Level level){
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        this.groundPathNavigation = new GroundPathNavigation(this, level);
        this.flyPathNavigation = createFlyNavigation(level);

        if(this.entityData.get(IS_WALKING)){
            this.navigation = groundPathNavigation;
            return navigation;
        }
        this.navigation = flyPathNavigation;
        return navigation;
    }

    @Override
    public PathNavigation getNavigation() {
        if(this.entityData.get(IS_WALKING)){
            this.navigation = groundPathNavigation;
            return groundPathNavigation;
        }
        this.navigation = flyPathNavigation;
        return flyPathNavigation;
    }

    /*    @Override
    public boolean isNoGravity() {
        return !this.entityData.get(IS_WALKING);
    }*/
}
