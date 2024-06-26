package teamHTBP.vidaReforged.server.mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.common.mobs.AbstractBeliever;
import teamHTBP.vidaReforged.core.common.mobs.manager.AttackManager;
import teamHTBP.vidaReforged.server.mobs.goal.AttackManagerMeleeAttackGoal;

import static teamHTBP.vidaReforged.core.common.VidaConstant.*;

/**
 * @author DustW
 */
public class AncientBeliever extends AbstractBeliever {

    public AncientBeliever(EntityType<AncientBeliever> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, new AttackManager(20, (int) (.5 * 20)));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 3);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new AttackManagerMeleeAttackGoal(this));
    }

    @Override
    protected String getModelName() {
        return ANCIENT_BELIEVER_NAME;
    }

    @Override
    protected void attackTarget(LivingEntity target) {
        if (getTarget() != null)
            doHurtTarget(getTarget());
    }

}
