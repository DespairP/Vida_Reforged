package teamHTBP.vidaReforged.core.common.mobs;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import teamHTBP.vidaReforged.core.api.debug.IDebugObj;
import teamHTBP.vidaReforged.core.api.mobs.IAttackManagerEntity;
import teamHTBP.vidaReforged.core.common.mobs.manager.AttackManager;
import teamHTBP.vidaReforged.core.common.mobs.manager.FiniteStateManager;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author DustW
 */
public abstract class AbstractBeliever extends VidaPathfinderMob implements GeoAnimatable, IAttackManagerEntity, IDebugObj {
    protected static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(AbstractBeliever.class, EntityDataSerializers.BOOLEAN);

    private static final String STANDBY_STATE = "STANDBY";
    private static final String WALK_STATE = "WALK";
    private static final String ATTACK_STATE = "ATTACK";

    @Getter
    protected final AttackManager attackManager;
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    protected final FiniteStateManager<AbstractBeliever> stateManager;

    protected AbstractBeliever(EntityType<? extends PathfinderMob> type, Level level, AttackManager attackManager) {
        super(type, level);

        stateManager = new FiniteStateManager<>(this);
        stateManager.addState(mob -> !moving() && !has(GOAL_FLAG_JUMP) && !isAttacking(), STANDBY_STATE);
        stateManager.addState(mob -> moving() && !isAttacking(), WALK_STATE);
        stateManager.addState(mob -> !moving() && isAttacking(), ATTACK_STATE);

        this.attackManager = attackManager;
    }

    @Override
    protected void registerGoals() {
        // nothing
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        // jump
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // look
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        // move look
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        // target
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        entityData.define(ATTACKING, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (!getCommandSenderWorld().isClientSide) {
            entityData.set(ATTACKING, attackManager.isStarted());

            attackManagerTick();

            setFlagLock(isAttacking());
        }

        stateManager.refresh();
    }

    protected void attackManagerTick() {
        attackManager.tick();

        if (attackManager.isAttacking() && getTarget() != null) {
            attackTarget(getTarget());
        }
    }

    void setFlagLock(boolean isLock) {
        if (isLock) {
            for (Goal.Flag value : Goal.Flag.values()) {
                goalSelector.disableControlFlag(value);
            }
        }
        else {
            for (Goal.Flag value : Goal.Flag.values()) {
                goalSelector.enableControlFlag(value);
            }
        }
    }

    @Override
    public void swing(InteractionHand pHand) {
        super.swing(pHand);
        startAttackAnim = true;
    }

    boolean isAttacking() {
        return entityData.get(ATTACKING);
    }

    boolean startAttackAnim;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "standby_controller",
                0, this::standbyPredicate));

        data.add(new AnimationController<>(this, "walk_controller",
                0, this::walkPredicate));

        data.add(new AnimationController<>(this, "attack_controller",
                0, this::attackPredicate));
    }

    private final String standbyAnimName = "animation.%s.standby".formatted(getModelName());
    private final String walkAnimName = "animation.%s.walk".formatted(getModelName());
    private final String attackAnimName = "animation.%s.attack".formatted(getModelName());

    private <T extends GeoAnimatable> PlayState standbyPredicate(AnimationState<T> event) {
        if (stateManager.isActive(STANDBY_STATE)) {
            event.getController().setAnimation(
                    RawAnimation
                            .begin()
                            .then(standbyAnimName, Animation.LoopType.LOOP)
            );
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <T extends GeoAnimatable> PlayState walkPredicate(AnimationState<T> event) {
        if (stateManager.isActive(WALK_STATE)) {
            event.getController().setAnimation(
                    RawAnimation
                            .begin()
                            .then(walkAnimName, Animation.LoopType.LOOP)
            );
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <T extends GeoAnimatable> PlayState attackPredicate(AnimationState<T> event) {
        if (stateManager.isActive(ATTACK_STATE)) {
            AnimationController<?> controller = event.getController();

            if (startAttackAnim) {
                startAttackAnim = false;
                // 清除对话缓存，使得 should loop 为 false 时也可以再次播放动画
                controller.forceAnimationReset();
                controller.setAnimation(RawAnimation.begin().then(attackAnimName, Animation.LoopType.LOOP));
            }

            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public double getTick(Object o) {
        return this.tickCount;
    }

    protected abstract String getModelName();
    protected abstract void attackTarget(LivingEntity target);

    @Override
    public Map<String, String> getDebugAttributes() {
        return ImmutableMap.of(
                "CurrentState", Objects.equals(this.stateManager, NULL) ? "" : Optional.ofNullable(this.stateManager.getActiveState()).orElse(""),
                "isAttack", entityData.get(ATTACKING).toString(),
                "health", "%.2f/%.2f".formatted(this.getHealth(), this.getMaxHealth())
        );
    }
}
