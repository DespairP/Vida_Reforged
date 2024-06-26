package teamHTBP.vidaReforged.server.mobs.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import teamHTBP.vidaReforged.server.mobs.OrangeSpottedSparrow;

public class FlyToGroundGoal extends WaterAvoidingRandomStrollGoal {
    public FlyToGroundGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
    }

    @Override
    public boolean canUse() {
        return super.canUse() && (!this.mob.onGround() || this.mob.level().getBlockState(this.mob.getOnPosLegacy()).isAir());
    }

    @Override
    public void start() {
        if(this.mob instanceof OrangeSpottedSparrow animal){
            animal.setWalk(false);
        }
        super.start();
    }

    @Override
    public void stop() {
        if(this.mob instanceof OrangeSpottedSparrow animal){
            animal.setWalk(true);
        }
        super.stop();
    }
}
